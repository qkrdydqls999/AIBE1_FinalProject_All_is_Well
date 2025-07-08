package org.example.bookmarket.ai.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookmarket.ai.dto.PriceSuggestResponse;
import org.example.bookmarket.ai.service.AiService;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.example.bookmarket.common.service.S3UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final S3UploadService s3UploadService;

    /**
     * [에러 핸들링 수정]
     * 프론트엔드에서 받은 이미지 파일로 AI 가격 분석을 수행합니다.
     * IOException 발생 시, try-catch로 잡는 대신 CustomException을 발생시킵니다.
     */
    @PostMapping("/suggest-price-from-upload")
    public ResponseEntity<PriceSuggestResponse> uploadAndSuggestPrice(
            @RequestParam("image") MultipartFile image,
            @RequestParam("newPrice") int newPrice) {

        if (image.isEmpty()) {
            log.warn("AI 가격 제안 요청에 이미지 파일이 누락되었습니다.");
            // 잘못된 요청에 대한 명확한 에러 코드를 사용합니다.
            throw new CustomException(ErrorCode.INVALID_REQUEST, "AI 분석을 위한 이미지가 필요합니다.");
        }
        try {
            // 1. S3에 이미지 업로드
            String imageUrl = s3UploadService.upload(image, "temp-book-images");

            // 2. AI 서비스 호출
            PriceSuggestResponse response = aiService.suggestPriceFromImage(imageUrl, newPrice);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            // 3. AI 서비스에서 오류 발생 시
            log.error("AI 이미지 분석 서비스 호출 중 IO 오류가 발생했습니다.", e);
            // 미리 정의된 ErrorCode를 사용하여 CustomException을 발생시킵니다.
            throw new CustomException(ErrorCode.AI_ANALYSIS_FAILED);
        }
    }
}