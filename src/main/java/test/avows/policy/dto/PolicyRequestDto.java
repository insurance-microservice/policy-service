package test.avows.policy.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyRequestDto {
    private Long customerId;
    private BigDecimal premiumAmount;
    private BigDecimal coverageAmount;
    private String policyType;
}
