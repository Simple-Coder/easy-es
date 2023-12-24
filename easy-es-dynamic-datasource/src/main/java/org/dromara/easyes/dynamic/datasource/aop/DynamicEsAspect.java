package org.dromara.easyes.dynamic.datasource.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.dromara.easyes.annotation.rely.DefaultChildClass;
import org.dromara.easyes.common.enums.ProcessIndexStrategyEnum;
import org.dromara.easyes.common.utils.LogUtils;
import org.dromara.easyes.common.utils.TypeUtils;
import org.dromara.easyes.core.biz.EntityInfo;
import org.dromara.easyes.core.config.GlobalConfig;
import org.dromara.easyes.core.core.BaseEsMapperImpl;
import org.dromara.easyes.core.toolkit.EntityInfoHelper;
import org.dromara.easyes.dynamic.datasource.annotation.DynamicEs;
import org.dromara.easyes.dynamic.datasource.config.EasyEsDataSourceProperties;
import org.dromara.easyes.dynamic.datasource.core.DynamicRoutingDataSource;
import org.dromara.easyes.dynamic.datasource.holder.DynamicDataSourceContextHolder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;


/**
 * Created by xiedong
 * Date: 2023/12/24 16:13
 */
@Aspect
public class DynamicEsAspect {
    @Resource
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Pointcut("@annotation(org.dromara.easyes.dynamic.datasource.annotation.DynamicEs)")
    public void dynamicEsAspectAnnotationPointcut() {
    }

    @Around("dynamicEsAspectAnnotationPointcut() ")
    public Object methodsAnnotatedWithDynamicEs(final ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            DynamicEs dynamicEs = AnnotationUtils.getAnnotation(signature.getMethod(), DynamicEs.class);
            DynamicDataSourceContextHolder.push(Objects.isNull(dynamicEs) ? null : dynamicEs.value());
            return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }
}
