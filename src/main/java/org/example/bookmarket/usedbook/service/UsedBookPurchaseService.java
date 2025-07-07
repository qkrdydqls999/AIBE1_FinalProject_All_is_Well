package org.example.bookmarket.usedbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.usedbook.repository.UsedBookRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsedBookPurchaseService {

    private final RedissonClient redissonClient;
    private final UsedBookRepository usedBookRepository;

    public void purchase(Long usedBookId) {
        final String lockKey = "lock:usedbook:" + usedBookId;
        final RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(10, 5, TimeUnit.SECONDS);

            if (!isLocked) {
                log.error("책 구매 락 획득 실패. lockKey={}", lockKey);
                // [수정] CustomException의 새로운 생성자 사용
                throw new CustomException(ErrorCode.PURCHASE_LOCK_FAILED, "락 획득 실패: " + lockKey);
            }

            log.info("락 획득 성공! lockKey={}", lockKey);
            processPurchase(usedBookId);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("락 대기 중 인터럽트 발생. lockKey={}", lockKey, e);
            // [수정] CustomException의 새로운 생성자 사용
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "락을 기다리는 중 문제가 발생했습니다: " + e.getMessage());
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("락 해제. lockKey={}", lockKey);
            }
        }
    }

    @Transactional
    public void processPurchase(Long usedBookId) {
        // [개선] 프로젝트 표준 예외 처리 사용
        UsedBook usedBook = usedBookRepository.findById(usedBookId)
                .orElseThrow(() -> new CustomException(ErrorCode.USED_BOOK_NOT_FOUND));

        // "판매중" 상태일 때만 구매 가능
        if (!"판매중".equals(usedBook.getStatus())) {
            // [개선] 프로젝트 표준 예외 처리 사용
            throw new CustomException(ErrorCode.BOOK_ALREADY_SOLD);
        }

        log.info("책 구매 처리 시작. 책 ID: {}, 현재 상태: {}", usedBookId, usedBook.getStatus());

        // [수정] 엔티티의 비즈니스 메서드를 사용하여 상태 변경
        usedBook.markAsSold();
        // @Transactional에 의해 변경 감지(Dirty Checking)되므로 명시적 save는 불필요합니다.

        log.info("책 구매 처리 완료. 책 ID: {}, 변경된 상태: {}", usedBookId, usedBook.getStatus());
    }
}