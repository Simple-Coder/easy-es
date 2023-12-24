package org.dromara.easyes.dynamic.datasource.provider;

import org.elasticsearch.client.RestHighLevelClient;

import java.util.Map;

/**
 * Created by xiedong
 * Date: 2023/12/24 14:33
 */
public interface DynamicDataSourceProvider {

    /**
     * 加载所有数据源
     *
     * @return 所有数据源，key为数据源名称
     */
    Map<String, RestHighLevelClient> loadDataSources();
}
