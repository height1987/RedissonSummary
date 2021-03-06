package com.height.redis.sample;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class RedisShardedPool {
    private static ShardedJedisPool pool;//sharded jedis连接池
    private static Integer maxTotal = 20; //最大连接数
    private static Integer maxIdle = 20;//在jedispool中最大的idle状态(空闲的)的jedis实例的个数
    private static Integer minIdle = 20;//在jedispool中最小的idle状态(空闲的)的jedis实例的个数

    private static Boolean testOnBorrow = true;//在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。
    private static Boolean testOnReturn = true;//在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。

    private static String redis1Ip = "139.224.33.69";
    private static Integer redis1Port = 6641;




    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true);//连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true。

        JedisShardInfo info1 = null;
        try {
            info1 = new JedisShardInfo(new URI("redis://holder:Toodc600@139.224.33.69:6641?max.idle=2000&max.total=5000&max.wait=15000"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(2);

        jedisShardInfoList.add(info1);
        //这就是我上面说的一些参数啥的hash算法之类的
        pool = new ShardedJedisPool(config,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static{
        initPool();
    }

    public static ShardedJedis getJedis(){
        return pool.getResource();
    }


    public static void close(ShardedJedis jedis){
        try {
            if (jedis != null) {
                jedis.close();
            }
        } catch (Exception e) {

        }
    }


    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();
        System.out.println(jedis.get("key1"));
//        for(int i =0;i<10;i++){
//            jedis.set("key"+i,"value"+i);
//        }
//        close(jedis);
//
//        pool.destroy();//临时调用，销毁连接池中的所有连接
//        System.out.println("program is end");
//        pool.destroy();//临时调用，销毁连接池中的所有连接

    }
}
