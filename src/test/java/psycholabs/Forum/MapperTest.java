package psycholabs.Forum;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import psycholabs.Forum.dao.DiscussPostMapper;
import psycholabs.Forum.dao.LoginTicketMapper;
import psycholabs.Forum.dao.UserMapper;
import psycholabs.Forum.entity.DiscussPost;
import psycholabs.Forum.entity.LoginTicket;
import psycholabs.Forum.entity.User;

import java.util.Date;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void selectUserById() {
        User user = userMapper.selectById(101);
        System.out.println(user);
        User user1 = userMapper.selectByName("liubei");
        System.out.println(user1);
        User user2 = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user2);
    }

    @Test
    public void insertUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("liujingze@gamil.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());
        System.out.println(user);
        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());

    }

    @Test
    public void updateUser() {
        int rows = userMapper.updateStatus(150, 1);

        rows = userMapper.updateHeader(150, "http://www.nowcoder.com/102.png");

        rows = userMapper.updatePassword(150, "hello");

    }

    @Test
    public void postMethTest(){

        List<DiscussPost> rrr = discussPostMapper.selectDiscussPosts(0,0,1000);
        rrr.forEach(System.out::println);
//        int rows = discussPostMapper.selectDiscussPostRows(0);
//        int row = discussPostMapper.selectDiscussPostRows(149);
//        System.out.println(rows);

    }

    @Test
    public void testInsertLoginTicketMapper(){
        LoginTicket loginTicket =new LoginTicket();
        loginTicket.setTicket("1123");
        loginTicket.setUser_id(122);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));
        loginTicket.setStatus(0);

        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void updateInsertLoginTicketMapper(){

        loginTicketMapper.updateLoginStatus(1,"1123");
    }

    @Test
    public void selectLoginTicketMapper(){
        LoginTicket loginTicket=loginTicketMapper.selectLoginTicket("15978ff557dc45ec87af091042966a74");
        System.out.println(loginTicket);
        System.out.println(loginTicket.getUser_id());
    }
    @Test
    public void selectLoginUsrIdMapper(){
        List<LoginTicket> loginTicket=loginTicketMapper.selectLoginUser_id(163);
        loginTicket.forEach(System.out::println);
    }

    @Test
    public void selectLoginUsrid(){
        int user_id =loginTicketMapper.selectLoginTickByTicket("15978ff557dc45ec87af091042966a74");
        System.out.println(user_id);
    }


    @Test
    public void insertDisscussPost(){
        DiscussPost post = new DiscussPost();
        post.setTitle("相亲贴2");
        post.setContent("我要找个大哥");
        post.setType(0);
        post.setUserId(163);
        post.setCreateTime(new Date());

        discussPostMapper.insertDiscussPost(post);
    }


}