package psycholabs.Forum.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import psycholabs.Forum.annotation.LoginRequired;
import psycholabs.Forum.entity.DiscussPost;
import psycholabs.Forum.entity.User;
import psycholabs.Forum.service.DiscussService;
import psycholabs.Forum.utli.ForumUtli;
import psycholabs.Forum.utli.HostHolder;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussService discussService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {

        System.out.println(">>>>>>>>>>>>>>>收到发送的请求<<<<<<<<<<<<");

        User user = hostHolder.getUser();
        if (user == null) {
            return ForumUtli.getJSONString(403, "你没有登陆大哥");
        }

        DiscussPost discussPost= new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setContent(content);
        discussPost.setTitle(title);
        discussPost.setCreateTime(new Date());

        discussService.addDiscussPost(discussPost);

        return ForumUtli.getJSONString(0,"消息发布成功");


    }
}
