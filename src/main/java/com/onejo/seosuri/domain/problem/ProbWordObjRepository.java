package com.onejo.seosuri.domain.problem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProbWordObjRepository extends JpaRepository<ProbWordObj, Long> {
    Optional<ProbWordObj> findById(Long id);
}
