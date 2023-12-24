package org.dromara.easyes.dynamic.datasource.provider;

import lombok.extern.slf4j.Slf4j;
import org.dromara.easyes.dynamic.datasource.config.EasyEsDataSourceProperties;
import org.dromara.easyes.dynamic.datasource.creator.DefaultDataSourceCreator;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.Map;

/**
 * Created by xiedong
 * Date: 2023/12/24 14:37
 */
@Slf4j
public class YmlDynamicDataSourceProvider extends AbstractDataSourceProvider {
    /**
     * 所有数据源
     */
    private final Map<String, EasyEsDataSourceProperties> dataSourcePropertiesMap;

    /**
     * 构造函数
     *
     * @param defaultDataSourceCreator 默认数据源创建器
     * @param dataSourcePropertiesMap  数据源参数
     */
    public YmlDynamicDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator, Map<String, EasyEsDataSourceProperties> dataSourcePropertiesMap) {
        super(defaultDataSourceCreator);
        this.dataSourcePropertiesMap = dataSourcePropertiesMap;
    }


    @Override
    public Map<String, RestHighLevelClient> loadDataSources() {
        return createDataSourceMap(dataSourcePropertiesMap);
    }
}
