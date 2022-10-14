package psycholabs.Forum.service;


import jdk.jshell.execution.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import psycholabs.Forum.dao.LoginTicketMapper;
import psycholabs.Forum.dao.UserMapper;
import psycholabs.Forum.entity.LoginTicket;
import psycholabs.Forum.entity.User;
import psycholabs.Forum.utli.ForumUtli;
import psycholabs.Forum.utli.MailClient;
import psycholabs.Forum.utli.PsycholabsConstant;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements PsycholabsConstant {


    private UserMapper userMapper;

    private MailClient mailClient;

    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Value("${forum.path.domain}")
    private String domain;

//    @Value("${server.servlet.context-path}")
//    private String contextPath;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    @Autowired
    public void setMailClient(MailClient mailClient) {
        this.mailClient = mailClient;
    }
    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    public Map<String,Object> register(User user){
        Map<String,Object> map =new HashMap<>();
        if (user==null){
            throw  new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空");
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
        }
        if (StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
        }


        User u =userMapper.selectByName(user.getUsername());

        if (u!=null){
            map.put("usernameMsg","该账号已存在");
            return map;
        }
        User e =userMapper.selectByEmail(user.getEmail());

        if (e!=null){
            map.put("usernameMsg","该邮箱已被注册");
            return map;
        }

        user.setSalt(ForumUtli.generateUUID().substring(0,5));
        user.setPassword(ForumUtli.generateMD5(user.getPassword()+user.getSalt()));

        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(ForumUtli.generateUUID());

        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        userMapper.insertUser(user);

        // 激活邮件

        Context context =new Context();
        context.setVariable("email",user.getEmail());
        //

        String url =domain+"/activation/"+user.getId()+"/"+user.getActivationCode();

        context.setVariable("url",url);

        String content =templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(),"激活账号",content);
        return map;
    }

    public int activation (int userId , String activationCode){


        User user =userMapper.selectById(userId);

        if (user.getStatus()==1){
            return ACTIVATION_REPEAT;
        }
        else if (user.getActivationCode().equals(activationCode)){
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        }
        else {
            return ACTIVATION_REPEAT;
        }

    }

    public Map<String,Object> login(String username, String password, Date expired){

        //创建一个哈希表来保存返回给网站的信息
        Map<String,Object> map =new HashMap<>();


        //首先检查用户名和密码是否为空
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","账号为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码为空");
            return map;
        }
        // 查询用户是否在数据库中
        User user = userMapper.selectByName(username);

        if (user == null){
            map.put("usernameMsg","账号不存在");
            return map;
        }
        //检查密码
        if (!user.getPassword().equals(ForumUtli.generateMD5(password+user.getSalt()))){
            map.put("passwordMsg","密码不正确");
            return map;
        }
        //生成登陆凭证
        LoginTicket loginTicket =new LoginTicket();

        loginTicket.setUser_id(user.getId());


        loginTicket.setStatus(0);
        loginTicket.setExpired(expired);
        loginTicket.setTicket(ForumUtli.generateUUID()); //调用写好的方法生成登陆凭证

        System.out.println("插入时的数据"+loginTicket);
        loginTicketMapper.insertLoginTicket(loginTicket);


        map.put("ticket",loginTicket.getTicket());

        System.out.println(loginTicketMapper.selectLoginTicket(loginTicket.getTicket()));




        return map;

    }

    public void logout(String ticket){
        loginTicketMapper.updateLoginStatus(1, ticket);
    }

    public LoginTicket findLoginTicket(String ticket){
        return loginTicketMapper.selectLoginTicket(ticket);
    }

    public int loginTicketUserId(String ticket){
        return loginTicketMapper.selectLoginTickByTicket(ticket);
    }

    public int updateHeader(int userId, String headerUrl){
        return userMapper.updateHeader(userId,headerUrl);
    }
}
