package psycholabs.Forum.service;


import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import psycholabs.Forum.dao.DiscussPostMapper;
import psycholabs.Forum.entity.DiscussPost;
import psycholabs.Forum.entity.User;

import java.util.List;

@Service
public class DiscussService {


    private DiscussPostMapper discussPostMapper;


    //使用Setter注入
    @Autowired
    public void setDiscussPostMapper(DiscussPostMapper discussPostMapper) {
        this.discussPostMapper = discussPostMapper;
    }



    public List<DiscussPost> findDiscussPost(int userId, int offset, int limit){

        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }


    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }

}
