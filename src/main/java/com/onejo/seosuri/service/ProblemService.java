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
        //~ 문제 리스트 생성 로직 (필요에 따라 수정하셔도 됩니다) ~
        List<CreateProbRes> problemList = new ArrayList<>();

        // 시험지 entity 생성 및 DB 저장

        // # 시험지 만들기 - 10회 반복

        // 템플릿 선택 - 카테고리, 난이도 맞춰서
        // name 조건에 맞춰 랜덤 선택 - arrayList에 넣습니다
        // obj 조건 맞춰 랜덤 선택 - arrayList에 넣습니다
        // num 난이도 맞춰 랜덤 선택 - arrayList에 넣습니다

        // 문제 번호, 문제 난이도 설정
        // content (문제 내용) 작성
        // explanation (해설) 작성
        // answer (답) 작성

        // 문제 entity 생성 및 DB 저장


        // 시험지 리스트 반환
        return problemList;
    }
}
