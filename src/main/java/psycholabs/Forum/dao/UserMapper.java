package psycholabs.Forum.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import psycholabs.Forum.entity.User;


// 把需要查找的数据进行映射
@Mapper
public interface UserMapper {

    User selectById(int id);
    User selectByName(String username);
    User selectByEmail(String email);

    int insertUser(User user);
    int updateStatus(int id , int status);
    int updateHeader(int id , String headerUrl);
    int updatePassword(int id , String password);
}
