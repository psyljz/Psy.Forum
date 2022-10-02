package psycholabs.Forum.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;
import psycholabs.Forum.entity.DiscussPost;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //@Param注解用于参数取别名，
    //如果只有一个参数，并且在<if>里，则必须加别名
    int selectDiscussPostRows(@Param("userId") int userId);


}

