package org.dromara.easyes.dynamic.datasource.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xiedong
 * Date: 2023/12/24 14:26
 */
@Slf4j
@Getter
@Setter
@ConfigurationProperties(prefix = DynamicDataSourceProperties.PREFIX)
public class DynamicDataSourceProperties {
    public static final String PREFIX = "easy-es.datasource.dynamic";

    /**
     * 必须设置默认的库,默认master
     */
    private String primary = "master";
    /**
     * 每一个数据源
     */
    private Map<String, EasyEsDataSourceProperties> datasource = new LinkedHashMap<>();

}
