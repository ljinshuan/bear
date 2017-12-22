package com.taobao.brand.bear.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author jinshuan.li 2017/12/23 05:54
 */

@Component
@ConfigurationProperties(value = "user")
@Data
public class UserProperties {


    private String username;

}
