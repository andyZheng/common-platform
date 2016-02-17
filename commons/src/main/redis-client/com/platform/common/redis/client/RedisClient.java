package com.platform.common.redis.client;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;

import com.platform.common.redis.RedisService;

/**
 * 功能描述：<code>RedisClient</code>类是基于Redis Java客户端API实现的远程访问接口，用户可基于Redis存取相关信息。
 * <p>
 *
 * @author andy.zheng andy.zheng0807@gmail.com
 * @version 1.0, 2014年4月29日 下午5:31:37
 * @since Common-Platform/Redis Client 1.0
 */
@Component
public class RedisClient implements RedisService {
    /** 日志对象 */
    private static Logger logger = LoggerFactory.getLogger(RedisService.class);

    /** Redis java客户端连接池对象 */
    @Autowired
    private JedisPool jedisPool;

    /*
     * @see com.platform.common.redis.RedisService#getJedis()
     */
    @Override
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    /*
     * @see com.platform.common.redis.RedisService#put(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void put(String key, String value) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            jedis.set(key, value);
        } catch (JedisException e) {
            logger.error("Set value fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    /*
     * @see com.platform.common.redis.RedisService#put(java.lang.String,
     * java.lang.String, int)
     */
    @Override
    public void put(String key, String value, int expiredSeconds) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            jedis.setex(key, expiredSeconds, value);
        } catch (JedisException e) {
            logger.error("Set value fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    /*
     * @see com.platform.common.redis.RedisService#get(java.lang.String)
     */
    @Override
    public String get(String key) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            return jedis.get(key);
        } catch (JedisException e) {
            logger.error("get value fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }

        return null;
    }

    /*
     * @see com.platform.common.redis.RedisService#getPut(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String getPut(String key, String value) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            return jedis.getSet(key, value);
        } catch (JedisException e) {
            logger.error("get value fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }

        return null;
    }

    /*
     * @see com.platform.common.redis.RedisService#increment(java.lang.String,
     * long)
     */
    @Override
    public void increment(String key, long step) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            jedis.incrBy(key, step);
        } catch (JedisException e) {
            logger.error("Increment value fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    /*
     * @see com.platform.common.redis.RedisService#decrement(java.lang.String,
     * long)
     */
    @Override
    public void decrement(String key, long step) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            jedis.decrBy(key, step);
        } catch (JedisException e) {
            logger.error("Decrement value fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    /*
     * @see
     * com.platform.common.redis.RedisService#listLeftPush(java.lang.String,
     * java.lang.String[])
     */
    @Override
    public void listLeftPush(String key, String... list) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            jedis.lpush(key, list);
        } catch (JedisException e) {
            logger.error("Set left list fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    /*
     * @see
     * com.platform.common.redis.RedisService#listFirstPop(java.lang.String)
     */
    @Override
    public String listFirstPop(String key) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            return jedis.lpop(key);
        } catch (JedisException e) {
            logger.error("Get first value fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }

        return null;
    }

    /*
     * @see
     * com.platform.common.redis.RedisService#listGetIndex(java.lang.String,
     * int)
     */
    @Override
    public String listGetIndex(String key, int index) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            return jedis.lindex(key, index);
        } catch (JedisException e) {
            logger.error("get value fail by index!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }

        return null;
    }

    /*
     * @see com.platform.common.redis.RedisService#listGetAll(java.lang.String)
     */
    @Override
    public List<String> listGetAll(String key) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            return jedis.lrange(key, 0, -1);
        } catch (JedisException e) {
            logger.error("Get all value fail by key!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }

        return null;
    }

    /*
     * @see
     * com.platform.common.redis.RedisService#listRightPush(java.lang.String,
     * java.lang.String[])
     */
    @Override
    public void listRightPush(String key, String... list) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            jedis.rpush(key, list);
        } catch (JedisException e) {
            logger.error("Set list fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    /*
     * @see com.platform.common.redis.RedisService#listLastPop(java.lang.String)
     */
    @Override
    public String listLastPop(String key) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            return jedis.rpop(key);
        } catch (JedisException e) {
            logger.error("Increment value fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }

        return null;
    }

    /*
     * @see com.platform.common.redis.RedisService#mapSet(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public void mapSet(String key, String property, String value) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            jedis.hset(key, property, value);
        } catch (JedisException e) {
            logger.error("Set map fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    /*
     * @see com.platform.common.redis.RedisService#mapSet(java.lang.String,
     * java.util.Map)
     */
    @Override
    public void mapSet(String key, Map<String, String> values) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            jedis.hmset(key, values);
        } catch (JedisException e) {
            logger.error("Subscribe message fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    /*
     * @see com.platform.common.redis.RedisService#mapGet(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String mapGet(String key, String property) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            return jedis.hget(key, property);
        } catch (JedisException e) {
            logger.error("Get map fail by key and field!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }

        return null;
    }

    /*
     * @see com.platform.common.redis.RedisService#mapGetAll(java.lang.String)
     */
    @Override
    public Map<String, String> mapGetAll(String key) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            return jedis.hgetAll(key);
        } catch (JedisException e) {
            logger.error("Get map fail by key!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }

        return null;
    }

    /*
     * @see com.platform.common.redis.RedisService#mapRemove(java.lang.String[])
     */
    @Override
    public void mapRemove(String key, String... values) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            jedis.hdel(key, values);
        } catch (JedisException e) {
            logger.error("Remove map fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    /*
     * @see com.platform.common.redis.RedisService#setAdd(java.lang.String,
     * java.lang.String[])
     */
    @Override
    public void setAdd(String key, String... values) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            jedis.sadd(key, values);
        } catch (JedisException e) {
            logger.error("Add set fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    /*
     * @see com.platform.common.redis.RedisService#setGetAll(java.lang.String)
     */
    @Override
    public Set<String> setGetAll(String key) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            return jedis.smembers(key);
        } catch (JedisException e) {
            logger.error("Get all set fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }

        return null;
    }

    /*
     * @see com.platform.common.redis.RedisService#setRemove(java.lang.String,
     * java.lang.String[])
     */
    @Override
    public void setRemove(String key, String... values) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            jedis.srem(key, values);
        } catch (JedisException e) {
            logger.error("Remove set fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    /*
     * @see com.platform.common.redis.RedisService#remove(java.lang.String[])
     */
    @Override
    public void remove(String... keys) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            jedis.del(keys);
        } catch (JedisException e) {
            logger.error("Delete fail by keys!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    /*
     * @see com.platform.common.redis.RedisService#publish(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void publish(String channel, String message) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            jedis.publish(channel, message);
        } catch (JedisException e) {
            logger.error("Publish message fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    /*
     * @see com.platform.common.redis.RedisService#subscribe(java.lang.String,
     * redis.clients.jedis.JedisPubSub)
     */
    @Override
    public void subscribe(String channel, JedisPubSub listener) {
        Jedis jedis = null;

        try {
            jedis = this.getJedis();
            jedis.subscribe(listener, channel);
        } catch (JedisException e) {
            logger.error("Subscribe message fail!", e);
            if (null != jedis) {
                this.jedisPool.returnBrokenResource(jedis);
            }
        } finally {
            if (null != jedis) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public final static String host = "192.168.6.55";
    public final static int port = 6379;

    // TODO TESTING
    public static void main(String[] args) {
        // 对象池化测试
        System.out.println("=============== 对象池化测试 ===============");
        testPoolRedis(host, port);

        // 批量写入测试
        System.out.println("=============== 批量写入测试 ===============");
        testBatchRedis(host, port);

        // 并发测试
        System.out.println("=============== 并发测试 ===============");
        testConcurrentRedis(host, port);
    }

    /**
     * 功能描述：测试批量发送性能。
     *
     * @see 类、类#方法、类#成员
     */
    public static void testBatchRedis(String host, int port) {
        RedisClient client = new RedisClient();
        client.setJedisPool(new JedisPool(new JedisPoolConfig(), host, port));

        int count = 100 * 10000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            client.mapSet("key0", "count" + i, "value");
        }

        double time = (System.currentTimeMillis() - start) / 1000.0;
        System.out.format("【Redis逐条发送数据】：发送%s条,共%s秒。平均每秒%s条。\n", count, time, count / time);

        start = System.currentTimeMillis();
        Jedis jedis = client.getJedis();
        Pipeline pipelined = jedis.pipelined();
        for (int i = 0; i < count; i++) {
            pipelined.hset("key1", "field" + i, "value");
        }
        pipelined.sync();
        time = (System.currentTimeMillis() - start) / 1000.0;
        System.out.format("【Redis批量发送数据】：发送%s条,共%s秒。平均每秒%s条。\n", count, time, count / time);
    }

    /**
     * 
     * 功能描述：测试Redis对象池化性能。
     *
     * @see 类、类#方法、类#成员
     */
    public static void testPoolRedis(String host, int port) {
        int count = 30 * 10000;
        long start = System.currentTimeMillis();

        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Jedis jedis = new Jedis(host, port);
            jedis.hset("key1", "field" + i, "value");
            jedis.disconnect();
        }
        double time = (System.currentTimeMillis() - start) / 1000.0;
        System.out.format("【Redis单实例发送数据】：共发送%s条,共%s秒。平均每秒%s条。\n", count, time, count / time);

        RedisClient client = new RedisClient();
        client.setJedisPool(new JedisPool(new JedisPoolConfig(), host, port));
        for (int i = 0; i < count; i++) {
            client.mapSet("key0", "count" + i, "value");
        }
        time = (System.currentTimeMillis() - start) / 1000.0;
        System.out.format("【Redis对象池化发送数据】：共发送%s条,共%s秒。平均每秒%s条。\n", count, time, count / time);
    }

    /**
     * 功能描述：测试Redis并发性能。
     *
     * @see 类、类#方法、类#成员
     */
    public static void testConcurrentRedis(final String host, final int port) {
        final int count = 10000;
        final int times = 1 * 100;
        final int stat = 10000;

        final RedisClient client = new RedisClient();
        client.setJedisPool(new JedisPool(new JedisPoolConfig(), host, port));

        /*
         * String dbName = "log_game_event"; MongoDatabase mongoDB = null;
         * MongoClient mongo = null; try { mongo = new
         * MongoClient("192.168.6.101", 27017); mongoDB =
         * mongo.getDatabase(dbName); } catch (Exception e) {
         * e.printStackTrace(); if (null != mongo) { mongo.close(); } }
         * 
         * final MongoDatabase db = mongoDB;
         */

        new Thread(new Runnable() {

            @Override
            public void run() {
                client.subscribe("channelv", new JedisPubSub() {
                    int counter = 0;
                    long startTime;
                    long prevTime;

                    @Override
                    public void onMessage(String channel, String message) {
                        String logCollection = "logs";
                        // MongoCollection<Document> collection =
                        // db.getCollection(logCollection);

                        try {
                            /*
                             * Document data = new Document(channel, message);
                             * if (counter == 0) { startTime =
                             * System.currentTimeMillis(); prevTime = startTime;
                             * }
                             * 
                             * collection.insertOne(data);
                             */

                            counter++;

                            if (counter % stat == 0) {
                                double time = (System.currentTimeMillis() - prevTime) / 1000.0;
                                System.err.format("【并发测试】：写入%s条,花费时间：%s毫秒,平均%s条/秒\n", stat, time, stat / time);
                                prevTime = System.currentTimeMillis();
                            }

                            if (counter == count * times) {
                                double time = (System.currentTimeMillis() - startTime) / 1000.0;
                                System.err.println("\n=============================");
                                System.err.format("【%s并发测试】：本次测试写入%s条,花费时间：%s秒,平均%s条/秒\n", count, counter, time,
                                        counter / time);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onPMessage(String pattern, String channel, String message) {
                    }

                    @Override
                    public void onSubscribe(String channel, int subscribedChannels) {
                    }

                    @Override
                    public void onUnsubscribe(String channel, int subscribedChannels) {
                    }

                    @Override
                    public void onPUnsubscribe(String pattern, int subscribedChannels) {
                    }

                    @Override
                    public void onPSubscribe(String pattern, int subscribedChannels) {
                    }
                });
            }
        }).start();

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final CountDownLatch maxDownLatch = new CountDownLatch(count);

        for (int i = 0; i < count; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                        int k = 0;
                        while (k < times) {
                            Jedis jedis = new Jedis(host, port, 180000);
                            jedis.publish("channelv", System.nanoTime() + "");
                            jedis.disconnect();
                            k++;
                        }
                        maxDownLatch.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }).start();
        }

        long start = System.currentTimeMillis();
        countDownLatch.countDown();

        try {
            maxDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double end = (System.currentTimeMillis() - start) / 1000.0;
        long totalNumbers = count * times;
        System.out.format("【%s并发测试】：本次测试共发送%s条,花费时间：%s秒,平均%s条/秒\n", count, totalNumbers, end, totalNumbers / end);
    }
}
