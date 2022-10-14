package psycholabs.Forum.controller;

import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import psycholabs.Forum.entity.Page;
import psycholabs.Forum.entity.User;
import psycholabs.Forum.service.UserService;
import psycholabs.Forum.utli.PsycholabsConstant;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController implements PsycholabsConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @RequestMapping("/register")
    public String getRegisterPage() {

        return "/site/register";

    }


    @RequestMapping("/login")
    public String getLoginPage() {
        return "/site/login";

    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功,我们向你邮箱发送了激活邮件");
            model.addAttribute("target", "index");

            return "/site/operate-result";

        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "site/register";
        }

    }

    //activation/161/2166f1d8e2a44bfa909524037b1de498
    @RequestMapping(path = "/activation/{userId}/{activationCode}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("activationCode") String code) {
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "该账号已经激活");
            model.addAttribute("target", "/login");
        } else {
            model.addAttribute("msg", "激活码不正确");
            model.addAttribute("target", "/index");
        }

        return "site/operate-result";

    }

    //生成验证码
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //将验证码存入session
        session.setAttribute("kaptcha", text);
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("响应验证码失败" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String checkUser(String username , String password, String login_code,Boolean remember,HttpSession session,Model model,
                            HttpServletResponse response) {
        System.out.println(remember);
        String kaptcha= (String) session.getAttribute("kaptcha");
        // 首先检查验证码
        if (!login_code.equalsIgnoreCase(kaptcha)){
            model.addAttribute("codeMsg","验证码不正确");
            return "/site/login";

        }
        Date expired = new Date(System.currentTimeMillis()+1000*60*60*100);
        if (remember){
            expired = new Date(System.currentTimeMillis()+1000*60*60*100);

        }

        Map<String,Object> map=userService.login(username,password,expired);

        if (map.containsKey("ticket")) {

            Cookie cookie = new Cookie("ticket", (String) map.get("ticket"));
            //设置cookie生效的范围 指定需要cookie的请求
            cookie.setPath("");
            //设置cookie的保存时间
            cookie.setMaxAge(60 * 60*25);
            response.addCookie(cookie);
            return "redirect:/index";
        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));

            return "/site/login";

        }
    }

    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/login";
    }


}
