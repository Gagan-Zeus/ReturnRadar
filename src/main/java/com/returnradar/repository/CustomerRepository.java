package com.returnradar.repository;

import com.returnradar.entity.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByFlaggedAsSuspiciousTrue();

    Optional<Customer> findByEmail(String email);
}
