package psycholabs.Forum.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psycholabs.Forum.dao.UserMapper;
import psycholabs.Forum.entity.User;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id){
        return userMapper.selectById(id);
    }
}
