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
public class PaymentConsumer {

    private final ObjectMapper objectMapper;
    private final PolicyService policyService;

    @KafkaListener(topics = "payment-responses", groupId = "payment-policy-group")
    public void consumerPaymentResponse(String message) {

        log.info("Received payment response: {}", message);

        JsonNode event = objectMapper.readTree(message);
        Long policyId = event.get("policyId").asLong();
        String status = event.get("paymentStatus").asText();

        policyService.updatePolicy(policyId, status);
    }
}
