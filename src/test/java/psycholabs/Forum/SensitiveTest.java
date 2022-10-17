package psycholabs.Forum;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import psycholabs.Forum.utli.SensitiveFilter;

@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSenstiveFilter(){
        String text ="赌博 开票 哈哈哈";
        text =sensitiveFilter.filter(text);
        System.out.println(text);
    }
}
