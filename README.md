# redis-shared-lock

#### 项目介绍
基于redis的分布式共享锁，使用注解的方式对方法加锁


#### 使用说明
在pom文件中引用redis-shared-lock模块，并做相关redis配置后即可使用。
在需要加锁的方法上加上@SharedLock注解
具体参照demo项目
