package com.onejo.seosuri.service;

import com.onejo.seosuri.controller.dto.problem.CreateProbRes;
import com.onejo.seosuri.domain.classification.Category;
import com.onejo.seosuri.domain.classification.CategoryRepository;
import com.onejo.seosuri.domain.classification.CategoryTitle;
import com.onejo.seosuri.domain.problem.Problem;
import com.onejo.seosuri.domain.problem.ProblemRepository;
import com.onejo.seosuri.domain.problem.ProblemTemplateRepository;
import com.onejo.seosuri.domain.testpaper.TestPaper;
import com.onejo.seosuri.domain.testpaper.TestPaperRepository;

import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.exception.common.ErrorCode;
import com.onejo.seosuri.util.ProbNumComparator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final TestPaperRepository testPaperRepository;
    private final ProblemTemplateRepository problemTemplateRepository;
    private final CategoryRepository categoryRepository;


    @Transactional
    public List<CreateProbRes> createProblem(String categoryTitle, String level){

        Long testPaperId = -1L;
        List<String> tmpList = new ArrayList<>();         // 사용할 템플릿
        List<CreateProbRes> probList = new ArrayList<>();  // 완성된 문제

        // 시험지 만들기
//        TestPaper testPaper = new TestPaper();
//        Optional<Category> optional = categoryRepository.findByCategoryTitle(CategoryTitle.valueOf(categoryTitle));
//        if(optional.isEmpty()) {
//            throw new BusinessException(ErrorCode.NO_EXIST_CATEGORY_TITLE);
//        }
//        testPaper.setCategory(optional.get());
//        testPaperId = testPaper.getId();

        //문제 만들기



        // ---------------- 템플릿 완성 후, 사용할 코드 ----------------
        // 다음 조건을 만족하는 템플릿 10개를 랜덤으로 뽑아서 tmpList에 저장한다
        // 템플릿 tmpList를 돌며 조건 맞춰서 숫자 삽입 + 단어 삽입
            // 사용한 단어는 DB에 저장
            // 완성된 문제 Problem DB 저장

        // ---------------- 템플릿 완성 전, api 연결 테스트 코드 ----------------
        /*
            # 템플릿 뽑아서 DB에 Problem이 모두 저장된 상태다.
            1. TestPaper Entity 생성 후, id 값 가져오기 -> 저장
            2. Problem Entity 에서 TestPaper id와 동일한 문제 모두 가져오기 -> 저장
            3. probList 반환

            # 현재는 시험지 만들 수 없는 상태니까, 시험지 번호 1로 진행
        */

        Optional<TestPaper> tmpTestPaper = testPaperRepository.findById(1L); // 이거 고쳐야 함... 위에서 나중에 시험지 생성하면 그걸로 하기

        List<Problem> probs = problemRepository.findAllByTestPaper(tmpTestPaper.get());
        if(probs.isEmpty()){
            throw new BusinessException(ErrorCode.NO_EXIST_PROBLEM);
        }
        for(int i = 0; i< probs.size(); i++){
            Problem tmpProb = probs.get(i);
            CreateProbRes createProbRes = new CreateProbRes();

            createProbRes.setTestPaperId(1L);  // 나중에 고쳐야 함!!
            createProbRes.setNum(tmpProb.getProbNum());
            createProbRes.setLevel(tmpProb.getLevel());
            createProbRes.setContent(tmpProb.getContent());
            createProbRes.setExplanation(tmpProb.getExplanation());
            createProbRes.setAnswer(tmpProb.getAnswer());

            probList.add(createProbRes);
        }

        Collections.sort(probList, new ProbNumComparator());

        // 문제 리스트 반환
        return probList;
    }
}

