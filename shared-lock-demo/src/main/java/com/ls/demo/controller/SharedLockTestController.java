package com.ls.demo.controller;

import com.ls.demo.service.SharedLockTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class SharedLockTestController {

    @Autowired
    private SharedLockTestService sharedLockTestService;

    @RequestMapping("1")
    public String testLock(String lockParam) {
        try {
            sharedLockTestService.lock(lockParam);
            return "success";
        } catch (Exception e) {
            return "error:" + lockParam + "has been locked";
        }

    }

    @RequestMapping("2")
    public String testLock2(String lockParam) {
        sharedLockTestService.lockWithDoNothingPolicy(lockParam);
        return "success";

    }


    @RequestMapping("3")
    public String testLock3(String lockParam) {
        sharedLockTestService.lockWaitAndRetry(lockParam);
        return "success";

    }

}
