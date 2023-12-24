package org.dromara.easyes.dynamic.datasource.core;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.easyes.dynamic.datasource.config.DynamicDataSourceProperties;
import org.dromara.easyes.dynamic.datasource.config.EasyEsDataSourceProperties;
import org.dromara.easyes.dynamic.datasource.provider.DynamicDataSourceProvider;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xiedong
 * Date: 2023/12/24 14:59
 */
@Slf4j
public class DynamicRoutingDataSource implements InitializingBean, DisposableBean {
    /**
     * 所有数据库
     */
    private final Map<String, RestHighLevelClient> dataSourceMap = new ConcurrentHashMap<>();

    @Autowired
    private List<DynamicDataSourceProvider> providers;

    @Autowired
    private DynamicDataSourceProperties dynamicDataSourceProperties;
    @Setter
    private String primary = "master";


    public Map<String, EasyEsDataSourceProperties> getAllInstanceProperties() {
        return this.dynamicDataSourceProperties.getDatasource();
    }

    public EasyEsDataSourceProperties getProperties(String name) {
        return this.dynamicDataSourceProperties.getDatasource().get(name);
    }

    public RestHighLevelClient determinePrimaryDataSource() {
        log.debug("dynamic-datasource switch to the primary datasource");
        RestHighLevelClient dataSource = dataSourceMap.get(primary);
        if (dataSource != null) {
            return dataSource;
        }
        throw new RuntimeException("dynamic-datasource can not find primary datasource");
    }
    public EasyEsDataSourceProperties determinePrimaryDataSourceprop() {
        return this.getProperties(primary);
    }

    /**
     * 获取数据源
     *
     * @param ds 数据源名称
     * @return 数据源
     */
    public RestHighLevelClient getDataSource(String ds) {
        if (StringUtils.isEmpty(ds)) {
            return determinePrimaryDataSource();
        } else if (dataSourceMap.containsKey(ds)) {
            log.debug("dynamic-datasource switch to the datasource named [{}]", ds);
            return dataSourceMap.get(ds);
        }
        return determinePrimaryDataSource();
    }
    @Override
    public void destroy() throws Exception {
        log.info("dynamic-datasource start closing ....");
        for (Map.Entry<String, RestHighLevelClient> item : dataSourceMap.entrySet()) {
            closeDataSource(item.getKey(), item.getValue());
        }
        log.info("dynamic-datasource all closed success,bye");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (DynamicDataSourceProvider provider : providers) {
            Map<String, RestHighLevelClient> dsMap = provider.loadDataSources();
            if (dsMap != null) {
                dataSourceMap.putAll(dsMap);
            }
        }
    }

    private void closeDataSource(String ds, RestHighLevelClient dataSource) {
        try {
            dataSource.close();
        } catch (Exception e) {
            log.warn("dynamic-datasource closed datasource named [{}] failed", ds, e);
        }
    }
}
