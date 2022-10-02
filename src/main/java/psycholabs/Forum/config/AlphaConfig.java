package psycholabs.Forum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;

@Repository
public class AlphaConfig {

    @Bean
    public SimpleDateFormat simpleDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }


}
