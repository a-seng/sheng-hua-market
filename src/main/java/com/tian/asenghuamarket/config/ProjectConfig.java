package com.tian.asenghuamarket.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "project")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectConfig {

    /**
     * 项目名称
     */
    private static String name;

    /**
     * 上传文件路径
     */
    private static String fileUploadPath;

    /**
     * 订单超期未支付，单位秒
     */
    private static Integer orderUnpaidOverTime;

    /**
     * 服务器访问路径
     */
    private static String serverUrl;
}
