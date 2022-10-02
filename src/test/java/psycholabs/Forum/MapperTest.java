package psycholabs.Forum;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import psycholabs.Forum.dao.DiscussPostMapper;
import psycholabs.Forum.dao.UserMapper;
import psycholabs.Forum.entity.DiscussPost;
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
}