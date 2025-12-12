package test.avows.policy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import test.avows.policy.client.CustomerClient;
import test.avows.policy.dto.PolicyResponseDto;
import test.avows.policy.dto.PolicyRequestDto;
import test.avows.policy.entity.Policy;
import test.avows.policy.exception.ApiException;
import test.avows.policy.messaging.producer.UnderwritingProducer;
import test.avows.policy.repository.PolicyRepository;
import test.avows.policy.util.PolicyUtil;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.sql.Timestamp;
import java.util.List;

import static test.avows.policy.util.ConstantUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final CustomerClient customerClient;
    private final UnderwritingProducer underwritingProducer;
    private final ObjectMapper objectMapper;


    public PolicyResponseDto getPolicy(Long policyId) {
        return policyRepository.findById(policyId)
                .map(policy -> PolicyResponseDto.builder()
                        .policyId(policy.getPolicyId())
                        .customerId(policy.getCustomerId())
                        .policyNumber(policy.getPolicyNumber())
                        .type(policy.getType())
                        .premiumAmount(policy.getPremiumAmount())
                        .status(policy.getStatus())
                        .startDate(policy.getStartDate())
                        .endDate(policy.getEndDate())
                        .build()
                ).orElseThrow(() -> new ApiException(400, "not found", "no data found for policy id " + policyId));

    }

    public List<PolicyResponseDto> getPoliciesByCustomerId(Long customerId) {
        return policyRepository.findByCustomerId(customerId).stream()
                .map(policy -> PolicyResponseDto.builder()
                        .policyId(policy.getPolicyId())
                        .customerId(policy.getCustomerId())
                        .policyNumber(policy.getPolicyNumber())
                        .type(policy.getType())
                        .premiumAmount(policy.getPremiumAmount())
                        .status(policy.getStatus())
                        .startDate(policy.getStartDate())
                        .endDate(policy.getEndDate())
                        .build()
                ).toList();
    }

    public void createPolicy(PolicyRequestDto param) {

        Long customerId = param.getCustomerId();
        JsonNode customerData = customerClient.getCustomerById(customerId).asOptional()
                .orElseThrow(() -> new ApiException(400, "not found", "no data found for customer id " + customerId));

        log.info("fetch data from customer service: {}", customerData);

        Policy policy = policyRepository.save(Policy.builder()
                .customerId(param.getCustomerId())
                .policyNumber(PolicyUtil.generatePolicyNumber())
                .type(param.getPolicyType())
                .premiumAmount(param.getPremiumAmount())
                .coverageAmount(param.getCoverageAmount())
                .status(POLICY_STATUS_PENDING_UNDERWRITING)
                .build()
        );

        ObjectNode message = objectMapper.createObjectNode();
        message.put("policyId", policy.getPolicyId());
        message.put("policyType", param.getPolicyType());
        message.put("coverageAmount", param.getCoverageAmount().toString());
        message.put("premiumAmount", param.getPremiumAmount().toString());
        message.put("age", customerData.get("age").asInt());

        log.info("sending underwriting request message: {}", message);

        underwritingProducer.sendMessage("underwriting-requests", message.toString());
    }

    public void updatePolicy(Long policyId, String status) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ApiException(400, "not found", "no data found for policy id " + policyId));

        Timestamp startDate = new Timestamp(System.currentTimeMillis());
        Timestamp endDate = Timestamp.valueOf(startDate.toLocalDateTime().plusYears(1));

        if(status.equals(PAYMENT_STATUS_PAID)) {
            policy.setStatus(POLICY_STATUS_ACTIVE);
        }
        else if (status.equals(POLICY_STATUS_APPROVED)) {
            policy.setStatus(POLICY_STATUS_APPROVED);
            policy.setStartDate(startDate);
            policy.setEndDate(endDate);
        }
        else {
            policy.setStartDate(null);
            policy.setEndDate(null);
        }

        policyRepository.save(policy);
    }
}
