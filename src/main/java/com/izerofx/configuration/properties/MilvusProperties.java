package com.izerofx.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * className: MilvusProperties<br>
 * description: Milvus属性文件<br>
 * createDate: 2024年01月15日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.milvus")
public class MilvusProperties {

    /**
     * 地址
     */
    private String host = "localhost";

    /**
     * 端口
     */
    private int port = 19530;

    /**
     * 数据库名
     */
    private String databaseName = "default";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 连接超时时间（默认：30秒)
     */
    private Duration connectTimeout = Duration.ofSeconds(30);

    /**
     * 保持活动时间（默认：60秒)
     */
    private Duration keepAliveTime = Duration.ofSeconds(60);

    /**
     * 保持活动超时时间（默认：30秒)
     */
    private Duration keepAliveTimeout = Duration.ofSeconds(30);

    /**
     * 空闲连接超时时间（默认24小时）
     */
    private Duration idleTimeout = Duration.ofHours(24L);
}
