package com.onejo.seosuri.domain.problem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProbWordRepository extends JpaRepository<ProbWord, Long> {
    Optional<ProbWord> findById(Long id);

    Long deleteByProb(Problem prob);
}
