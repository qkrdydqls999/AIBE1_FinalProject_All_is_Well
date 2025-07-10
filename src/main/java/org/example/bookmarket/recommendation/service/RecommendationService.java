package org.example.bookmarket.recommendation.service;

import org.example.bookmarket.usedbook.dto.UsedBookResponse;
import java.util.List;

/**
 * 개인화 도서 추천 기능을 위한 서비스 인터페이스
 */
public interface RecommendationService {
    /**
     * 사용자의 관심 카테고리, 찜 목록을 기반으로 개인화된 중고 도서를 추천합니다.
     * 비로그인 사용자의 경우, 최신 등록 도서를 반환합니다.
     * @param userId 현재 로그인한 사용자 ID (비로그인 시 null)
     * @param limit 추천할 도서의 수
     * @return 추천된 중고 도서 목록
     */
    List<UsedBookResponse> getPersonalizedRecommendations(Long userId, int limit);
}