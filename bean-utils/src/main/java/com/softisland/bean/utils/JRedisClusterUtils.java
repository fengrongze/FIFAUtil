package com.softisland.bean.utils;

import com.softisland.bean.bean.RedisClusterProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

import java.util.*;

/**
 * redis集群帮助类
 * 
 * 配置：spring.rediscluster 配置集群中的一个节点的ip与端口
 * 在启动类里增加JRedisClusterUtils.getInstance(new RedisClusterProperties());
 * 
 * @author wangj@2015.12.18
 */
public class JRedisClusterUtils {
    private static final Logger logger = LoggerFactory.getLogger(JRedisClusterUtils.class);
    private JedisCluster jedisCluster;
    
    
    private JRedisClusterUtils(){}
    
    /**
     * 通过redisProperties的sentinel的nodes构建集群
     * @param redisClusterProperties
     */
    public JRedisClusterUtils(RedisClusterProperties redisClusterProperties){
		JRedisClusterUtils jredisClusterUtils = new JRedisClusterUtils();
    	GenericObjectPoolConfig pc = new GenericObjectPoolConfig();
    	if(null != redisClusterProperties.getPool()){
        	pc.setMaxIdle(redisClusterProperties.getPool().getMaxIdle());
        	pc.setMinIdle(redisClusterProperties.getPool().getMinIdle());
        	pc.setMaxTotal(redisClusterProperties.getPool().getMaxActive());
        	pc.setMaxWaitMillis(redisClusterProperties.getPool().getMaxWait());
    	}
    	if(StringUtils.isNotBlank(redisClusterProperties.getHost())){
            String nodes = redisClusterProperties.getHost()+":"+redisClusterProperties.getPort();
            List<HostAndPort> hps = jredisClusterUtils.parseAddHosts(nodes);
            jedisCluster = new JedisCluster(new HashSet<HostAndPort>(hps),pc);
    	}
    }

    /**
     * 添加集群节点配置
     * @param envHosts	：	一次可以配置一个或者多个，例如127.0.0.1:8000,127.0.0.1:8001
     */
    public List<HostAndPort> parseAddHosts(String envHosts){
    	return parseAddHosts( envHosts,  new ArrayList<>());
    }
     
    /**
     * 打印本集群节点
     * @return
     */
    public String printClusterInfo(){
    	StringBuilder ret = new StringBuilder("");
    	Client cl = null;
    	for(String v : jedisCluster.getClusterNodes().keySet()){
    		cl = jedisCluster.getClusterNodes().get(v).getResource().getClient();
    		ret.append(cl.getHost()).append(":").append(cl.getPort()).append("\n");
    	}
    	logger.debug("\n----print redis ClusterInfo: \n"+ret.substring(0));
    	return ret.substring(0);
    }

	/**
	 * 根据key和MAP中的key获取MAP中的值
	 * @param key
	 * @param hashKey
	 * @return
	 * @throws Exception
	 */
	public Object getHashValueByKey(String key,String hashKey)throws Exception{
		return jedisCluster.hget(key,hashKey);
	}

	/**
	 * 用来测试玩的
	 * @throws Exception
	 */
	public void test()throws Exception{
		this.putValueToMap("lwx","aa","bb");
		Object object = this.hget("lwx","aa");
		this.putValueToMap("1","1","11");
		this.putValueToMap("1","2","22");
//		String object = (String)this.hget("1","1");
		String object1 = (String)this.hget("1","2");
		System.out.println(object);
		System.out.println(object1);
	}

	/**
	 * 通过KEY获取HashMap
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getMapEntries(String key)throws Exception{
		return jedisCluster.hgetAll(key);
	}

	/**
	 * 将值存入MAP中
	 * @param key
	 * @param hashKey
	 * @param hasValue
	 * @throws Exception
	 */
	public void putValueToMap(String key,String hashKey,String hasValue)throws Exception{
        jedisCluster.hset(key,hashKey,hasValue);
	}

	/**
	 * 重新设置某个KEY的值
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public void setValue(String key,String value)throws Exception{
        jedisCluster.set(key,value);
	}

	/**
	 * 添加值
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public void addValue(String key,String value)throws Exception{
        jedisCluster.sadd(key,value);
	}

	/**
	 * 添加一组值
	 * @param values
	 * @throws Exception
	 */
	public void addValues(Map<String,String> values)throws Exception{
        values.keySet().forEach(v->{jedisCluster.sadd(v,values.get(v));});
	}

	/**
	 * 获取某个KEY的值的数量
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Long getSize(String key)throws Exception{
		return (long)jedisCluster.hgetAll(key).size();
	}

	/**
	 * 删除某个KEY的所有值
	 * @param key
	 * @throws Exception
	 */
	public void deleteValue(String key)throws Exception{
        jedisCluster.del(key);
	}

	/**
	 *
	 * @param keys
	 * @throws Exception
	 */
	public void deleteValues(List<String> keys)throws Exception{
        jedisCluster.del((String[])keys.toArray());
	}

	/**
	 * 根据KEY移除某个KEY下面的MAP中某几个HashKey的值
	 * @param key
	 * @param hashKeys
	 * @throws Exception
	 */
	public void removeMapValues(String key,String... hashKeys)throws Exception{
        jedisCluster.hdel(key,hashKeys);
	}

	/**
	 * 对现有的KEY进行重命名
	 * @param oldKey
	 * @param newKey
	 * @throws Exception
	 */
	public void renameKey(String oldKey,String newKey)throws Exception{
        jedisCluster.rename(oldKey,newKey);
	}
	/**
	 * 检查MAP中是否包含已存在的值
	 * @param key
	 * @param hashKey
	 * @return
	 * @throws Exception
	 */
	public boolean hasKeyInMap(String key,String hashKey)throws Exception{
		return jedisCluster.hexists(key,hashKey);
	}


	public String set(final String key, final String value) {
		return jedisCluster.set(key, value);
	}
	public String set(final String key, final String value, final String nxxx, final String expx,
					  final long time) {
		return jedisCluster.set(key, value, nxxx, expx, time);
	}
	public String get(final String key) {
		return jedisCluster.get(key);
	}
	public Boolean exists(final String key) {
		return jedisCluster.exists(key);
	}
	public Long exists(final String... keys) {
		return jedisCluster.exists(keys);
	}
	public Long persist(final String key) {
		return jedisCluster.persist(key);
	}
	public String type(final String key) {
		return jedisCluster.type(key);
	}
	public Long expire(final String key, final int seconds) {
		return jedisCluster.expire(key, seconds);
	}
	public Long pexpire(final String key, final long milliseconds) {
		return jedisCluster.pexpire(key, milliseconds);
	}
	public Long expireAt(final String key, final long unixTime) {
		return jedisCluster.expireAt(key, unixTime);
	}
	public Long pexpireAt(final String key, final long millisecondsTimestamp) {
		return jedisCluster.pexpireAt(key, millisecondsTimestamp);
	}
	public Long ttl(final String key) {
		return jedisCluster.ttl(key);
	}
	public Long pttl(final String key) {
		return jedisCluster.pttl(key);
	}
	public Boolean setbit(final String key, final long offset, final boolean value) {
		return jedisCluster.setbit(key, offset, value);
	}
	public Boolean setbit(final String key, final long offset, final String value) {
		return jedisCluster.setbit(key, offset, value);
	}
	public Boolean getbit(final String key, final long offset) {
		return jedisCluster.getbit(key, offset);
	}
	public Long setrange(final String key, final long offset, final String value) {
		return jedisCluster.setrange(key, offset, value);
	}
	public String getrange(final String key, final long startOffset, final long endOffset) {
		return jedisCluster.getrange(key, startOffset, endOffset);
	}
	public String getSet(final String key, final String value) {
		return jedisCluster.getSet(key, value);
	}
	public Long setnx(final String key, final String value) {
		return jedisCluster.setnx(key, value);
	}
	public String setex(final String key, final int seconds, final String value) {
		return jedisCluster.setex(key, seconds, value);
	}
	public String psetex(final String key, final long milliseconds, final String value) {
		return jedisCluster.psetex(key, milliseconds, value);
	}
	public Long decrBy(final String key, final long integer) {
		return jedisCluster.decrBy(key, integer);
	}
	public Long decr(final String key) {
		return jedisCluster.decr(key);
	}
	public Long incrBy(final String key, final long integer) {
		return jedisCluster.incrBy(key, integer);
	}
	public Double incrByFloat(final String key, final double value) {
		return jedisCluster.incrByFloat(key, value);
	}
	public Long incr(final String key) {
		return jedisCluster.incr(key);
	}
	public Long append(final String key, final String value) {
		return jedisCluster.append(key, value);
	}
	public String substr(final String key, final int start, final int end) {
		return jedisCluster.substr(key, start, end);
	}
	public Long hset(final String key, final String field, final String value) {
		return jedisCluster.hset(key, field, value);
	}
	public String hget(final String key, final String field) {
		return jedisCluster.hget(key, field);
	}
	public Long hsetnx(final String key, final String field, final String value) {
		return jedisCluster.hsetnx(key, field, value);
	}
	public String hmset(final String key, final Map<String, String> hash) {
		return jedisCluster.hmset(key, hash);
	}
	public List<String> hmget(final String key, final String... fields) {
		return jedisCluster.hmget(key, fields);
	}
	public Long hincrBy(final String key, final String field, final long value) {
		return jedisCluster.hincrBy(key, field, value);
	}
	public Double hincrByFloat(final String key, final String field, final double value) {
		return jedisCluster.hincrByFloat(key, field, value);
	}
	public Boolean hexists(final String key, final String field) {
		return jedisCluster.hexists(key, field);
	}
	public Long hdel(final String key, final String... field) {
		return jedisCluster.hdel(key, field);
	}
	public Long hlen(final String key) {
		return jedisCluster.hlen(key);
	}
	public Set<String> hkeys(final String key) {
		return jedisCluster.hkeys(key);
	}
	public List<String> hvals(final String key) {
		return jedisCluster.hvals(key);
	}
	public Map<String, String> hgetAll(final String key) {
		return jedisCluster.hgetAll(key);
	}
	public Long rpush(final String key, final String... string) {
		return jedisCluster.rpush(key, string);
	}
	public Long lpush(final String key, final String... string) {
		return jedisCluster.lpush(key, string);
	}
	public Long llen(final String key) {
		return jedisCluster.llen(key);
	}
	public List<String> lrange(final String key, final long start, final long end) {
		return jedisCluster.lrange(key, start, end);
	}
	public String ltrim(final String key, final long start, final long end) {
		return jedisCluster.ltrim(key, start, end);
	}
	public String lindex(final String key, final long index) {
		return jedisCluster.lindex(key, index);
	}
	public String lset(final String key, final long index, final String value) {
		return jedisCluster.lset(key, index, value);
	}
	public Long lrem(final String key, final long count, final String value) {
		return jedisCluster.lrem(key, count, value);
	}
	public String lpop(final String key) {
		return jedisCluster.lpop(key);
	}
	public String rpop(final String key) {
		return jedisCluster.rpop(key);
	}
	public Long sadd(final String key, final String... member) {
		return jedisCluster.sadd(key, member);
	}
	public Set<String> smembers(final String key) {
		return jedisCluster.smembers(key);
	}
	public Long srem(final String key, final String... member) {
		return jedisCluster.srem(key, member);
	}
	public String spop(final String key) {
		return jedisCluster.spop(key);
	}
	public Set<String> spop(final String key, final long count) {
		return jedisCluster.spop(key, count);
	}
	public Long scard(final String key) {
		return jedisCluster.scard(key);
	}
	public Boolean sismember(final String key, final String member) {
		return jedisCluster.sismember(key, member);
	}
	public String srandmember(final String key) {
		return jedisCluster.srandmember(key);
	}
	public List<String> srandmember(final String key, final int count) {
		return jedisCluster.srandmember(key, count);
	}
	public Long strlen(final String key) {
		return jedisCluster.strlen(key);
	}
	public Long zadd(final String key, final double score, final String member) {
		return jedisCluster.zadd(key, score, member);
	}
	public Long zadd(final String key, final double score, final String member, final ZAddParams params) {
		return jedisCluster.zadd(key, score, member, params);
	}
	public Long zadd(final String key, final Map<String, Double> scoreMembers) {
		return jedisCluster.zadd(key, scoreMembers);
	}
	public Long zadd(final String key, final Map<String, Double> scoreMembers, final ZAddParams params) {
		return jedisCluster.zadd(key, scoreMembers, params);
	}
	public Set<String> zrange(final String key, final long start, final long end) {
		return jedisCluster.zrange(key, start, end);
	}
	public Long zrem(final String key, final String... member) {
		return jedisCluster.zrem(key, member);
	}
	public Double zincrby(final String key, final double score, final String member) {
		return jedisCluster.zincrby(key, score, member);
	}
	public Double zincrby(final String key, final double score, final String member, final ZIncrByParams params) {
		return jedisCluster.zincrby(key, score, member, params);
	}
	public Long zrank(final String key, final String member) {
		return jedisCluster.zrank(key, member);
	}
	public Long zrevrank(final String key, final String member) {
		return jedisCluster.zrevrank(key, member);
	}
	public Set<String> zrevrange(final String key, final long start, final long end) {
		return jedisCluster.zrevrange(key, start, end);
	}
	public Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
		return jedisCluster.zrangeWithScores(key, start, end);
	}
	public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long end) {
		return jedisCluster.zrevrangeWithScores(key, start, end);
	}
	public Long zcard(final String key) {
		return jedisCluster.zcard(key);
	}
	public Double zscore(final String key, final String member) {
		return jedisCluster.zscore(key, member);
	}
	public List<String> sort(final String key) {
		return jedisCluster.sort(key);
	}
	public List<String> sort(final String key, final SortingParams sortingParameters) {
		return jedisCluster.sort(key, sortingParameters);
	}
	public Long zcount(final String key, final double min, final double max) {
		return jedisCluster.zcount(key, min, max);
	}
	public Long zcount(final String key, final String min, final String max) {
		return jedisCluster.zcount(key, min, max);
	}
	public Set<String> zrangeByScore(final String key, final double min, final double max) {
		return jedisCluster.zrangeByScore(key, min, max);
	}
	public Set<String> zrangeByScore(final String key, final String min, final String max) {
		return jedisCluster.zrangeByScore(key, min, max);
	}
	public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
		return jedisCluster.zrevrangeByScore(key, max, min);
	}
	public Set<String> zrangeByScore(final String key, final double min, final double max,
									 final int offset, final int count) {
		return jedisCluster.zrangeByScore(key, min, max, offset, count);
	}
	public Set<String> zrevrangeByScore(final String key, final String max, final String min) {
		return jedisCluster.zrevrangeByScore(key, max, min);
	}
	public Set<String> zrangeByScore(final String key, final String min, final String max,
									 final int offset, final int count) {
		return jedisCluster.zrangeByScore(key, min, max, offset, count);
	}
	public Set<String> zrevrangeByScore(final String key, final double max, final double min,
										final int offset, final int count) {
		return jedisCluster.zrevrangeByScore(key, max, min, offset, count);
	}
	public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
		return jedisCluster.zrangeByScoreWithScores(key, min, max);
	}
	public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min) {
		return jedisCluster.zrevrangeByScoreWithScores(key, max, min);
	}
	public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max,
											  final int offset, final int count) {
		return jedisCluster.zrangeByScoreWithScores(key, min, max, offset, count);
	}
	public Set<String> zrevrangeByScore(final String key, final String max, final String min,
										final int offset, final int count) {
		return jedisCluster.zrevrangeByScore(key, max, min, offset, count);
	}
	public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max) {
		return jedisCluster.zrangeByScoreWithScores(key, min, max);
	}
	public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min) {
		return jedisCluster.zrevrangeByScoreWithScores(key, max, min);
	}
	public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max,
											  final int offset, final int count) {
		return jedisCluster.zrangeByScoreWithScores(key, min, max, offset, count);
	}
	public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max,
												 final double min, final int offset, final int count) {
		return jedisCluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}
	public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max,
												 final String min, final int offset, final int count) {
		return jedisCluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}
	public Long zremrangeByRank(final String key, final long start, final long end) {
		return jedisCluster.zremrangeByRank(key, start, end);
	}
	public Long zremrangeByScore(final String key, final double start, final double end) {
		return jedisCluster.zremrangeByScore(key, start, end);
	}
	public Long zremrangeByScore(final String key, final String start, final String end) {
		return jedisCluster.zremrangeByScore(key, start, end);
	}
	public Long zlexcount(final String key, final String min, final String max) {
		return jedisCluster.zlexcount(key, min, max);
	}
	public Set<String> zrangeByLex(final String key, final String min, final String max) {
		return jedisCluster.zrangeByLex(key, min, max);
	}
	public Set<String> zrangeByLex(final String key, final String min, final String max,
								   final int offset, final int count) {
		return jedisCluster.zrangeByLex(key, min, max, offset, count);
	}
	public Set<String> zrevrangeByLex(final String key, final String max, final String min) {
		return jedisCluster.zrevrangeByLex(key, max, min);
	}
	public Set<String> zrevrangeByLex(final String key, final String max, final String min,
									  final int offset, final int count) {
		return jedisCluster.zrevrangeByLex(key, max, min, offset, count);
	}
	public Long zremrangeByLex(final String key, final String min, final String max) {
		return jedisCluster.zremrangeByLex(key, min, max);
	}
	public Long linsert(final String key, final BinaryClient.LIST_POSITION where, final String pivot,
                        final String value) {
		return jedisCluster.linsert(key, where, pivot, value);
	}
	public Long lpushx(final String key, final String... string) {
		return jedisCluster.lpushx(key, string);
	}
	public Long rpushx(final String key, final String... string) {
		return jedisCluster.rpushx(key, string);
	}
	public Long del(final String key) {
		return jedisCluster.del(key);
	}
	public String echo(final String string) {
		// note that it'll be run from arbitary node
		return jedisCluster.echo(string);
	}
	public Long bitcount(final String key) {
		return jedisCluster.bitcount(key);
	}
	public Long bitcount(final String key, final long start, final long end) {
		return jedisCluster.bitcount(key, start, end);
	}
	public Long bitpos(final String key, final boolean value) {
		return jedisCluster.bitpos(key, value);
	}
	public Long bitpos(final String key, final boolean value, final BitPosParams params) {
		return jedisCluster.bitpos(key, value, params);
	}
	public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor) {
		return jedisCluster.hscan(key, cursor);
	}
	public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor, final ScanParams params) {
		return jedisCluster.hscan(key, cursor, params);
	}
	public ScanResult<String> sscan(final String key, final String cursor) {
		return jedisCluster.sscan(key, cursor);
	}
	public ScanResult<String> sscan(final String key, final String cursor, final ScanParams params) {
		return jedisCluster.sscan(key, cursor, params);
	}
	public ScanResult<Tuple> zscan(final String key, final String cursor) {
		return jedisCluster.zscan(key, cursor);
	}
	public ScanResult<Tuple> zscan(final String key, final String cursor, final ScanParams params) {
		return jedisCluster.zscan(key, cursor, params);
	}
	public Long pfadd(final String key, final String... elements) {
		return jedisCluster.pfadd(key, elements);
	}
	public long pfcount(final String key) {
		return jedisCluster.pfcount(key);
	}
	public List<String> blpop(final int timeout, final String key) {
		return jedisCluster.blpop(timeout, key);
	}
	public List<String> brpop(final int timeout, final String key) {
		return jedisCluster.brpop(timeout, key);
	}
	public Long del(final String... keys) {
		return jedisCluster.del(keys);
	}
	public List<String> blpop(final int timeout, final String... keys) {
		return jedisCluster.blpop(timeout, keys);
	}
	public List<String> brpop(final int timeout, final String... keys) {
		return jedisCluster.brpop(timeout, keys);
	}
	public List<String> mget(final String... keys) {
		return jedisCluster.mget(keys);
	}
	public String mset(final String... keysvalues) {
		return jedisCluster.mset(keysvalues);
	}
	public Long msetnx(final String... keysvalues) {
		return jedisCluster.msetnx(keysvalues);
	}
	public String rename(final String oldkey, final String newkey) {
		return jedisCluster.rename(oldkey, newkey);
	}
	public Long renamenx(final String oldkey, final String newkey) {
		return jedisCluster.renamenx(oldkey, newkey);
	}
	public String rpoplpush(final String srckey, final String dstkey) {
		return jedisCluster.rpoplpush(srckey, dstkey);
	}
	public Set<String> sdiff(final String... keys) {
		return jedisCluster.sdiff(keys);
	}
	public Long sdiffstore(final String dstkey, final String... keys) {
		return jedisCluster.sdiffstore(dstkey, keys);
	}
	public Set<String> sinter(final String... keys) {
		return jedisCluster.sinter(keys);
	}
	public Long sinterstore(final String dstkey, final String... keys) {
		return jedisCluster.sinterstore(dstkey, keys);
	}
	public Long smove(final String srckey, final String dstkey, final String member) {
		return jedisCluster.smove(srckey, dstkey, member);
	}
	public Long sort(final String key, final SortingParams sortingParameters, final String dstkey) {
		return jedisCluster.sort(key, sortingParameters, dstkey);
	}
	public Long sort(final String key, final String dstkey) {
		return jedisCluster.sort(key, dstkey);
	}
	public Set<String> sunion(final String... keys) {
		return jedisCluster.sunion(keys);
	}
	public Long sunionstore(final String dstkey, final String... keys) {
		return jedisCluster.sunionstore(dstkey, keys);
	}
	public Long zinterstore(final String dstkey, final String... sets) {
		return jedisCluster.zinterstore(dstkey, sets);
	}
	public Long zinterstore(final String dstkey, final ZParams params, final String... sets) {
		return jedisCluster.zinterstore(dstkey, params, sets);
	}
	public Long zunionstore(final String dstkey, final String... sets) {
		return jedisCluster.zunionstore(dstkey, sets);
	}
	public Long zunionstore(final String dstkey, final ZParams params, final String... sets) {
		return jedisCluster.zunionstore(dstkey, params, sets);
	}
	public String brpoplpush(final String source, final String destination, final int timeout) {
		return jedisCluster.brpoplpush(source, destination, timeout);
	}
	public Long publish(final String channel, final String message) {
		return jedisCluster.publish(channel, message);
	}
	public void subscribe(final JedisPubSub jedisPubSub, final String... channels) {
		jedisCluster.subscribe(jedisPubSub, channels);
	}
	public void psubscribe(final JedisPubSub jedisPubSub, final String... patterns) {
		jedisCluster.subscribe(jedisPubSub, patterns);
	}
	public Long bitop(final BitOP op, final String destKey, final String... srcKeys) {
		return jedisCluster.bitop(op, destKey, srcKeys);
	}
	public String pfmerge(final String destkey, final String... sourcekeys) {
		return jedisCluster.pfmerge(destkey, sourcekeys);
	}
	public long pfcount(final String... keys) {
		return jedisCluster.pfcount(keys);
	}
	public Object eval(final String script, final int keyCount, final String... params) {
		return jedisCluster.eval(script, keyCount, params);
	}
	public Object eval(final String script, final String key) {
		return jedisCluster.eval(script,key);
	}
	public Object eval(final String script, final List<String> keys, final List<String> args) {
		return jedisCluster.eval(script, keys, args);
	}
	public Object evalsha(final String sha1, final int keyCount, final String... params) {
		return jedisCluster.evalsha(sha1, keyCount, params);
	}
	public Object evalsha(final String sha1, final List<String> keys, final List<String> args) {
		return jedisCluster.evalsha(sha1, keys, args);
	}
	public Object evalsha(final String script, final String key) {
		return jedisCluster.evalsha(script,key);
	}
	public List<Boolean> scriptExists(final String sha1, final String key) {
		return jedisCluster.scriptExists(sha1);
	}
	public List<Boolean> scriptExists(final String key, final String... sha1) {
		return jedisCluster.scriptExists(key,sha1);
	}
	public String scriptLoad(final String script, final String key) {
		return jedisCluster.scriptLoad(script,key);
	}


    /**
     * 解析节点envHosts配置，放到existingHostsAndPorts列表中，如果需要加到集群里的话，不要调用此方法
     * @param envHosts
     * @param existingHostsAndPorts
     * @return
     */
    private List<HostAndPort> parseAddHosts(String envHosts, List<HostAndPort> existingHostsAndPorts) {
	    if (null != envHosts && 0 < envHosts.length()) {
	      String[] hostDefs = envHosts.split(",");
	      if (null != hostDefs && 1 <= hostDefs.length) {
	        List<HostAndPort> envHostsAndPorts = new ArrayList<HostAndPort>(hostDefs.length);
	        for (String hostDef : hostDefs) {
	          String[] hostAndPort = hostDef.split(":");
	          if (null != hostAndPort && 2 == hostAndPort.length) {
	            String host = hostAndPort[0];
	            int port = Protocol.DEFAULT_PORT;
	            try {
	              port = Integer.parseInt(hostAndPort[1]);
	            } catch (final NumberFormatException nfe) {
	            }
	            envHostsAndPorts.add(new HostAndPort(host, port));
	          }
	        }
	        existingHostsAndPorts.addAll(envHostsAndPorts);
	      }
	    }
	    return existingHostsAndPorts;
    }
    public static void main1(String[] args)throws Exception {
        RedisClusterProperties redisClusterProperties =  new RedisClusterProperties();
        redisClusterProperties.setHost("172.24.8.32");
        redisClusterProperties.setPort(7000);
        JRedisClusterUtils jrc = new JRedisClusterUtils(redisClusterProperties);
        jrc.test();
    }
}
