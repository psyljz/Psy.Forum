package psycholabs.Forum.controller;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import psycholabs.Forum.annotation.LoginRequired;
import psycholabs.Forum.controller.Interceptor.AlphaInterceptor;
import psycholabs.Forum.entity.Page;
import psycholabs.Forum.entity.User;
import psycholabs.Forum.service.UserService;
import psycholabs.Forum.utli.ForumUtli;
import psycholabs.Forum.utli.HostHolder;
import psycholabs.Forum.utli.MailClient;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger= LoggerFactory.getLogger(UserController.class);

    @Value("${forum.path.upload}")
    private String uploadPath;

    @Value("${forum.path.domain}")
    private String domain;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){

        if (headerImage==null){
            model.addAttribute("error","您还没有选择图片");
            return "/site/setting";
        }
        // 获取文件后缀名
        String filename =headerImage.getOriginalFilename();
        String suffix =filename.substring(filename.lastIndexOf("."));

        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","您还没有选择图片");
            return "/site/setting";
        }

        //生成随机的文件名
        String FileName =ForumUtli.generateUUID() +suffix;
        //确定文件存放的路径
        File dest =new File(uploadPath+FileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
           logger.error("上传文件失败"+e.getMessage());
           throw new RuntimeException("上传文件失败，服务器发生异常",e);
        }
        // 更新当前用户头像的路径(web访问路径)

        //http://localhost:8080/user/header/xxx.png

        User user=hostHolder.getUser();
        String headerUrl =domain+"/user/header/"+FileName;
        userService.updateHeader(user.getId(),headerUrl);

        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{filename}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("filename") String filename, HttpServletResponse response){
        //从服务器找到存放路径
        filename = uploadPath+"/" +filename;
        //解析文件后缀
        String suffix =filename.substring(filename.lastIndexOf("."));
        //响应图片
        response.setContentType("image/"+suffix);
        try (FileInputStream fis =new FileInputStream(filename);
             OutputStream os =response.getOutputStream();)
        {

            byte[] buffer =new byte[1024];
            int b=0;
            while ((b=fis.read(buffer))!=-1){

                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败"+e.getMessage());
        }

    }






}
