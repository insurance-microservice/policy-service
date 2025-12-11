package test.avows.policy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "policy", schema = "policy_svc")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Long policyId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "policy_number")
    private String policyNumber;

    @Column(name = "type")
    private String type;

    @Column(name = "premium_amount")
    private BigDecimal premiumAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;
}
