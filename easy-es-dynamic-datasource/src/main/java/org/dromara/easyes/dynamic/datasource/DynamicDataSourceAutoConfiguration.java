package org.dromara.easyes.dynamic.datasource;

import lombok.extern.slf4j.Slf4j;
import org.dromara.easyes.dynamic.datasource.aop.DynamicEsAspect;
import org.dromara.easyes.dynamic.datasource.config.DynamicDataSourceProperties;
import org.dromara.easyes.dynamic.datasource.core.DynamicRoutingDataSource;
import org.dromara.easyes.dynamic.datasource.creator.DefaultDataSourceCreator;
import org.dromara.easyes.dynamic.datasource.provider.DynamicDataSourceProvider;
import org.dromara.easyes.dynamic.datasource.provider.YmlDynamicDataSourceProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Created by xiedong
 * Date: 2023/12/24 14:40
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DynamicDataSourceAutoConfiguration implements InitializingBean {
    private final DynamicDataSourceProperties properties;


    public DynamicDataSourceAutoConfiguration(
            DynamicDataSourceProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicEsAspect dynamicEsAspect() {
        return new DynamicEsAspect();
    }

    @Bean
    @Order(0)
    public DynamicDataSourceProvider ymlDynamicDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator) {
        return new YmlDynamicDataSourceProvider(defaultDataSourceCreator, properties.getDatasource());
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultDataSourceCreator dataSourceCreator() {
        DefaultDataSourceCreator creator = new DefaultDataSourceCreator();
        return creator;
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicRoutingDataSource dataSource() {
        return new DynamicRoutingDataSource();
    }

    @Override
    public void afterPropertiesSet() {
    }
}
