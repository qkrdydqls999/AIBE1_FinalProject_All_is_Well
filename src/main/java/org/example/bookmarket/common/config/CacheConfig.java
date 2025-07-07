package org.example.bookmarket.common.config;

import org.redisson.api.RedissonClient;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.redisson.spring.cache.RedissonSpringCacheManager;


import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * RedissonClient를 사용하여 스프링 캐시 매니저를 생성합니다.
     * 이제 @Cacheable 애너테이션은 내부적으로 Redisson을 통해 동작합니다.
     *
     * @param redissonClient RedissonConfig에서 생성된 Bean이 자동으로 주입됩니다.
     * @return Redisson 기반의 CacheManager
     */
    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        // ✅ 클래스 이름 충돌을 피하기 위해 전체 경로(org.redisson.spring.cache.CacheConfig)를 사용합니다.
        Map<String, org.redisson.spring.cache.CacheConfig> config = new HashMap<>();

        // "categories" 캐시에 대한 개별 설정을 정의합니다.
        // TTL(Time-To-Live): 24시간, MaxIdleTime: 12시간
        // 12시간 동안 아무도 접근하지 않거나, 생성된 지 24시간이 지나면 캐시가 자동으로 삭제됩니다.
        // ✅ 여기에서도 전체 경로를 사용합니다.
        config.put("categories", new org.redisson.spring.cache.CacheConfig(24 * 60 * 60 * 1000, 12 * 60 * 60 * 1000));

        return new RedissonSpringCacheManager(redissonClient, config);
    }
}