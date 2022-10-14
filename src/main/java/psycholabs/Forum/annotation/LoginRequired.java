package psycholabs.Forum.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//该注解可以加什么类型上
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME) //注解起作用的范围
public @interface LoginRequired {
}
