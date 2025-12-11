package test.avows.policy.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.avows.policy.entity.Policy;

import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<@NonNull Policy, @NonNull Long> {
    List<Policy> findByCustomerId(@NonNull Long customerId);
}
