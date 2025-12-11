package test.avows.policy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.avows.policy.dto.PolicyDto;
import test.avows.policy.entity.Policy;
import test.avows.policy.exception.ApiException;
import test.avows.policy.repository.PolicyRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;

    public PolicyDto getPolicy(Long policyId) {
        return policyRepository.findById(policyId)
                .map(policy -> PolicyDto.builder()
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

    public List<PolicyDto> getPoliciesByCustomerId(Long customerId) {
        return policyRepository.findByCustomerId(customerId).stream()
                .map(policy -> PolicyDto.builder()
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

    public void createPolicy(PolicyDto param) {
        policyRepository.save(
                Policy.builder()
                        .customerId(param.getCustomerId())
                        .policyNumber(param.getPolicyNumber())
                        .type(param.getType())
                        .premiumAmount(param.getPremiumAmount())
                        .status(param.getStatus())
                        .startDate(param.getStartDate())
                        .endDate(param.getEndDate())
                        .build()
        );
    }

    public void updatePolicy(Long policyId, PolicyDto param) {
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
