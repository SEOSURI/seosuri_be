package com.onejo.seosuri.service;

import com.onejo.seosuri.controller.dto.problem.ProbRes;
import com.onejo.seosuri.domain.classification.Category;
import com.onejo.seosuri.domain.classification.CategoryRepository;
import com.onejo.seosuri.domain.classification.CategoryTitle;
import com.onejo.seosuri.domain.problem.*;
import com.onejo.seosuri.domain.testpaper.TestPaper;
import com.onejo.seosuri.domain.testpaper.TestPaperRepository;

import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.exception.common.ErrorCode;
import com.onejo.seosuri.service.algorithm.ProblemValueStruct;
import com.onejo.seosuri.service.algorithm.problem.CreateAgeProblem;
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
    public List<ProbRes> createProblem(String categoryTitle, int level){
        // 나이 구하기 문제 알고리즘 -> 실제 문제 생성: 결과: varElementary5th.real_content, real_explanation, real_answer에 저장됨
        // 0-0. 시험지 만들기
        // 0-1. 서비스 단에서 시험지 레벨 받아오면, 시험지 레벨 맞춰서 각 문제 별 난이도 개수 정하기
        // 0-2. 난이도 별로 다음과 같은 과정 반복
        // 1. Create(?)Problem 생성하기
        // 2. 현재 정해진 문제의 level 입력하고 createProblem 호출하기 - 그러면 자동으로 조건 셋팅 될 것임
        // 3. createProblem(조건 여러개 파라미터)에서 템플릿불러오기
        //      1. 문제 레벨 조건, category_id로 불러올거야
        //      2. 조건에 맞는 템플릿이 ArrayList에 쫙 들어옴
        //      3. ArrayList 사이즈 계산해서 그 중 랜덤으로 문제

        // 조건에 맞는 후보 템플릿 저장
        List<ProblemTemplate> tmpList1 = new ArrayList<>();  // 문제 하 템플릿
        List<ProblemTemplate> tmpList2 = new ArrayList<>();  // 문제 중 템플릿
        List<ProblemTemplate> tmpList3 = new ArrayList<>();  // 문제 상 템플릿
        // 랜덤 선택 당한 템플릿 저장
        Long testPaperId;
        List<ProblemTemplate> tmplList1 = new ArrayList<>();  // 문제 하 템플릿
        List<ProblemTemplate> tmplList2 = new ArrayList<>();  // 문제 중 템플릿
        List<ProblemTemplate> tmplList3 = new ArrayList<>();  // 문제 상 템플릿
        List<ProbRes> probList = new ArrayList<>();  // 완성된 문제
        // 기타 필요한 필드
        CategoryTitle categoryTitle_;

        // 0-0. 시험지 만들기
        TestPaper testPaper = new TestPaper();
        if(categoryTitle.equals("나이_구하기")){
            categoryTitle_ = CategoryTitle.AGE;
        }
        else if(categoryTitle.equals("이은_색테이프")){
            categoryTitle_ = CategoryTitle.COLOR_TAPE;
        }
        else if(categoryTitle.equals("어떤수")){
            categoryTitle_ = CategoryTitle.UNKNOWN_NUM;
        }
        else if(categoryTitle.equals("도형_혼합계산_응용")){
            categoryTitle_ = CategoryTitle.GEOMETRY_APP_CALC;
        }
        else{
            throw new BusinessException(ErrorCode.NO_EXIST_CATEGORY_TITLE);
        }
        Optional<Category> tmpCategory = categoryRepository.findByTitle(categoryTitle_);
        if(tmpCategory.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_EXIST_CATEGORY_TITLE);
        }
        testPaper.setCategory(tmpCategory.get());
        testPaper.setLevel(""+level);
        testPaperRepository.save(testPaper);
        testPaperId = testPaperRepository.count();


        // 상(6 3 1) 중(4 4 2) 하(1 4 5)
        // 0-1. 서비스 단에서 시험지 레벨 받아오면, 시험지 레벨 맞춰서 각 문제 별 난이도 개수 정하기
        int high=0, mid=0, low=0;
        if(level == 1){
            high = 6;
            mid = 3;
            low = 1;
        }
        else if(level == 2){
            high = 4;
            mid = 4;
            low = 2;
        }
        else{
            high = 1;
            mid = 4;
            low = 5;
        }

        // 0-2. 난이도 별로 다음과 같은 과정 반복
        // 난이도 별 템플릿 불러오기
        tmpList1 = problemTemplateRepository.findByLevelAndCategory("1", tmpCategory.get());
        tmpList2 = problemTemplateRepository.findByLevelAndCategory("2", tmpCategory.get());
        tmpList3 = problemTemplateRepository.findByLevelAndCategory("3", tmpCategory.get());
        // 각 후보 리스트 사이즈 측정해서 랜덤으로 ArrayList 추출
        ArrayList<Integer> random = new ArrayList<Integer>();

        for(int i=1; i<=tmpList1.size(); i++){ random.add(i); } // tmpList1 사이즈 만큼 int 값 원소에 넣기
        Collections.shuffle(random);                            // 랜덤으로 섞기
        for(int i=0; i<low; i++){
            int id = random.get(i);
            tmplList1.add(tmpList1.get(id));
        }
        random.clear();

        for(int i=1; i<=tmpList2.size(); i++){ random.add(i); } // tmpList1 사이즈 만큼 int 값 원소에 넣기
        Collections.shuffle(random);                            // 랜덤으로 섞기
        for(int i=0; i<mid; i++){
            int id = random.get(i);
            tmplList2.add(tmpList2.get(id));
        }
        random.clear();

        for(int i=1; i<=tmpList3.size(); i++){ random.add(i); } // tmpList1 사이즈 만큼 int 값 원소에 넣기
        Collections.shuffle(random);                            // 랜덤으로 섞기
        for(int i=0; i<high; i++){
            int id = random.get(i);
            tmplList3.add(tmpList3.get(id));
        }
        random.clear();

        ////// ####################### test 용 출력 ##############################
        for(int i=0; i<tmplList1.size(); i++){
            System.out.println("난이도 하 id: " + tmplList1.get(i).getId());
        }

        for(int i=0; i<tmplList2.size(); i++){
            System.out.println("난이도 중 id: " + tmplList2.get(i).getId());
        }

        for(int i=0; i<tmplList3.size(); i++){
            System.out.println("난이도 상 id: " + tmplList3.get(i).getId());
        }
        ////// ####################### test 용 출력 ##############################

        // 카테고리에 따른 CreateProblem 진행
        if(categoryTitle.equals("나이_구하기")){
            for(int i=0; i<tmplList1.size(); i++){
                // 문제 생성 객체 만들기
                ProblemValueStruct problemValueStruct = new ProblemValueStruct();
                CreateAgeProblem createAgeProblem = new CreateAgeProblem(problemValueStruct);
                createAgeProblem.createProblem(level);
            }

            for(int i=0; i<tmplList2.size(); i++){

            }

            for(int i=0; i<tmplList3.size(); i++){

            }
        }
        else if(categoryTitle.equals("어떤수")){
            for(int i=0; i<tmplList1.size(); i++){

            }

            for(int i=0; i<tmplList2.size(); i++){

            }

            for(int i=0; i<tmplList3.size(); i++){

            }
        }
//        else if(categoryTitle.equals("이은_색테이프")){
//            for(int i=0; i<tmplList1.size(); i++){
//
//            }
//
//            for(int i=0; i<tmplList2.size(); i++){
//
//            }
//
//            for(int i=0; i<tmplList3.size(); i++){
//
//            }
//        }
//        else if(categoryTitle.equals("도형_혼합계산_응용")){
//            for(int i=0; i<tmplList1.size(); i++){
//
//            }
//
//            for(int i=0; i<tmplList2.size(); i++){
//
//            }
//
//            for(int i=0; i<tmplList3.size(); i++){
//
//            }
//        }
        else{
            throw new BusinessException(ErrorCode.NO_EXIST_CATEGORY_TITLE);
        }



        // Problem 테이블에서 완성된 문제 가져오기
        Optional<TestPaper> tmpTestPaper = testPaperRepository.findById(testPaperId);

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

