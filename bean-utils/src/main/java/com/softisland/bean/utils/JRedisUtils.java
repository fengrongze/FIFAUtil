package com.softisland.bean.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

/**
 * Created by liwx on 15/11/9.
 */
public class JRedisUtils {
    private static final Logger logger = LoggerFactory.getLogger(JRedisUtils.class);


    /**
     * 缓存是否开启，默认开启,true开启，false不开启
     */
    private volatile boolean redisSwitch = true;

    private RedisTemplate<String,Object> redisTemplate;
    /**
     * 现在默认的template,key为字符串,value位字符串
     */
    private StringRedisTemplate stringRedisTemplate;
    
    /**
     * 构建RedisTemplate和StringRedisTemplate
     * @param redisProperties
     */
    public static JRedisUtils getInstance(RedisProperties redisProperties){
    	//RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration();
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisProperties.getPool().getMaxIdle());
        poolConfig.setMaxTotal(redisProperties.getPool().getMaxActive());
        poolConfig.setMinIdle(redisProperties.getPool().getMinIdle());
        poolConfig.setMaxWaitMillis(redisProperties.getPool().getMaxWait());
        JedisShardInfo info = new JedisShardInfo(redisProperties.getHost(),redisProperties.getPort());
        info.setPassword(redisProperties.getPassword());
        JedisConnectionFactory factory = new JedisConnectionFactory(poolConfig);
        factory.setShardInfo(info);
        factory.setDatabase(redisProperties.getDatabase());
        JRedisUtils redisUtils = new JRedisUtils(factory);
        return redisUtils;
    }
    /**
     * 构建RedisTemplate和StringRedisTemplate
     * @param factory
     */
    public JRedisUtils(RedisConnectionFactory factory){
        redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();
        stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        stringRedisTemplate.afterPropertiesSet();
    }
    public String redisDbInfo(){
    	JedisConnectionFactory factory = (JedisConnectionFactory)stringRedisTemplate.getConnectionFactory();
    	JedisShardInfo js = factory.getShardInfo();
    	String ret = "redis db info:===>>"+js.getHost()+":"+js.getPort()+" passwd:"+js.getPassword();
    	logger.debug(ret);
    	return ret;
    }
    /**
     * 根据key和MAP中的key获取MAP中的值
     * @param key
     * @param hashKey
     * @return
     * @throws Exception
     */
    public Object getHashValueByKey(String key,String hashKey)throws Exception{
        return stringRedisTemplate.opsForHash().get(key,hashKey);
    }

    /**
     * 用来测试玩的
     * @throws Exception
     */
    public void test()throws Exception{
        //redisTemplate.opsForHash().put("lwx","aa","bb");
        //Object object = redisTemplate.opsForHash().get("lwx","aa");
        //stringRedisTemplate.opsForHash().put("1","1","11");
        //stringRedisTemplate.opsForHash().put("1","2","22");
        addValue("111","111");
        addValue("111","222");
        System.out.println(getValue("111"));
    }

    /**
     * 通过KEY获取HashMap
     * @param key
     * @return
     * @throws Exception
     */
    public Map<Object,Object> getMapEntries(String key)throws Exception{
        return stringRedisTemplate.opsForHash().entries(key);
    }

    /**
     * 将值存入MAP中
     * @param key
     * @param hashKey
     * @param hasValue
     * @throws Exception
     */
    public void putValueToMap(String key,String hashKey,String hasValue)throws Exception{
        stringRedisTemplate.opsForHash().put(key,hashKey,hasValue);
    }
    /**
     * 将值存入MAP并判断Key是否存在 存在为false
     * @param key
     * @param hashKey
     * @param hasValue
     * @return
     * @throws Exception
     */
    public boolean putIfAbsentValueToMap(String key,String hashKey,String hasValue)throws Exception{
        return stringRedisTemplate.opsForHash().putIfAbsent(key, hashKey, hasValue);
    }


    /**
     * 设置临时key，毫秒
     * @param key
     * @param value
     * @param timeout
     * @throws Exception
     */
    public void setTempValue(String key,String value,long timeout)throws Exception{
         stringRedisTemplate.opsForValue().set(key,value,timeout, TimeUnit.MILLISECONDS);
    }


    /**
     *，当且仅当 key 不存在，不存在,set
     * @param key
     * @param value
     * @param timeout
     * @return 成功 = true;已经存在=false
     * @throws Exception
     */
    public boolean setIfAbsent(String key,String value,long timeout)throws Exception{
       return setIfAbsent(key,value,timeout,TimeUnit.MILLISECONDS);
    }


    /**
     * 原子check&set操作
     * @param redisKey
     * @param value
     * @param timeout
     * @param unit
     * @return
     */
    public boolean setIfAbsent(String redisKey,String value,long timeout, TimeUnit unit) {
        boolean result = false;
        if(redisSwitch){
            try {
                BoundValueOperations boundValueOperations = stringRedisTemplate.boundValueOps(redisKey);
                result = boundValueOperations.setIfAbsent(value);
                if(result){
                    boundValueOperations.expire(timeout, unit);
                }
            } catch (Exception e) {
                logger.error("setIfAbsent error:",e);
            }
        }
        return result;
    }


    /**
     * 重新设置某个KEY的值
     * @param key
     * @param value
     * @throws Exception
     */
    public void setValue(String key,String value)throws Exception{
        stringRedisTemplate.opsForValue().set(key,value);
    }


    /**
     * 添加值
     * @param key
     * @param value
     * @throws Exception
     */
    public void addValue(String key,String value)throws Exception{
        stringRedisTemplate.opsForValue().append(key,value);
    }

    /**
     * 添加值到LIST中
     * @param key
     * @param value
     * @throws Exception
     */
    public void addValueToList(String key,String value)throws Exception{
        stringRedisTemplate.boundListOps(key).leftPush(value);
    }
    /**
     * 
     * @param key
     * @param value
     * @return
     * @throws Exception
     */
    public Long addToList(String key,String value)throws Exception{
        return stringRedisTemplate.boundListOps(key).leftPush(value);
    }

    /**
     * 根据KEY从list中获取所有值
     * @param key
     * @return
     * @throws Exception
     */
    public List<String> getValuesFromList(String key)throws Exception{
        return stringRedisTemplate.boundListOps(key).range(0,-1);
    }
    
    /**
     * 把值添加到队列的尾部
     * @param key KEY
     * @param value 值
     * @return
     * @throws Exception
     */
    public void appendValueToList(String key, String value) throws Exception{
        stringRedisTemplate.boundListOps(key).rightPush(value);
    }
    
    /**
     * 移除队列头部的值
     * @param key KEY
     * @return
     * @throws Exception
     */
    public String lpopValueFromList(String key) throws Exception{
        return stringRedisTemplate.boundListOps(key).leftPop();
    }
    
    /**
     * 移除队列头部的值
     * @param key KEY
     * @return
     * @throws Exception
     */
    public String getValueFromList(String key, long index) throws Exception{
        return stringRedisTemplate.boundListOps(key).index(index);
    }

    /**
     * 通过key从LIST中获取指定范围的值
     * @param key
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    public List<String> getValuesFromList(String key,int start,int end)throws Exception{
        return stringRedisTemplate.boundListOps(key).range(start,end);
    }

    /**
     * 获取LIST的大小
     * @param key
     * @return
     * @throws Exception
     */
    public Long getListSize(String key)throws Exception{
        return stringRedisTemplate.boundListOps(key).size();
    }

    /**
     * 从redis中获取值
     * @param key
     * @return
     * @throws Exception
     */
    public String getValue(String key)throws Exception{
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 添加一组值
     * @param values
     * @throws Exception
     */
    public void addValues(Map<String,String> values)throws Exception{
        stringRedisTemplate.opsForValue().multiSet(values);
    }

    /**
     * 获取某个KEY的值的数量
     * @param key
     * @return
     * @throws Exception
     */
    public Long getSize(String key)throws Exception{
        return stringRedisTemplate.opsForValue().size(key);
    }

    /**
     * 删除某个KEY的所有值
     * @param key
     * @throws Exception
     */
    public void deleteValue(String key)throws Exception{
        stringRedisTemplate.delete(key);
    }

    /**
     *
     * @param keys
     * @throws Exception
     */
    public void deleteValues(List<String> keys)throws Exception{
        stringRedisTemplate.delete(keys);
    }

    /**
     * 根据KEY移除某个KEY下面的MAP中某几个HashKey的值
     * @param key
     * @param hashKeys
     * @throws Exception
     */
    public void removeMapValues(String key,String... hashKeys)throws Exception{
        stringRedisTemplate.opsForHash().delete(key,hashKeys);
    }

    /**
     * 获取hash的大小
     * @param key
     * @return
     * @throws Exception
     */
    public Long getHashSize(String key)throws Exception{
        return stringRedisTemplate.opsForHash().size(key);
    }

    /**
     * 对现有的KEY进行重命名
     * @param oldKey
     * @param newKey
     * @throws Exception
     */
    public void renameKey(String oldKey,String newKey)throws Exception{
        stringRedisTemplate.rename(oldKey, newKey);

    }

    /**
     * 检查MAP中是否包含已存在的值
     * @param key
     * @param hashKey
     * @return
     * @throws Exception
     */
    public boolean hasKeyInMap(String key,String hashKey)throws Exception{
        return stringRedisTemplate.opsForHash().hasKey(key,hashKey);
    }

    /**
     * 存HASHMAP
     * @param key
     * @param hashKey
     * @param value
     * @throws Exception
     */
    public void putHashmap(String key,String hashKey,Object value)throws Exception{
    	redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 设置超时时间
     * @param key
     * @param time
     * @param unit
     */
    public void setExpier(String key,Long time,TimeUnit unit){
    	redisTemplate.expire(key, time, unit);
    }
    /**
     * 检验key是否存在
     * @param key
     * @return
     */
    public boolean exsitKey(String key){
    	return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.exists(key.getBytes());
			}
		});
    }
    /**
     * 根据关键字得到所有的key列表
     * @param keys
     * @return
     */
    public Set<String> getKeysList(String keys){
    	return stringRedisTemplate.keys(keys+"*");
    }
    /**
     * 加入hash并唯一并设置超时
     * @param key
     * @param hashKey
     * @param value
     * @param time
     * @param unit
     * @return
     */
    public boolean hIfSetFieldTimeOut(String key,String hashKey,String value,Long time,TimeUnit unit){
    	boolean ret= false ;
    	BoundHashOperations<String, String, String> boundHashOperations = stringRedisTemplate.boundHashOps(key); 
    	ret=boundHashOperations.putIfAbsent(hashKey, value);
    	if(ret){
    		boundHashOperations.expire(time, unit);
    	}
    	return ret;
    }

    public static void main(String[] args)throws Exception {
        /*JedisShardInfo info = new JedisShardInfo("114.215.178.40",6379);
        info.setPassword("1qaz2WSX!@");*/
    	//正式环境
//        JedisShardInfo info = new JedisShardInfo("50.23.94.86",6379);
        
        JedisShardInfo info = new JedisShardInfo("http://172.16.14.86:6379/4");
//        info.setPassword("123456");
        RedisConnectionFactory factory = new JedisConnectionFactory(info);
        JRedisUtils jRedisUtils = new JRedisUtils(factory);
        String supplierId="";
        String taskId="414";
        try {
			if(StringUtils.isNoneBlank(supplierId)){
				jRedisUtils.deleteValue(String.format("SUPPLIER_%s:%s", taskId,supplierId));
			}else{
				Set<String> keys=jRedisUtils.getKeysList(String.format("SUPPLIER_%s:%s", taskId,""));
				keys.forEach(v->{
					try {
						jRedisUtils.deleteValue(v);
//						System.out.println(v);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
 
        
         
      
//        utils.addToList("", "")
        jRedisUtils.test();
    }
}
