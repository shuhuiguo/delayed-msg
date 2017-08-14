package com.shuhg.utils;

import com.shuhg.Main;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by 大舒 on 2017/8/10.
 */
public class RedisUtil {

    /**
     * IP
     */
    private static String address="redis.address";
    /**
     * 端口
     */
    private static String  port = "redis.port";
    /**
     * 密码
     */
    private static String auth= "redis.auth";
    /**
     * 连接实例的最大数目
     */
    private static String  maxTotal = "redis.maxTotal";
    /**
     * 最大空闲数
     */
    private static String maxIdle ="redis.maxIdle";

    /**
     * 最长等在时间
     */
    private static String maxWait= "redis.maxWait";
    /**
     * 超时时间
     */
    private static String timeOut="redis.timeOut";

    /**
     * 初始对象锁
     */
    private static ReentrantLock lock = new ReentrantLock();
    /**
     * 初始化redis连接池锁
     */
    private static ReentrantLock initLock = new ReentrantLock();
    private static JedisPool jedisPool = null;
    private static RedisUtil redisUtil = null;

    private RedisUtil() {
    }

    public static RedisUtil getInstance() {
        if (redisUtil == null) {
            lock.lock();
            if (redisUtil == null) {
                redisUtil = new RedisUtil();
            }
            lock.unlock();
        }
        return redisUtil;
    }

    /**
     * 初始化连接池
     */
    public void initJedisPool() {
        try {
            String path =Main.class.getClass().getResource("/").getPath()+"redis.properties";
            Properties pps = PropertiesUtil.getProperties(path);
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(Integer.parseInt(pps.getProperty(maxTotal)));
            config.setMaxIdle(Integer.parseInt(pps.getProperty(maxIdle)));
            config.setMaxWaitMillis(Long.parseLong(pps.getProperty(maxWait)));
            jedisPool = new JedisPool(config, pps.getProperty(address), Integer.parseInt(pps.getProperty(port)), Integer.parseInt(pps.getProperty(timeOut)), pps.getProperty(auth));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取连接
     * @return
     */
    public Jedis getJedis() {
        if (jedisPool == null) {
            initLock.lock();
            if (jedisPool == null) {
                initJedisPool();
            }
            initLock.unlock();
        }
        return jedisPool.getResource();
    }

    //------------------命令---------------------
    public Long zadd(String key, double score, String member) {
        Long rs = null;
        Jedis client = null;
        try {
            client= getJedis();
            rs = client.zadd(key, score, member);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            if(client != null){
                client.close();
            }
        }
        return rs;
    }

    public void removeZsetByScore(String key,long score){
        Jedis client = null;
        try {
            client= getJedis();
           client.zremrangeByScore(key,score,score);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            if(client != null){
                client.close();
            }
        }


    }

}
