package pro.chenggang.project.reactive.lock.benchmark;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

/**
 * Redis Connection Factory Initialization
 * @author Gang Cheng
 * @since 1.0.0
 */
public class RedisConnectionFactoryInitialization {

    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        GenericObjectPoolConfig genericObjectPoolConfig = this.cacheRedisPoolConfig();
        RedisConfiguration redisConfiguration = this.cacheRedisConfiguration();
        LettuceClientConfiguration lettuceClientConfiguration = this.cacheLettuceClientConfiguration(genericObjectPoolConfig);
        LettuceConnectionFactory lettuceConnectionFactory = this.cacheRedisConnectionFactory(redisConfiguration, lettuceClientConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    private GenericObjectPoolConfig cacheRedisPoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(1000);
        config.setMaxIdle(300);
        config.setMinIdle(100);
        config.setTimeBetweenEvictionRunsMillis(5000);
        config.setMaxWaitMillis(5000);
        return config;
    }

    private RedisConfiguration cacheRedisConfiguration() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("127.0.0.1");
        config.setPort(6379);
        config.setPassword(RedisPassword.of("123123"));
        config.setDatabase(0);
        return config;
    }

    private LettuceClientConfiguration cacheLettuceClientConfiguration(GenericObjectPoolConfig cacheRedisPoolConfig) {
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder()
                .poolConfig(cacheRedisPoolConfig);
        return builder.build();
    }

    private LettuceConnectionFactory cacheRedisConnectionFactory(RedisConfiguration cacheRedisConfiguration, LettuceClientConfiguration cacheLettuceClientConfiguration) {
        return new LettuceConnectionFactory(cacheRedisConfiguration, cacheLettuceClientConfiguration);
    }
}