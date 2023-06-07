package com.onejo.seosuri.service;

import com.onejo.seosuri.controller.dto.problem.ProbRes;
import com.onejo.seosuri.controller.dto.template.TemplateDto;
import com.onejo.seosuri.domain.classification.Category;
import com.onejo.seosuri.domain.classification.CategoryRepository;
import com.onejo.seosuri.domain.classification.CategoryTitle;
import com.onejo.seosuri.domain.problem.*;
import com.onejo.seosuri.domain.testpaper.TestPaper;
import com.onejo.seosuri.domain.testpaper.TestPaperRepository;

import com.onejo.seosuri.domain.word.Word;
import com.onejo.seosuri.domain.word.WordRepository;
import com.onejo.seosuri.domain.word.WordType;
import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.exception.common.ErrorCode;
import com.onejo.seosuri.service.algorithm.ProblemValueStruct;
import com.onejo.seosuri.service.algorithm.problem.CreateAgeProblem;
import com.onejo.seosuri.util.ProbNumComparator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final TestPaperRepository testPaperRepository;
    private final ProblemTemplateRepository problemTemplateRepository;
    private final CategoryRepository categoryRepository;
    private final ProbWordRepository probWordRepository;
    private final WordRepository wordRepository;


    @Transactional
    public List<ProbRes> createProblem(String categoryTitle, String level_){
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
        int level = -1;

        if(level_.equals("하")){
            level = 1;
        }
        else if(level_.equals("중")){
            level = 2;
        }
        else{
            level = 3;
        }
        // ################### test ###################
        System.out.println("testpaper level: " + level);
        // ################### test ###################

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
        if(level == 3){
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
        // 난이도 별 랜덤 인덱스 선택하기 (1: 1~110, 2:111~18260 , 3:18260~517139
        Set<Integer> lowRandNumSet = new HashSet<>();
        Set<Integer> midRandNumSet = new HashSet<>();
        Set<Integer> highRandNumSet = new HashSet<>();
        Random random = new Random();
        while(lowRandNumSet.size() < low){
            int tmp = random.nextInt(110)+1;
            lowRandNumSet.add(tmp);
        }
        while(midRandNumSet.size() < mid){
            int tmp = random.nextInt(18150)+111;
            midRandNumSet.add(tmp);
        }
        while(highRandNumSet.size() < high){
            int tmp = random.nextInt(498880)+18260;
            highRandNumSet.add(tmp);
        }

        List<Integer> lowRandNumList = new ArrayList<>(lowRandNumSet);
        List<Integer> midRandNumList = new ArrayList<>(midRandNumSet);
        List<Integer> highRandNumList = new ArrayList<>(highRandNumSet);

        for(int i=0; i<lowRandNumList.size(); i++){
            Optional<ProblemTemplate> tmpProblemTemplate = problemTemplateRepository.findById((long)lowRandNumList.get(i));
            tmplList1.add(tmpProblemTemplate.get());
        }
        for(int i=0; i<midRandNumList.size(); i++){
            Optional<ProblemTemplate> tmpProblemTemplate = problemTemplateRepository.findById((long)midRandNumList.get(i));
            tmplList2.add(tmpProblemTemplate.get());
        }
        for(int i=0; i<highRandNumList.size(); i++){
            Optional<ProblemTemplate> tmpProblemTemplate = problemTemplateRepository.findById((long)highRandNumList.get(i));
            tmplList3.add(tmpProblemTemplate.get());
        }

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

        // 단어 정보 셋
        // 단어 타입 4개 중 랜덤 하나 선택
        String wordType;
        Random typeRand = new Random();
//        int typeRandResult = typeRand.nextInt(6);
//        if (problemValueStruct.getTemplate_level() == 3) {
//            wordType = "인외";
//        } else {
//            if (typeRandResult == 0) {
//                wordType = "직업";
//            } else if (typeRandResult == 1) {
//                wordType = "가족";
//            } else {
//                wordType = "이름";
//            }
//        }
        wordType = "인외";
        List<Word> tmpWordList = wordRepository.findByType(wordType);
        if(tmpWordList.isEmpty()){
            throw new BusinessException(ErrorCode.NO_EXIST_WORD_TYPE);
        }

        // 카테고리에 따른 CreateProblem 진행
        if(categoryTitle.equals("나이_구하기")){
            for(int i=0; i<tmplList1.size(); i++){
                createAgeProblemPart(testPaperId, tmplList1, i, i+1, tmpWordList);
            }

            for(int i=0; i<tmplList2.size(); i++){
                createAgeProblemPart(testPaperId, tmplList2, i, i+tmplList1.size()+1, tmpWordList);
            }

            for(int i=0; i<tmplList3.size(); i++){
                createAgeProblemPart(testPaperId, tmplList2, i, i+tmplList1.size() + tmplList2.size() +1, tmpWordList);
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

            probRes.setTestPaperId(testPaperId);  // 나중에 고쳐야 함!!
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
        Optional<TestPaper> testPaper = testPaperRepository.findById(testPaperId); // 이거 고쳐야 함... 위에서 나중에 시험지 생성하면 그걸로 하기
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

            probRes.setTestPaperId(testPaperId);  // 나중에 고쳐야 함!!
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

    // 3. 문제 바꾸기 api
//    @Transactional
//    public void


    private void createAgeProblemPart(Long testPaperId, List<ProblemTemplate> tmplList, int i, int probNum, List<Word> tmpWordList) {
        // 문제에 대한 모든 정보 출력
        System.out.println("---------------###################################");
        System.out.println("시험지번호: " + testPaperId);
        System.out.println("문제 템플릿 번호: " + tmplList.get(i).getId());
        System.out.println("문제 번호: " + probNum);
        System.out.println("문제 난이도: " + tmplList.get(i).getLevel());



        // 시험지 객체 찾기
        TestPaper tmpTestPaper = testPaperRepository.findById(testPaperId).get();
        // 템플릿을 TemplateDto로 변경
        TemplateDto templateDto = new TemplateDto(tmplList.get(i));
        // TemplateDto 내용 ProblemValueStruct에 저장
        ProblemValueStruct problemValueStruct = templateDto.setProblemValueStruct();
        // 추가 정보 set
        problemValueStruct.setProblemTemplate(tmplList.get(i));
        problemValueStruct.setTestPaper(tmpTestPaper);
        problemValueStruct.setProbNum((long)(probNum));

        List<Word> wordList = new ArrayList<>();
        // 랜덤으로 목록 섞기
        ArrayList<Integer> random1 = new ArrayList<>();
        for (int j = 0; j < tmpWordList.size(); j++) {
            random1.add(j);
        } // tmpList1 사이즈 만큼 int 값 원소에 넣기
        Collections.shuffle(random1);                            // 랜덤으로 섞기
        for (int j = 0; j < tmpWordList.size(); j++) {
            int index = random1.get(j);
            Word item = tmpWordList.get(index);
            item.setNumStart(1L);
            wordList.add(item);
        }
        problemValueStruct.setWordListDirect(wordList);
        random1.clear();
        wordList.clear();
        // 나이 문제 생성 객체 만들기
        CreateAgeProblem createAgeProblem = new CreateAgeProblem(problemValueStruct);

        createAgeProblem.createProblem(problemValueStruct.getTemplate_level());

        // 문제 생성
        Problem problem = new Problem();
        problem.setTestPaper(problemValueStruct.getTestPaper());
        problem.setProbTemp(problemValueStruct.getProblemTemplate());
        problem.setProbNum(problemValueStruct.getProbNum());
        problem.setLevel("" + problemValueStruct.getTemplate_level());
        problem.setContent(problemValueStruct.getReal_content());
        problem.setExplanation(problemValueStruct.getReal_explanation());
        problem.setAnswer(problemValueStruct.getReal_answer());
        problem.setState("A");

        problemRepository.save(problem);

        // ProbWord 객체 생성
        for (int j = 0; j < problemValueStruct.getWordList().size(); j++) {
            ProbWord probWord = new ProbWord();
            probWord.setPosition("");
            probWord.setWord(problemValueStruct.getWordList().get(j));
            probWord.setProb(problem);
            probWordRepository.save(probWord);
        }

        // 문제 정보 출력
        System.out.println("문제 내용: " + problem.getContent());
        System.out.println("문제 해설: " + problem.getExplanation());
        System.out.println("문제 답: " + problem.getAnswer());
        System.out.println("---------------###################################");
    }
}

