package test.avows.policy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDto {
    private Long policyId;
    private Long customerId;
    private String policyNumber;
    private String type;
    private BigDecimal premiumAmount;
    private String status;
    private Timestamp startDate;
    private Timestamp endDate;
}
