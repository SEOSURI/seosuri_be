package com.onejo.seosuri.domain.classification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BigUnitRepository extends JpaRepository<BigUnit, Long> {
    Optional<BigUnit> findById(Long id);
}
