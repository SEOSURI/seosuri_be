package com.onejo.seosuri.domain.problem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemTemplateRepository extends JpaRepository<ProblemTemplate, Long> {
    Optional<ProblemTemplate> findById(Long id);
}
