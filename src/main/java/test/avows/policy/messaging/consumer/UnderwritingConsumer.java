package test.avows.policy.messaging.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import test.avows.policy.service.PolicyService;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnderwritingConsumer {

    private final ObjectMapper objectMapper;
    private final PolicyService policyService;

    @KafkaListener(topics = "underwriting-results", groupId = "underwriting-policy-group")
    public void consumeUnderwritingResult(String message) {

        log.info("Received underwriting result: {}", message);

        JsonNode event = objectMapper.readTree(message);
        Long policyId = event.get("policyId").asLong();
        String status = event.get("status").asText();

        policyService.updatePolicy(policyId, status);
    }
}
