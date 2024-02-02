package com.izerofx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;

import java.util.Optional;

/**
 * className: Application.java<br>
 * description: 程序主入口<br>
 * createDate: 2022年06月19日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext application = new SpringApplicationBuilder(Application.class).main(SpringVersion.class).run(args);

        Environment env = application.getEnvironment();
        String port = env.getProperty("server.port");
        String path = Optional.ofNullable(env.getProperty("server.servlet.context-path")).orElse("");
        logger.info("应用启动成功，访问地址：http://localhost:{}/{}", port, path);
    }
}
