package org.example.bookmarket.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * QueryDSL을 사용하기 위해 JPAQueryFactory를 Spring Bean으로 등록하는 설정 클래스입니다.
 */
@Configuration
public class QueryDslConfig {

    // EntityManager를 주입받기 위해 @PersistenceContext 어노테이션을 사용합니다.
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JPAQueryFactory를 Bean으로 등록합니다.
     * 이제부터 다른 클래스에서 @RequiredArgsConstructor나 @Autowired를 통해
     * JPAQueryFactory를 주입받아 사용할 수 있습니다.
     *
     * @return 설정이 완료된 JPAQueryFactory 객체
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}