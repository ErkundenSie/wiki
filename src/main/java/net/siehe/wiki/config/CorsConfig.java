package net.siehe.wiki.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类解决跨域问题
 * 跨域可以这样理解，来自一个IP端口的页面（vue项目），要求访问另一个IP端口的资源（springboot请求接口），
 * 会产生跨域访问问题
 * 修改了配置类或者pom.xml建议重启应用，不要热部署
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedHeaders(CorsConfiguration.ALL)
                .allowedMethods(CorsConfiguration.ALL)
                .allowCredentials(true)
                .maxAge(3600);//1小时内不再进行预检，在调用电子书接口之前，会先发一个OPTOBS请求
    }
}
