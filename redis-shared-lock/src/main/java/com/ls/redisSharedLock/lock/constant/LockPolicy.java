package com.ls.redisSharedLock.lock.constant;

/**
 * 访问被锁定资源处理方式
 */
public enum LockPolicy {
    do_nothing,
    throw_exception,
    wait_and_retry
}
