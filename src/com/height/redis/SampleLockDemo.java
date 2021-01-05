package com.height.redis;

import com.height.redis.sample.RedisSampleLockUtils;


public class SampleLockDemo {
    public static void main(String args[]){
        String keyName = "MyKeyName";
        RedisSampleLockUtils.lock(keyName,3600);
        doOneThing();
        RedisSampleLockUtils.unlock(keyName);
    }
    private static void doOneThing() {
        System.out.println("do twoThing");
    }
}
