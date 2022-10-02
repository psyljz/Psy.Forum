package psycholabs.Forum.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@RequestMapping("/alpha")
@Controller
public class alphaController {


    @RequestMapping("/http")
    public void httpTest(HttpServletRequest request, HttpServletResponse response) {
        //获取请求的数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());


        Enumeration<String> enumeration = request.getHeaderNames();

        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ":" + value);
        }

        System.out.println(request.getParameter("code"));

        //设置返回的内容
        response.setContentType("text/html;charset=utf-8");

        try (PrintWriter writer = response.getWriter();) {

            writer.write("<h1>Psych我的天o</h1>");
            writer.write("<h1>Psych我的天o</h1>");
            writer.write("<h1>Psych我的天o</h1>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //获取GET请求中的信息的方法
    //1.
    @RequestMapping(path = "/students",method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(@RequestParam(name = "current",required = false,defaultValue = "1") int current,
                              @RequestParam(name = "limit",required = false,defaultValue = "1") int limit)
    {
        System.out.println(current);
        System.out.println(limit);
        return "some student";
    }
    //2.
    @RequestMapping(path = "/students/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){
        System.out.println(id);
        return "a student";

    }

    //Post请求
    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String addStudent(String name,int age){

        System.out.println(name);
        System.out.println(age);

        return "success";

    }

    @RequestMapping(path = "/teacher",method = RequestMethod.GET)
    public ModelAndView teacher(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name","张三");
        modelAndView.addObject("age","55");
        modelAndView.setViewName("/demo/view");

        return modelAndView;
    }

    @RequestMapping(path = "/school",method = RequestMethod.GET)
    public String getSchool(Model model){

        model.addAttribute("name","北京大学");
        model.addAttribute("age","120");

        return "/demo/view";
    }
    // 响应Json数据对象(异步请求)
    // Java对象 ->JSON字符串 ->JS对象

    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmp(){

        List<Map<String, Object>> list = new ArrayList<>();

        HashMap<String, Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age",23);
        emp.put("salary",8000);
        list.add(emp);

        HashMap<String, Object> emp2 = new HashMap<>();
        emp2.put("name","张三");
        emp2.put("age",23);
        emp2.put("salary",8000);
        list.add(emp2);

        return list;


    }






}
