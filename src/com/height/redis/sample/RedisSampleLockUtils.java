package com.height.redis.sample;

import redis.clients.jedis.ShardedJedis;


public class RedisSampleLockUtils {

    public static Long SUCCESS_CODE = 1L;
    public static Long ERROR_CODE = 0L;
    private static String KEY_PLACEHOLDER = "KEY_HOLDER";

    public static boolean lock(String key, int exTime) {
        Long setnx = setnx(key, KEY_PLACEHOLDER, exTime);
        return SUCCESS_CODE.equals(setnx);
    }

    public static boolean unlock(String key) {
        Long del = del(key);
        return SUCCESS_CODE.equals(del);
    }


    private static Long setnx(String key, String value, int exTime) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setnx(key, value);
            jedis.expire(key, exTime);
        } catch (Exception e) {
            RedisShardedPool.close(jedis);
            return result;
        }
        RedisShardedPool.close(jedis);
        return result;
    }

    private static Long del(String key) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            RedisShardedPool.close(jedis);
            return result;
        }
        RedisShardedPool.close(jedis);
        return result;
    }


    public static void main(String[] args) {
        ShardedJedis jedis = RedisShardedPool.getJedis();

        System.out.println(jedis.get("key1"));

        System.out.println("end");
    }

}
