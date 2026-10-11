package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//阿里云的一个配置类
@Configuration
@Slf4j
public class OssConfiguration {
    @Autowired
    AliOssProperties aliOssProperties;
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(){
        log.info("开始创建阿里云上传工具类对象{}",aliOssProperties);
        AliOssUtil aliOssUtil=new AliOssUtil(aliOssProperties.getEndpoint(),aliOssProperties.getAccessKeyId(),aliOssProperties.getAccessKeySecret(),aliOssProperties.getBucketName() );
        return  aliOssUtil;

    }
}
