package psycholabs.Forum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import psycholabs.Forum.dao.LoginTicketMapper;
import psycholabs.Forum.entity.LoginTicket;
import psycholabs.Forum.utli.MailClient;

import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class MailTest {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;



    @Test
    public void testTextMail() {

        mailClient.sendMail("893000386@qq.com", "test", "你的天啊啊");
    }

    @Test
    public void testHTMLMail() {

        Context context = new Context();
        context.setVariable("username", "sunday");
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);
        mailClient.sendMail("liujingze@skiff.com", "HTMLtest", content);
    }

}
