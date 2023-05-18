package com.onejo.seosuri.service;

import com.onejo.seosuri.controller.dto.problem.CreateProbRes;
import com.onejo.seosuri.domain.classification.CategoryRepository;
import com.onejo.seosuri.domain.problem.ProblemRepository;
import com.onejo.seosuri.domain.problem.ProblemTemplateRepository;
import com.onejo.seosuri.domain.testpaper.TestPaperRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final TestPaperRepository testPaperRepository;
    private final ProblemTemplateRepository problemTemplateRepository;
    private final CategoryRepository categoryRepository;


    @Transactional
    public List<CreateProbRes> createProblem(String categoryTitle, String level){

        List<String> tmpList = new ArrayList<>();         // 사용할 템플릿
        List<CreateProbRes> probList = new ArrayList<>();  // 완성된 문제


        // ---------------- 템플릿 완성 후, 사용할 코드 ----------------
        // 다음 조건을 만족하는 템플릿 10개를 랜덤으로 뽑아서 tmpList에 저장한다
        // 템플릿 tmpList를 돌며 조건 맞춰서 숫자 삽입 + 단어 삽입
            // 사용한 단어는 DB에 저장
            // 완성된 문제 Problem DB 저장

        // ---------------- 템플릿 완성 전, api 연결 테스트 코드 ----------------
        // 시험지 id +

        // 문제 리스트 반환
        return probList;
    }
}
