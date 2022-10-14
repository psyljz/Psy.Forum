package psycholabs.Forum.controller.Interceptor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import psycholabs.Forum.annotation.LoginRequired;
import psycholabs.Forum.utli.HostHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断拦截的是不是方法
        if(handler instanceof HandlerMethod handlerMethod){
            Method method =handlerMethod.getMethod();
            LoginRequired loginRequired =method.getAnnotation(LoginRequired.class);
            if (loginRequired!=null && hostHolder.getUser()==null ){
                response.sendRedirect(request.getContextPath()+"/login");
                return false;
            }
        }
        return true;
    }
}
