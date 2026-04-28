package com.careercompass.ai.repository;

import com.careercompass.ai.model.Certificate;
import com.careercompass.ai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByUserOrderByIssuedAtDesc(User user);
}
