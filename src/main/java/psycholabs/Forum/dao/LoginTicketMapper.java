package psycholabs.Forum.dao;

import org.apache.ibatis.annotations.*;
import psycholabs.Forum.entity.LoginTicket;
import psycholabs.Forum.entity.User;

import java.util.List;

//放查询LoginTicket的一些接口
@Mapper
public interface LoginTicketMapper {

    @Insert({"INSERT INTO login_ticket(user_id,ticket,status,expired) ",
            "values(#{user_id},#{ticket},#{status},#{expired})",
            })
    @Options(useGeneratedKeys = true ,keyColumn = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Update({"UPDATE login_ticket ","set status=#{status}","where ticket=#{ticket}"})
    int updateLoginStatus(int status,String ticket);



    LoginTicket selectLoginTicket(String ticket);

    @Select(
            {"SELECT user_id from login_ticket where ticket=#{ticket}"}
    )
    int selectLoginTickByTicket(String ticket);


    @Select(
            {"SELECT id,user_id,ticket,status,expired from login_ticket ",
                    "where user_id=#{user_id}"}
    )
    List<LoginTicket> selectLoginUser_id(int user_id);





}
