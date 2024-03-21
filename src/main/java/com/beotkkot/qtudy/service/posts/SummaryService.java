package com.beotkkot.qtudy.service.posts;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SummaryService {
    @Value("${CLOVA_API_KEY_ID}")
    private String CLOVA_API_KEY_ID;
    @Value("${CLOVA_API_KEY}")
    private String CLOVA_API_KEY;
    private static final String ENDPOINT = "https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize";

    // 텍스트 데이터 받아와서 요약
    public String summary(String content) {

        RestTemplate rt = new RestTemplate();

        // HTTPHeader
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;UTF-8");
        headers.add("X-NCP-APIGW-API-KEY-ID", CLOVA_API_KEY_ID);
        headers.add("X-NCP-APIGW-API-KEY", CLOVA_API_KEY);

        // HTTPBody
        Map<String, Object> document = new HashMap<>();
        document.put("content", content);

        Map<String, Object> option = new HashMap<>();
        option.put("language", "ko");
        option.put("model", "general");
        option.put("tone", 2);

        Map<String, Object> params = new HashMap<>();
        params.put("document", document);
        params.put("option", option);

        System.out.println("params = " + params);

        HttpEntity<Map<String, Object>> summaryTextRequest = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = rt.exchange(ENDPOINT, HttpMethod.POST, summaryTextRequest, String.class);

        String responseBody = response.getBody();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(responseBody);
        String summaryText = element.getAsJsonObject().get("summary").getAsString();

        System.out.println("원본 = " + content);
        System.out.println("요약본 = " + summaryText);

        return summaryText;
    }
}
