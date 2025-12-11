package test.avows.policy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.avows.policy.common.ApiResponse;
import test.avows.policy.dto.PolicyResponseDto;
import test.avows.policy.dto.PolicyRequestDto;
import test.avows.policy.service.PolicyService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/policy")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    @GetMapping("/customer/{customerId}")
    public List<PolicyResponseDto> getCustomerPolicies(@PathVariable Long customerId) {
        return policyService.getPoliciesByCustomerId(customerId);
    }

    @GetMapping("/{policyId}")
    public PolicyResponseDto getPolicy(@PathVariable Long policyId) {
        return policyService.getPolicy(policyId);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createCustomer(@RequestBody PolicyRequestDto param) {
        policyService.createPolicy(param);
        return ResponseEntity.ok().body(
                new ApiResponse(
                        true,
                        "successfully create policy",
                        null
                )
        );
    }

    @PutMapping("/{PolicyId}")
    public ResponseEntity<ApiResponse> updatePolicy(@PathVariable Long policyId, @RequestBody PolicyResponseDto param) {
        policyService.updatePolicy(policyId, param);
        return ResponseEntity.ok().body(
                new ApiResponse(
                        true,
                        "successfully update policy",
                        null
                )
        );
    }
}
