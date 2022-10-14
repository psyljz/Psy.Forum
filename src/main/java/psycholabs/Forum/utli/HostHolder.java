package psycholabs.Forum.utli;


import org.springframework.stereotype.Component;
import psycholabs.Forum.entity.User;

/**
 * 用于代替session对象
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users =new ThreadLocal<>();
    public void setUsers(User user){
        users.set(user);
    }
    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }

}
