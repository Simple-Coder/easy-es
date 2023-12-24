package org.dromara.easyes.starter.proxy;

import org.dromara.easyes.core.cache.BaseCache;
import org.dromara.easyes.core.core.BaseEsMapperImpl;
import org.dromara.easyes.dynamic.datasource.core.DynamicRoutingDataSource;
import org.dromara.easyes.dynamic.datasource.holder.DynamicDataSourceContextHolder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理类
 * <p>
 * Copyright © 2021 xpc1024 All Rights Reserved
 **/
public class EsMapperProxy<T> implements InvocationHandler, Serializable {
    private static final long serialVersionUID = 1L;
    private Class<T> mapperInterface;

    private DynamicRoutingDataSource dynamicRoutingDataSource;

    public EsMapperProxy(Class<T> mapperInterface, DynamicRoutingDataSource dynamicRoutingDataSource) {
        this.mapperInterface = mapperInterface;
        this.dynamicRoutingDataSource = dynamicRoutingDataSource;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        BaseEsMapperImpl<?> baseEsMapperInstance = BaseCache.getBaseEsMapperInstance(mapperInterface);

        String ds = DynamicDataSourceContextHolder.peek();
        RestHighLevelClient dataSource = dynamicRoutingDataSource.getDataSource(ds);
        baseEsMapperInstance.setClient(dataSource);
        // 这里如果后续需要像MP那样 从xml生成代理的其它方法,则可增强method,此处并不需要
        return method.invoke(baseEsMapperInstance, args);
    }

}
