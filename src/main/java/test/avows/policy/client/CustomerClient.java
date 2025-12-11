package test.avows.policy.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;

@Component
@RequiredArgsConstructor
public class CustomerClient {

    @Value("${services.customer-url}")
    private String customerServiceUrl;

    private final WebClient webClient;

    public JsonNode getCustomerById(Long customerId) {
        return webClient.get()
                .uri(customerServiceUrl + "/api/v1/customer/{customerId}", customerId)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
}
