package com.onejo.seosuri.domain.problem;

import com.onejo.seosuri.domain.classification.Category;
import com.onejo.seosuri.domain.classification.SchoolYear;
import com.onejo.seosuri.domain.testpaper.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findById(Long id);

    List<Problem> findAllByTestPaper(TestPaper testPaper);
    List<Problem> findAllByTestPaperAndState(TestPaper testPaper, String State);

    Optional<Problem> findByTestPaperAndProbNum(TestPaper testPaper, Long probNum);

    @Modifying
    @Query("UPDATE Problem p " + "SET p.probTemp = :probTemp " + "WHERE p.id = :id")
    void updateProbTemp(@Param("id")Long id, @Param("probTemp")ProblemTemplate probTemp);

    @Modifying
    @Query("UPDATE Problem p " + "SET p.content = :content " + "WHERE p.id = :id")
    void updateContent(@Param("id")Long id, @Param("content")String content);

    @Modifying
    @Query("UPDATE Problem p " + "SET p.level = :level " + "WHERE p.id = :id")
    void updateLevel(@Param("id")Long id, @Param("level")String level);

    @Modifying
    @Query("UPDATE Problem p " + "SET p.explanation = :explanation " + "WHERE p.id = :id")
    void updateExplanation(@Param("id")Long id, @Param("explanation")String explanation);

    @Modifying
    @Query("UPDATE Problem p " + "SET p.answer = :answer " + "WHERE p.id = :id")
    void updateAnswer(@Param("id")Long id, @Param("answer")String answer);



}
