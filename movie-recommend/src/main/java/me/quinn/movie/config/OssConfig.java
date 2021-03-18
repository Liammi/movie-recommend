package me.quinn.movie.config;

import com.aliyun.oss.OSSClient;
import me.quinn.movie.enums.OssConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OSS客户端配置类
 */
@Configuration
public class OssConfig {

    /**
     * 定义ossClient bean对象
     */
    @Bean
    public OSSClient ossClient() {
        return new OSSClient(OssConstant.END_POINT, OssConstant.ACCESS_KEY_ID, OssConstant.ACCESS_KEY_SECRET);
    }
}