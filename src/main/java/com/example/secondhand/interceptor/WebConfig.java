package com.example.secondhand.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")
                .addPathPatterns("/myProduct/**")
                .addPathPatterns("/products/**")
                .addPathPatterns("/payOrder/**")
                .addPathPatterns("/order/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/index")
                .excludePathPatterns("/types")
                .excludePathPatterns("/admin")
                .excludePathPatterns("/login")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/**/*.jpg")
                .excludePathPatterns("/**/*.png")
                .excludePathPatterns("/**/*.jpeg")
                .excludePathPatterns("/static/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //获取文件的真实路径 work_project代表项目工程名 需要更改
        String path = System.getProperty("user.dir")+"/src/main/resources/static/uploadFile/";
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            registry.addResourceHandler("/uploadFile/**").
                    addResourceLocations("file:"+path);
        }else{//linux和mac系统 可以根据逻辑再做处理
            registry.addResourceHandler("/uploadFile/**").
                    addResourceLocations("file:"+path);
        }
        super.addResourceHandlers(registry);
    }
}
