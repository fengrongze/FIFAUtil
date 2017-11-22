package com.softisland.bean.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redis集群配置
 * @author wangj@2015.12.21
 *
 */
@ConfigurationProperties(prefix = "spring.rediscluster")
public class RedisClusterProperties {

	/**
	 * Redis server host.
	 */
	private String host = "localhost";


	/**
	 * Redis server port.
	 */
	private int port = 7000;

	/**
	 * Connection timeout in milliseconds.
	 */
	private int timeout;

	private Pool pool;

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}


	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public Pool getPool() {
		return this.pool;
	}

	public void setPool(Pool pool) {
		this.pool = pool;
	}

	/**
	 * Pool properties.
	 */
	public static class Pool {

		/**
		 * Max number of "idle" connections in the pool. Use a negative value to indicate
		 * an unlimited number of idle connections.
		 */
		private int maxIdle = 8;

		/**
		 * Target for the minimum number of idle connections to maintain in the pool. This
		 * setting only has an effect if it is positive.
		 */
		private int minIdle = 0;

		/**
		 * Max number of connections that can be allocated by the pool at a given time.
		 * Use a negative value for no limit.
		 */
		private int maxActive = 8;

		/**
		 * Maximum amount of time (in milliseconds) a connection allocation should block
		 * before throwing an exception when the pool is exhausted. Use a negative value
		 * to block indefinitely.
		 */
		private int maxWait = -1;

		public int getMaxIdle() {
			return this.maxIdle;
		}

		public void setMaxIdle(int maxIdle) {
			this.maxIdle = maxIdle;
		}

		public int getMinIdle() {
			return this.minIdle;
		}

		public void setMinIdle(int minIdle) {
			this.minIdle = minIdle;
		}

		public int getMaxActive() {
			return this.maxActive;
		}

		public void setMaxActive(int maxActive) {
			this.maxActive = maxActive;
		}

		public int getMaxWait() {
			return this.maxWait;
		}

		public void setMaxWait(int maxWait) {
			this.maxWait = maxWait;
		}
	}

}
