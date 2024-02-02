package com.izerofx.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.izerofx.common.web.filter.LogMDCFilter;
import com.izerofx.common.web.serializer.CustomLongConverter;
import com.izerofx.common.web.serializer.CustomSerializerProvider;
import com.izerofx.common.web.serializer.NullStringJsonSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * className: WebAppConfiguration.java<br>
 * description: 应用配置<br>
 * createDate: 2022年06月19日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Configuration
public class WebAppConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/favicon.ico");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowedHeaders("*");
    }

    @Bean
    @ConditionalOnClass({StringHttpMessageConverter.class})
    public StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        WebMvcConfigurer.super.configureMessageConverters(converters);

        converters.add(stringHttpMessageConverter());

        // 配置jackson
        Jackson2ObjectMapperBuilder jackson2ObjectMapper = new Jackson2ObjectMapperBuilder();
        jackson2ObjectMapper.indentOutput(true).propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        jackson2ObjectMapper.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 自定义Long类型转换 超过12个数字用String格式返回，由于js的number只能表示15个数字
        jackson2ObjectMapper.serializerByType(Long.class, new CustomLongConverter());
        jackson2ObjectMapper.serializerByType(Long.TYPE, new CustomLongConverter());
        jackson2ObjectMapper.serializerByType(String.class, new NullStringJsonSerializer());

        ObjectMapper jacksonMapper = jackson2ObjectMapper.build();
        jacksonMapper.setSerializerProvider(new CustomSerializerProvider());
        jacksonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        converters.add(0, new MappingJackson2HttpMessageConverter(jacksonMapper));
    }

    @Bean
    public FilterRegistrationBean<LogMDCFilter> logFilterRegistration() {
        FilterRegistrationBean<LogMDCFilter> registration = new FilterRegistrationBean<>();
        // 注入过滤器
        registration.setFilter(new LogMDCFilter());
        // 拦截规则
        registration.addUrlPatterns("/*");
        // 过滤器名称
        registration.setName("logMDCFilter");
        // 过滤器顺序
        registration.setOrder(0);
        return registration;
    }
}
