package net.siehe.wiki.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
// 含@ComponentScan但只会扫描到所在包下的子包，非同包加@ComponentScan("包名")多个包({"包名1","包名2"})
@ComponentScan("net.siehe")
@SpringBootApplication
public class WikiApplication {

    private static final Logger LOG = LoggerFactory.getLogger(WikiApplication.class);

    public static void main(String[] args) {
//        SpringApplication.run(WikiApplication.class, args);
        SpringApplication app = new SpringApplication(WikiApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("=====================启动成功=======================");
        LOG.info("地址:http://127.0.0.1:{}",env.getProperty("server.port"));
    }

}
