package psycholabs.Forum.utli;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class ForumUtli {

    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");

    }

    //MD5加密
    public static String generateMD5(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }

        return DigestUtils.md5DigestAsHex(key.getBytes());

    }
}
