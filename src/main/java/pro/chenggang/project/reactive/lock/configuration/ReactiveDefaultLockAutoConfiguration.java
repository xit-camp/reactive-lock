package pro.chenggang.project.reactive.lock.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pro.chenggang.project.reactive.lock.core.ReactiveLockRegistry;
import pro.chenggang.project.reactive.lock.core.defaults.DefaultReactiveLockRegistry;
import pro.chenggang.project.reactive.lock.properties.RedisReactiveLockProperties;
import reactor.core.publisher.Flux;

/**
 * ReactiveDefaultLockAutoConfiguration AutoConfiguration
 *
 * @author Gang Cheng
 * @date 2021-03-14.
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ReactiveLockRegistry.class, Flux.class})
public class ReactiveDefaultLockAutoConfiguration {

    /**
    * Redis reactive lock properties.
    *
    * @return the redis reactive lock properties
    */
    @Bean
    @ConditionalOnMissingBean(RedisReactiveLockProperties.class)
    @ConfigurationProperties(RedisReactiveLockProperties.REDIS_LOCK_PROPERTIES_PREFIX)
    public RedisReactiveLockProperties redisReactiveLockProperties() {
        return new RedisReactiveLockProperties();
    }

    /**
     * Default reactive lock registry.
     *
     * @param redisReactiveLockProperties the redis reactive lock properties
     * @return the reactive lock registry
     */
    @Bean
    @ConditionalOnExpression("@redisReactiveLockProperties.reactiveLockType.contains(T(pro.chenggang.project.reactive.lock.option.ReactiveLockType).DEFAULT)")
    public ReactiveLockRegistry defaultReactiveLockRegistry(RedisReactiveLockProperties redisReactiveLockProperties) {
        ReactiveLockRegistry reactiveLockRegistry = new DefaultReactiveLockRegistry(redisReactiveLockProperties.getExpireEvictIdle(), redisReactiveLockProperties.getExpireAfter());
        log.info("Load Default Reactive Lock Registry Success,Default Expire Duration:{}", redisReactiveLockProperties.getExpireAfter());
        return reactiveLockRegistry;
    }

}
