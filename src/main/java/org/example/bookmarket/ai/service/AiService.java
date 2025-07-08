package org.example.bookmarket.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookmarket.ai.dto.PriceSuggestResponse;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.endpoint}")
    private String apiEndpoint;

    public PriceSuggestResponse suggestPriceFromImage(String imageUrl, int newPrice) throws IOException {
        String prompt = "당신은 중고책의 결함을 찾아내는 전문 분석가입니다. 이 사진을 보고 아래 '결함 목록' 중에서 해당하는 것을 모두 찾아서 키워드 리스트 형태로 알려주세요. 결함이 전혀 없으면 '없음'이라고 답해주세요. 결함 목록: [모서리 닳음, 커버 긁힘, 페이지 찢어짐, 물에 젖은 자국, 낙서 또는 필기, 변색 또는 황변, 스티커 부착] 결과는 반드시 '결함: [키워드1, 키워드2, ...]' 형식으로만 대답해주세요.";

        byte[] imageBytes;
        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            imageBytes = inputStream.readAllBytes();
        }
        String base64EncodedImage = Base64.getEncoder().encodeToString(imageBytes);

        String requestBody = String.format("""
            {
              "contents": [
                {
                  "parts": [
                    { "text": "%s" },
                    { "inline_data": { "mime_type": "image/jpeg", "data": "%s" } }
                  ]
                }
              ]
            }
            """, prompt, base64EncodedImage);

        String analysisResultText = callGeminiApi(requestBody);

        List<String> defects = parseDefects(analysisResultText);
        int penalty = calculatePenalty(defects);
        double finalRatio = (100.0 - penalty) / 100.0;
        int suggestedMaxPrice = (int) (newPrice * finalRatio * 0.8 / 100.0) * 100;
        int suggestedMinPrice = (int) (suggestedMaxPrice * 0.85 / 100.0) * 100;

        return new PriceSuggestResponse(defects, suggestedMinPrice, suggestedMaxPrice);
    }

    public List<String> extractKeywordsFromQuery(String userQuery) throws IOException {
        String prompt = String.format("다음 사용자 검색어에서 도서 검색에 사용할 핵심 키워드를 1~3개 추출해서 쉼표로 구분된 목록 형태로만 반환해줘. 다른 부가적인 설명은 절대로 추가하지 마. 사용자 검색어: \\\"%s\\\"", userQuery);

        String requestBody = String.format("""
            {
              "contents": [ { "parts": [ { "text": "%s" } ] } ]
            }
            """, prompt);

        String resultText = callGeminiApi(requestBody);

        if (resultText.isEmpty()) {
            return Collections.emptyList();
        }
        return List.of(resultText.split(",")).stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private String callGeminiApi(String requestBody) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        String urlWithKey = apiEndpoint + "?key=" + apiKey;

        try {
            String response = restTemplate.postForObject(urlWithKey, entity, String.class);

            JsonNode root = objectMapper.readTree(response);
            JsonNode textNode = root.at("/candidates/0/content/parts/0/text");
            return textNode.asText();

        } catch (RestClientException e) {
            log.error("Gemini API 호출 중 오류 발생", e);
            throw new CustomException(ErrorCode.AI_ANALYSIS_FAILED);
        }
    }

    /**
     * [수정] AI 응답 파싱 로직을 더 안정적으로 변경합니다.
     * AI 응답에 다른 텍스트가 포함되어 있어도 '결함:' 키워드가 있는 줄을 찾아 처리합니다.
     */
    private List<String> parseDefects(String text) {
        String[] lines = text.split("\\R"); // 응답 텍스트를 줄 단위로 분리
        for (String line : lines) {
            if (line.trim().startsWith("결함:")) {
                Pattern pattern = Pattern.compile("\\[(.*?)\\]");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String defectString = matcher.group(1).trim();
                    if (defectString.equalsIgnoreCase("없음") || defectString.isEmpty()) {
                        return Collections.emptyList();
                    }
                    return List.of(defectString.split(",")).stream()
                            .map(String::trim)
                            .collect(Collectors.toList());
                }
            }
        }
        // '결함:' 키워드를 포함한 줄을 찾지 못한 경우
        log.warn("AI 응답에서 '결함:' 키워드를 찾지 못했습니다. 전체 응답: {}", text);
        return List.of("결함 정보 없음");
    }

    private int calculatePenalty(List<String> defects) {
        int totalPenalty = 0;
        for (String defect : defects) {
            switch (defect) {
            case "페이지 찢어짐", "물에 젖은 자국" -> totalPenalty += 30;
            case "낙서 또는 필기", "변색 또는 황변" -> totalPenalty += 20;
            case "모서리 닳음", "커버 긁힘", "스티커 부착" -> totalPenalty += 10;
            }
        }
        return Math.min(100, totalPenalty);
    }
}