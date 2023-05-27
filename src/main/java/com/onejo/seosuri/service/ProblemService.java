package com.onejo.seosuri.service;

import com.onejo.seosuri.controller.dto.problem.ProbRes;
import com.onejo.seosuri.domain.classification.CategoryRepository;
import com.onejo.seosuri.domain.problem.ProbWordRepository;
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
    private final ProbWordRepository probWordRepository;


    @Transactional
    public List<ProbRes> createProblem(String categoryTitle, String level){

        Long testPaperId = -1L;
        List<String> tmpList = new ArrayList<>();         // 사용할 템플릿
        List<ProbRes> probList = new ArrayList<>();  // 완성된 문제

        // 시험지 만들기
//        TestPaper testPaper = new TestPaper();
//        Optional<Category> optional = categoryRepository.findByCategoryTitle(CategoryTitle.valueOf(categoryTitle));
//        if(optional.isEmpty()) {
//            throw new BusinessException(ErrorCode.NO_EXIST_CATEGORY_TITLE);
//        }
//        testPaper.setCategory(optional.get());
//        testPaperId = testPaper.getId();

        // 시험지 난이도에 따라서 문제 난이도 개수 조절
        // 상(6 3 1) 중(4 4 2) 하(1 4 5)

        //



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
            ProbRes probRes = new ProbRes();

            probRes.setTestPaperId(1L);  // 나중에 고쳐야 함!!
            probRes.setNum(tmpProb.getProbNum());
            probRes.setLevel(tmpProb.getLevel());
            probRes.setContent(tmpProb.getContent());
            probRes.setExplanation(tmpProb.getExplanation());
            probRes.setAnswer(tmpProb.getAnswer());

            probList.add(probRes);
        }

        Collections.sort(probList, new ProbNumComparator());

        // 문제 리스트 반환
        return probList;
    }

    // 2. 문항 삭제 api
    @Transactional
    public List<ProbRes> deleteProb(Long testPaperId, Long probNum){

        List<ProbRes> probList = new ArrayList<>();  // 완성된 문제

//        Optional<TestPaper> testPaper = testPaperRepository.findById(testPaperId);
        Optional<TestPaper> testPaper = testPaperRepository.findById(1L); // 이거 고쳐야 함... 위에서 나중에 시험지 생성하면 그걸로 하기
        if(testPaper.isEmpty()){
            throw new BusinessException(ErrorCode.NO_EXIST_TEST_PAPER);
        }
        Optional<Problem> deleteProblem = problemRepository.findByTestPaperAndProbNum(testPaper.get(), probNum);
        if(deleteProblem.isEmpty()){
            throw new BusinessException(ErrorCode.NO_EXIST_PROBLEM);
        }

        // 1. 문제 state => D로 변경 하는 방식
        deleteProblem.get().setState("D");
        // 2. 그냥 삭제해버리기
//        problemRepository.deleteById(deleteProblem.get().getId());

        // 3. 삭제 후 문제 리스트 불러오기
        List<Problem> probs = problemRepository.findAllByTestPaperAndState(testPaper.get(), "A");
        if(probs.isEmpty()){
            throw new BusinessException(ErrorCode.NO_EXIST_PROBLEM);
        }
        for(int i = 0; i< probs.size(); i++){
            Problem tmpProb = probs.get(i);
            ProbRes probRes = new ProbRes();

            probRes.setTestPaperId(1L);  // 나중에 고쳐야 함!!
            probRes.setNum(tmpProb.getProbNum());
            probRes.setLevel(tmpProb.getLevel());
            probRes.setContent(tmpProb.getContent());
            probRes.setExplanation(tmpProb.getExplanation());
            probRes.setAnswer(tmpProb.getAnswer());

            probList.add(probRes);
        }

        // 4. 정렬
        Collections.sort(probList, new ProbNumComparator());

        // 5. 빈 숫자 수정 및 DB 수정
        for(int i = 0; i<probList.size(); i++){
            Optional<Problem> updateProbNum = problemRepository.findByTestPaperAndProbNum(testPaper.get(), probList.get(i).getNum());
            updateProbNum.get().setProbNum((long)i+1);
            probList.get(i).setNum((long)i+1);
        }

        // 6. A의 개수 + 1 -> 문제 번호로
        deleteProblem.get().setProbNum((long)probList.size() + 1);

        return probList;
    }
}

