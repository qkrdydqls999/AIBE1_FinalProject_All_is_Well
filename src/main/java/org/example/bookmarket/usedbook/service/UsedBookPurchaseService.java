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
            // 락을 획득하기 위해 10초간 대기하고, 락을 점유하는 시간은 5초로 설정
            boolean isLocked = lock.tryLock(10, 5, TimeUnit.SECONDS);

            if (!isLocked) {
                log.error("책 구매 락 획득 실패. lockKey={}", lockKey);
                throw new CustomException(ErrorCode.PURCHASE_LOCK_FAILED);
            }

            log.info("락 획득 성공! lockKey={}", lockKey);
            processPurchase(usedBookId);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 현재 스레드의 interrupt 상태를 다시 설정
            log.error("락 대기 중 인터럽트 발생. lockKey={}", lockKey, e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "락을 기다리는 중 문제가 발생했습니다.");
        } finally {
            // 현재 스레드가 락을 점유하고 있을 경우에만 락 해제
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("락 해제. lockKey={}", lockKey);
            }
        }
    }

    @Transactional
    public void processPurchase(Long usedBookId) {
        UsedBook usedBook = usedBookRepository.findById(usedBookId)
                .orElseThrow(() -> new CustomException(ErrorCode.USED_BOOK_NOT_FOUND));

        if (!"FOR_SALE".equalsIgnoreCase(usedBook.getStatus())) {
            throw new CustomException(ErrorCode.BOOK_ALREADY_SOLD);
        }

        log.info("책 구매 처리 시작. 책 ID: {}, 현재 상태: {}", usedBookId, usedBook.getStatus());
        usedBook.markAsSold(); // 상태를 "판매 완료"로 변경
        log.info("책 구매 처리 완료. 책 ID: {}, 변경된 상태: {}", usedBookId, usedBook.getStatus());
    }
}