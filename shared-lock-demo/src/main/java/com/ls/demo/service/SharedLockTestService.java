package com.ls.demo.service;

import com.ls.redisSharedLock.lock.annotion.SharedLock;
import com.ls.redisSharedLock.lock.constant.LockPolicy;
import org.springframework.stereotype.Service;

@Service
public class SharedLockTestService {

    @SharedLock(key = "test_#{lockParams}")
    public void lock(String lockParams){

        System.out.println("lock方法开始,lockparams="+lockParams);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("lock方法结束,lockparams="+lockParams);
    }


    @SharedLock(key = "test_#{lockParams2}",policy = LockPolicy.do_nothing)
    public void lockWithDoNothingPolicy(String lockParams2){

        System.out.println("lock方法开始,lockparams="+lockParams2);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("lock方法结束,lockparams="+lockParams2);
    }

    @SharedLock(key = "test_#{lockParams2}",policy = LockPolicy.wait_and_retry,waitTime = 1000l,maxRetryTimes = 5)
    public void lockWaitAndRetry(String lockParams2){

        System.out.println("lock方法开始,lockparams="+lockParams2);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("lock方法结束,lockparams="+lockParams2);
    }

}
