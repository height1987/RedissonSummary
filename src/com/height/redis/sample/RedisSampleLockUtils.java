package com.height.redis.sample;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;
import java.util.logging.Logger;

public class RedisSampleLockUtils {

    public static Long setnx(String key, String value,int exTime) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setnx(key, value);
            jedis.expire(key,exTime);
        } catch (Exception e) {
            RedisShardedPool.close(jedis);
            return result;
        }
        RedisShardedPool.close(jedis);
        return result;
    }

    public static Long del(String key){
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
