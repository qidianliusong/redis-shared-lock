package com.ls.redisSharedLock.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableAutoConfiguration
public class RedisConfig {
	public static Logger logger = LoggerFactory.getLogger(RedisConfig.class);

	@Value("${spring.redis.defaultExpiration:3600}")
	private Long defaultExpiration;

	@Bean
	@ConfigurationProperties(prefix = "spring.redis")
	public JedisPoolConfig getRedisConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		return config;
	}

	@Primary
	@Bean(name = "jedisConnectionFactory")
	@ConfigurationProperties(prefix = "spring.redis")
	public JedisConnectionFactory getConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		JedisPoolConfig config = getRedisConfig();
		factory.setPoolConfig(config);
		logger.info("redis连接池配置成功");
		return factory;
	}



	@Bean(name = "redisJdkTemplate")
	public RedisTemplate<String, Object> getRedisJdkTemplate() {

		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(getConnectionFactory());

		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringRedisSerializer);
		template.setHashKeySerializer(stringRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}

	@Bean(name = "redisTemplate")
	public RedisTemplate<String, Object> getRedisTemplate() {
		// RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		// redisTemplate.setConnectionFactory(getConnectionFactory());
		// StringRedisSerializer stringRedisSerializer = new
		// StringRedisSerializer(Charset.forName("UTF-8"));
		// redisTemplate.setKeySerializer(stringRedisSerializer);
		// redisTemplate.setHashKeySerializer(stringRedisSerializer);
		// return redisTemplate;

		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(getConnectionFactory());
		Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringRedisSerializer);
		template.setHashKeySerializer(stringRedisSerializer);
		template.setValueSerializer(jackson2JsonRedisSerializer);
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}

	@Bean(name = "stringRedisTemplate")
	public StringRedisTemplate getStringRedisTemplate() {
		return new StringRedisTemplate(getConnectionFactory());
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.redis")
	public CacheManager cacheManager() {
		RedisCacheManager redisCacheManager = new RedisCacheManager(getRedisTemplate());
		redisCacheManager.setDefaultExpiration(defaultExpiration);
		return redisCacheManager;
	}

	// @Bean
	// public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory
	// factory) {
	//// StringRedisTemplate template = new StringRedisTemplate(factory);
	// RedisTemplate<String, Object> template = new RedisTemplate<>();
	// redisTemplate.setConnectionFactory(getConnectionFactory());
	// Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new
	// Jackson2JsonRedisSerializer(Object.class);
	// ObjectMapper om = new ObjectMapper();
	// om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
	// om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
	// jackson2JsonRedisSerializer.setObjectMapper(om);
	// template.setValueSerializer(jackson2JsonRedisSerializer);
	// template.afterPropertiesSet();
	// return template;
	// }
}
