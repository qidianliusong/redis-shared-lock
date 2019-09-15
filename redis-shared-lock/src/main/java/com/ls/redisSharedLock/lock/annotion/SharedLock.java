package com.ls.redisSharedLock.lock.annotion;

import com.ls.redisSharedLock.lock.constant.LockPolicy;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 共享锁（使用该注解时对方法加锁）
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SharedLock {

    @AliasFor("key")
    String value() default "";

    @AliasFor("value")
    String key();

    LockPolicy policy() default LockPolicy.throw_exception;

    long maxLockTime() default 300000l;

    /**
     * LockPolicy为wait_and_retry模式时的等待重试时间,单位是毫秒
     * @return
     */
    long waitTime() default 500l;

    /**
     * LockPolicy为wait_and_retry模式时的重试次数
     * @return
     */
    int maxRetryTimes() default 10;

}
