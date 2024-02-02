package com.izerofx.common.exception.handle;

import jakarta.servlet.Servlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.List;

/**
 * className: ExceptionControllerConfiguration.java<br>
 * description: 异常控制配置<br>
 * createDate: 2022年06月19日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 *
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(WebProperties.class)
public class ExceptionControllerConfiguration {
    /**
     * errorViewResolvers
     */
    @Autowired(required = false)
    private List<ErrorViewResolver> errorViewResolvers;

    /**
     * serverProperties
     */
    private final ServerProperties serverProperties;

    /**
     * 构造函数
     *
     * @param serverProperties serverProperties
     */
    public ExceptionControllerConfiguration(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    /**
     * exceptionController
     *
     * @param errorAttributes errorAttributes
     * @return ExceptionController
     */
    @Bean
    @ConditionalOnMissingBean(value = ErrorController.class, search = SearchStrategy.CURRENT)
    public ExceptionController exceptionController(ErrorAttributes errorAttributes) {
        return new ExceptionController(errorAttributes, this.serverProperties, this.errorViewResolvers);
    }


}
