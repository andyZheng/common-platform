/*
 * 文件名：RedisService.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved. 
 * 描述： 游戏数据采集平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * 功能描述：<code>RedisService</code>接口是Redis Java客户端远程操作API，用于定义常用操作接口。
 * <p>
 *
 * @author   andy.zheng  andy.zheng0807@gmail.com
 * @version  1.0, 2014年4月29日 下午2:46:49
 * @since    Common-Platform/ 1.0
 */
public interface RedisService {
    
    /**
     * 功能描述：获取Redis实例。
     *
     * @return  Redis实例。
     */
    public Jedis getJedis();
    
    /**
     * 功能描述：以某个key为键缓存指定字符串值。
     * 
     * @see redis.clients.jedis.Jedis#set(String, String)
     * @param key    待缓存的键。
     * @param value  待缓存的值。
     */
    public void put(String key, String value);
    
    /**
     * 功能描述：以某个key值为键缓存某个指定字符串并设置过期时间。单位为秒。
     *
     * @see  redis.clients.jedis.Jedis#setex(String, int, String)
     * @param key               待缓存的键。
     * @param value             待缓存的值。
     * @param expiredSeconds    过期时间。
     */
    public void put(String key, String value, int expiredSeconds);
    
    /**
     * 功能描述：根据指定key获取缓存的值。
     *
     * @see  redis.clients.jedis.Jedis#get(String)
     * @param key   待查询的键。
     * @return      查询到的缓存值。
     */
    public String get(String key);
    
    /**
     * 功能描述：获取指定key存储的值并设置新值。
     *
     * @see redis.clients.jedis.Jedis#getSet(String, String)
     * @param key   待查询key.
     * @param value 待设置的值。
     * @return      上次存储的值。
     */
    public String getPut(String key, String value);
    
    /**
     * 功能描述：以某个key存储累计的值。
     *
     * @see   redis.clients.jedis.Jedis#incrBy(byte[], long)
     * @param key   待存储的键。 
     * @param step  递增的值。
     */
    public void increment(String key, long step);
    
    /**
     * 功能描述：以某个key存储递减值。
     *
     * @see   redis.clients.jedis.Jedis#decrBy(String, long)
     * @param key   待存储的键。
     * @param step  递减的值。
     */
    public void decrement(String key, long step);
    
    /**
     * 功能描述：以某个key为键存储指定的字符串列表。<p>
     * <b>注：从左至右依次存储。</b>
     *
     * @see redis.clients.jedis.Jedis#lpush(String, String...)
     * @param key   待存储键。
     * @param list  待存储的值列表。
     */
    public void listLeftPush(String key, String... list);
    
    /**
     * 功能描述：获取指定key所存储列表中的第一个值。<p>
     *
     * @see redis.clients.jedis.Jedis#lpop(String)
     * @param key   待查询键。
     * @param list  待查询的值列表。
     */
    public String listFirstPop(String key);
    
    /**
     * 功能描述：获取指定key所存储列表中指定位置存储的值。
     *
     * @see   redis.clients.jedis.Jedis#lindex(String, long)
     * @param key   待查询的键。
     * @param index 待查询的索引。
     * @return      查询到的值。
     */
    public String listGetIndex(String key, int index);
    
    /**
     * 功能描述：获取指定key存储的值列表。
     *
     * @see   redis.clients.jedis.Jedis#lrange(String, long, long)
     * @param key   待查询的键。
     * @return      指定key存储的值列表。
     */
    public List<String> listGetAll(String key);
    
    /**
     * 功能描述：以某个key为键存储指定的字符串列表。<p>
     * <b>注：从列表尾部开始存储。</b>
     *
     * @see redis.clients.jedis.Jedis#rpush(String, String...)
     * @param key   待存储键。
     * @param list  待存储的值列表。
     */
    public void listRightPush(String key, String... list);
    
    /**
     * 功能描述：获取指定key所存储列表中的最后一个值。<p>
     *
     * @see redis.clients.jedis.Jedis#rpop(String)
     * @param key   待查询键。
     * @param list  待查询的值列表。
     */
    public String listLastPop(String key);
    
    /**
     * 功能描述：以某个key为键存储Map结构的值。
     *
     * @see   redis.clients.jedis.Jedis#hset(String, String, String)
     * @param key       待存储的key。
     * @param propery   待存储的属性。
     * @param value     待存储的值。
     */
    public void mapSet(String key, String property, String value);
    
    /**
     * 功能描述：以某个key为键存储Map结构的值。
     *
     * @see   redis.clients.jedis.Jedis#hmset(String, Map)
     * @param key       待存储的key。
     * @param values    待存储的集合。
     */
    public void mapSet(String key, Map<String, String> values);
    
    /**
     * 功能描述：获取指定key指定属性所存储的值。
     *
     * @see   redis.clients.jedis.Jedis#hget(String, String)
     * @param key      待查询的键。
     * @param property 待查询的属性。
     * @return         查询到的值。
     */
    public String mapGet(String key, String property);
    
    /**
     * 功能描述：获取指定key存储的Map结构值。
     *
     * @see   redis.clients.jedis.Jedis#hgetAll(String)
     * @param key   待查询的键。
     * @return      查询到的Map结构值。
     */
    public Map<String, String> mapGetAll(String key);
    
    /**
     * 功能描述：删除指定key存储的Map结构数据。
     *
     * @see   redis.clients.jedis.Jedis#hdel(String, String...)
     * @param key   待删除key.
     */
    public void mapRemove(String key, String... values);
    
    /**
     * 
     * 功能描述：以某个key为键存储Set结构的值。
     *
     * @see   redis.clients.jedis.Jedis#sadd(String, String...)
     * @param key    待存储的键。
     * @param values 待存储的值。
     */
    public void setAdd(String key, String... values);
    
    /**
     * 功能描述：获取指定key存储的Set集合。
     *
     * @see   redis.clients.jedis.Jedis#smembers(String)
     * @param key   待查询的键。
     * @return      查询到的Set集合。
     */
    public Set<String> setGetAll(String key);
    
    /**
     * 功能描述：删除指定key存储的值。
     *
     * @see   redis.clients.jedis.Jedis#srem(String, String...)
     * @param key     待删除的key.
     * @param values  待删除的值。  
     */
    public void setRemove(String key, String... values);
    
    /**
     * 功能描述：删除指定key列表的存储的值。
     *
     * @see   redis.clients.jedis.Jedis#del(String...)
     * @param keys 待删除key列表。
     */
    public void remove(String... keys);
    
    /**
     * 功能描述：发布消息到指定队列。<p>
     * 发布者
     *
     * @see   redis.clients.jedis.Jedis#publish(String, String)
     * @param channel   待发布的渠道。
     * @param messages  发布的消息。
     */
    public void publish(String channel, String messages);
    
    /**
     * 功能描述：监听指定渠道发布的消息。<p>
     * 订阅者
     *
     * @see   redis.clients.jedis.Jedis#subscribe(redis.clients.jedis.JedisPubSub, String...)
     * @param channel   订阅的消息渠道。
     * @param listener  消息监听器。
     */
    public void subscribe(String channel, JedisPubSub listener);
    
}