package test.avows.policy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import test.avows.policy.client.CustomerClient;
import test.avows.policy.dto.PolicyResponseDto;
import test.avows.policy.dto.PolicyRequestDto;
import test.avows.policy.entity.Policy;
import test.avows.policy.exception.ApiException;
import test.avows.policy.repository.PolicyRepository;
import test.avows.policy.util.PolicyUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static test.avows.policy.util.ConstantUtil.POLICY_STATUS_PENDING_UNDERWRITING;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final CustomerClient customerClient;

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
        policyRepository.save(
                Policy.builder()
                        .customerId(param.getCustomerId())
                        .policyNumber(PolicyUtil.generatePolicyNumber())
                        .type(param.getPolicyType())
                        .premiumAmount(param.getPremiumAmount())
                        .coverageAmount(param.getCoverageAmount())
                        .status(POLICY_STATUS_PENDING_UNDERWRITING)
                        .build()
        );

        //@todo: trigger kafka event for underwriting process

    }

    public void updatePolicy(Long policyId, PolicyResponseDto param) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ApiException(400, "not found", "no data found for policy id " + policyId));

        String policyNumber = param.getPolicyNumber();
        String type = param.getType();
        BigDecimal premiumAmount = param.getPremiumAmount();
        String status = param.getStatus();
        Timestamp startDate = param.getStartDate();
        Timestamp endDate = param.getEndDate();

        if (policyNumber != null) policy.setPolicyNumber(policyNumber);
        if (type != null) policy.setType(type);
        if (premiumAmount != null) policy.setPremiumAmount(premiumAmount);
        if (status != null) policy.setStatus(status);
        if (startDate != null) policy.setStartDate(startDate);
        if (endDate != null) policy.setEndDate(endDate);

        policyRepository.save(policy);
    }
}
