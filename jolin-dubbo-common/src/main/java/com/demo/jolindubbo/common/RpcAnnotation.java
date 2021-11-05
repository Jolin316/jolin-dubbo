package com.demo.jolindubbo.common;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcAnnotation {
    Class clazz();
}
