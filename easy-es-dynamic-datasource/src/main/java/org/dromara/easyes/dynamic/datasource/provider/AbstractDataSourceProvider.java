package org.dromara.easyes.dynamic.datasource.provider;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.easyes.dynamic.datasource.config.EasyEsDataSourceProperties;
import org.dromara.easyes.dynamic.datasource.creator.DefaultDataSourceCreator;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiedong
 * Date: 2023/12/24 14:34
 */
@Slf4j
@AllArgsConstructor
public abstract class AbstractDataSourceProvider implements DynamicDataSourceProvider {
    private final DefaultDataSourceCreator defaultDataSourceCreator;

    /**
     * 创建数据源
     *
     * @param dataSourcePropertiesMap 数据源参数Map
     * @return 数据源Map
     */
    protected Map<String, RestHighLevelClient> createDataSourceMap(Map<String, EasyEsDataSourceProperties> dataSourcePropertiesMap) {
        Map<String, RestHighLevelClient> dataSourceMap = new HashMap<>(dataSourcePropertiesMap.size() * 2);
        for (Map.Entry<String, EasyEsDataSourceProperties> item : dataSourcePropertiesMap.entrySet()) {
            String dsName = item.getKey();
            EasyEsDataSourceProperties dataSourceProperty = item.getValue();
            RestHighLevelClient dataSource = defaultDataSourceCreator.createDataSource(dataSourceProperty);
            dataSourceMap.put(dsName, dataSource);
        }
        return dataSourceMap;
    }
}
