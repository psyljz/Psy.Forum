package psycholabs.Forum.controller.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import psycholabs.Forum.entity.LoginTicket;
import psycholabs.Forum.entity.User;
import psycholabs.Forum.service.UserService;
import psycholabs.Forum.utli.CookieUtil;
import psycholabs.Forum.utli.HostHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class TicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;
    //查找请求中是有用户的ticket信息
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 从cookie中获取凭证
        String ticket = CookieUtil.getValue(request,"ticket");
        if (ticket!=null){
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //检查凭证是否有效
            if(loginTicket!=null && loginTicket.getStatus()==0 && loginTicket.getExpired().after(new Date())){

                System.out.println(loginTicket);


                User user=userService.findUserById(userService.loginTicketUserId(ticket));

                System.out.println("-----------");
                System.out.println(user.getId());
                System.out.println("-----------");

                //在本次请求中持用用户
                // 在多个线程中隔离
                hostHolder.setUsers(user);

            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user!=null && modelAndView !=null){
            modelAndView.addObject("loginUser",user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
