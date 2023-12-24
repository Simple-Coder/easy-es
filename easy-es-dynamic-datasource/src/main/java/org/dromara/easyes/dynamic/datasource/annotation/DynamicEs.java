package org.dromara.easyes.dynamic.datasource.annotation;

import java.lang.annotation.*;

/**
 * Created by xiedong
 * Date: 2023/12/24 15:28
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicEs {
    String value();
}
