package com.izerofx.configuration;

import com.izerofx.configuration.properties.MilvusProperties;
import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * className: MilvusClientConfiguration<br>
 * description: Milvus向量数据库客户端配置类<br>
 * createDate: 2024年01月15日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Configuration
public class MilvusClientConfiguration {

    @Bean
    public MilvusServiceClient milvusClient(MilvusProperties properties) {
        return new MilvusServiceClient(ConnectParam.newBuilder()
                .withHost(properties.getHost())
                .withPort(properties.getPort())
                .withDatabaseName(properties.getDatabaseName())
                .withAuthorization(properties.getUsername(), properties.getPassword())
                .withConnectTimeout(properties.getConnectTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .withKeepAliveTime(properties.getKeepAliveTime().toMillis(), TimeUnit.MILLISECONDS)
                .withKeepAliveTimeout(properties.getKeepAliveTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .withIdleTimeout(properties.getIdleTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .build());
    }
}
