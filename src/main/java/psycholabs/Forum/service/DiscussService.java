package psycholabs.Forum.service;


import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import psycholabs.Forum.dao.DiscussPostMapper;
import psycholabs.Forum.entity.DiscussPost;
import psycholabs.Forum.entity.User;
import psycholabs.Forum.utli.SensitiveFilter;

import java.util.List;

@Service
public class DiscussService {


    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;


    //使用Setter注入
    @Autowired
    public void setDiscussPostMapper(DiscussPostMapper discussPostMapper) {
        this.discussPostMapper = discussPostMapper;
    }


    public List<DiscussPost> findDiscussPost(int userId, int offset, int limit) {

        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }


    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    //添加一个发帖子的方法
    public int addDiscussPost(DiscussPost Post) {

        //
        if (Post == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        //标签
        Post.setTitle(HtmlUtils.htmlEscape(Post.getTitle()));
        Post.setContent(HtmlUtils.htmlEscape(Post.getContent()));

        //过滤敏感词
        Post.setContent(sensitiveFilter.filter(Post.getContent()));
        Post.setTitle(sensitiveFilter.filter(Post.getTitle()));

        return discussPostMapper.insertDiscussPost(Post);

    }

}
