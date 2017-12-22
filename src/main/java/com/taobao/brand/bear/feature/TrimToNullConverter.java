package com.taobao.brand.bear.feature;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author jinshuan.li 2017/12/23 06:11
 * <p>
 * 注册一个数据转化类 避免后面写了空格造成一些恶心的错误
 */

@ConfigurationPropertiesBinding
@Component
public class TrimToNullConverter implements Converter<String, String> {
    @Override
    public String convert(String source) {
        return StringUtils.trimToNull(source);
    }
}
