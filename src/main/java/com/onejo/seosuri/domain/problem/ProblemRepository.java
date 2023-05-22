package com.onejo.seosuri.domain.problem;

import com.onejo.seosuri.domain.classification.Category;
import com.onejo.seosuri.domain.classification.SchoolYear;
import com.onejo.seosuri.domain.testpaper.TestPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findById(Long id);

    List<Problem> findAllByTestPaper(TestPaper testPaper);
}
