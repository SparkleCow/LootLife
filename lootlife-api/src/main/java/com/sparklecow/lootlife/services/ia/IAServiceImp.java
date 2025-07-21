package com.sparklecow.lootlife.services.ia;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class IAServiceImp implements IAService{

    private final WebClient webClient;

    @Override
    public String sendRequest(String message) {
        String model = "mistralai/mistral-nemo:free";

        Map<String, Object> request = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "user", "content", message)
                )
        );

        try {
            Map<String, Object> response = webClient.post()
                    .uri("/chat/completions")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .log()
                    .block();

            if (response == null) {
                return "Respuesta nula del modelo IA.";
            }

            if (response.containsKey("error")) {
                Map<String, Object> error = (Map<String, Object>) response.get("error");
                return "Error del modelo IA: " + error.get("message");
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> choice = choices.get(0);
                Map<String, Object> iaMessage = (Map<String, Object>) choice.get("message");
                if (iaMessage != null && iaMessage.containsKey("content")) {
                    log.info("IA provider response: {}", iaMessage);
                    return (String) iaMessage.get("content");
                }
            }

        } catch (Exception e) {
            log.error("Error querying IA agent", e);
            return "Error querying IA agent: " + e.getMessage();
        }

        return "Without IA response";
    }
}
