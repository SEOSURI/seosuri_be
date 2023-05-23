// 이곳에 문제 생성 알고리즘을 넣어두고, ProblemSerice에서 필요한 method를 호출하면 될 듯 싶습니다..

package com.onejo.seosuri.service.algorithm;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

/*
템플릿 생성 완료

남은 일
0. 난이도 기준 마련
    - 템플릿 난이도
        - 상황 문장 갯수
        - 상황 문장 난이도
            useYear
            useMult
            useAddMinus
        - 상황 문장 셔플 여부
    - 숫자 난이도
        - 자리수 : 한자리, 두자리, 세자리
1. 랜덤 숫자 뽑기 - 무한루프 문제
2. 숫자에 맞는 단어 뽑기
3. 템플릿 -> 실제 문장으로 변경
 */

public class Elementary5th {
    Random random;
    static final int PLUS_SIGN = 0;
    static final int MINUS_SIGN = 1;

    // 상황 문장 id
    static final int AGE_CATEGORY_ID_YX = 0;
    static final int AGE_CATEGORY_ID_SUM_DIFFERENCE = 1;
    static final int AGE_PROB_VAR_NUM_PER_SENTENCE = 4;     // var1, var2, year1, year2


    // 변수명 string 규칙
    static final String NAME_VAR_STR = "name_var";    // age 변수 : {age0}, {age1}, {age2}, ...
    static final String NAME_STR = "name";  // name 변수 : {name0}, {name1}, {name2}, ...
    static final String VAR_STR = "var";    // 상수 변수 : {var0}, {var1}, {var2}, ...
    static final String YEAR_STR = "year";  // year 변수
    static final String VAR_START = "{";
    static final String VAR_END = "}";
    static final String EXPRESSION_START = "[";
    static final String EXPRESSION_END = "]";
    static final String BRACKET_START = "(";    // expression 내부에서 사용하는 괄호(시작)
    static final String BRACKET_END = ")";      // expression 내부에서 사용하는 괄호(끝)
    static final String PLUS_SYM = "+";
    static final String MINUS_SYM = "-";
    static final String MULT_SYM = "*";
    static final String DIVIDE_SYM = "/";
    static final String EQUAL_SYM = "=";
    static final String BLANK_SYM = " ";
    static final String EQUAL_BLANK_SYM = "\n= ";
    static final String PLUS_BLANK_STR = " + ";
    static final String MINUS_BLANK_STR = " - ";
    static final String MULT_BLANK_STR = " * ";
    static final String DIVIDE_BLANK_STR = " / ";


    // string tokens
    static final String blank_token = " ";
    static final String ui_token = "의";
    static final String gwa_token = "과";
    static final String wa_token = "와";
    static final String neun_token = "는";
    static final String eun_token = "은";
    static final String eul_token = "을";
    static final String reul_token = "를";
    static final String eseo_token = "에서";

    static final String bae_token = "배 한 것";
    static final String than_token = "보다";
    static final String same_token = "같습니다.";
    static final String more_amount_token = "많습니다.";
    static final String less_amount_token = "적습니다.";
    static final String sum_token = "합한";
    static final String difference_token = "뺀";
    static final String value_token = "값";

    static final String age_category_token = "나이";
    static final String age_unit_token = "살";
    static final String year_unit_token = "년";
    static final String time_after_token = "후";
    static final String time_before_token = "전";
    //String time_past_token = "년 후";

    private static final int SENTENCE_CATEGORY_NUM = 2;  // 상황 문장 유형 갯수

    private VarElementary5th varElementary5th = new VarElementary5th();


    // 나이 구하기 문제 알고리즘 -> 실제 문제 생성: 결과: varElementary5th.real_content, real_explanation, real_answer에 저장됨
    public void ageProblem(int level){
         /*
        DB에서 가져올 값
        sentence_category_id_ls
        content_template
        explanation_template
        answer_template
        name_ls
        name_var_min_value_ls
        name_var_max_value_ls
         */
        int prob_sentence_num = level;  // 상황 문장 갯수 - 문제 난이도에 따라 값 달라짐

        random = new Random(); //랜덤 객체 생성(디폴트 시드값 : 현재시간)
        random.setSeed(System.currentTimeMillis()); //시드값 설정을 따로 할수도 있음

        int name_var_num = prob_sentence_num + 1;
        int var_num = prob_sentence_num * AGE_PROB_VAR_NUM_PER_SENTENCE;
        varElementary5th.sentence_category_id_ls = new int[prob_sentence_num];    // 각 상황문장이 어떤 유형의 문장인지를 저장한 배열
        // DB에서 sentence_category_id_ls 가져오기!!!!

        // DB에서 템플릿 가져오기
        // 난이도로 템플릿 고르고 그 중에서 랜덤 뽑기!!!
        varElementary5th.content_template = "내용";
        varElementary5th.explanation_template = "설명";
        varElementary5th.answer_template = "답";

        // name 뽑기, name_var 범위 설정  -> 문제 숫자 값 랜덤 뽑기 시 이용됨
        // DB에서 name, name_var범위 값 가져오게 수정해야!!!
        setNameAndNameVarRange(name_var_num);          // name_ls, name_var_min_value_ls, name_var_max_value_ls 설정

        // 상수 var 범위 설정 -> 문제 숫자 값 랜덤 뽑기 시 이용됨
        setVarMinMaxLs(prob_sentence_num, AGE_PROB_VAR_NUM_PER_SENTENCE);    // var_min_value_ls, var_max_value_ls 설정

        // 상수 var, name_var 랜덤 뽑기 -> 숫자 변경 시 여기부터 다시 실행하면 됨!!!
        // ageProblem의 name_var_num = 4, var_num_per_sentence = level
        getRandomAgeValue(name_var_num, AGE_PROB_VAR_NUM_PER_SENTENCE);  // name_var_ls, var_ls

        // template -> problem
        String[] real_prob = templateToProblem(varElementary5th.name_ls, varElementary5th.name_var_ls, varElementary5th.var_ls,
                varElementary5th.content_template, varElementary5th.explanation_template, varElementary5th.answer_template);

        varElementary5th.real_content = real_prob[0];
        varElementary5th.real_explanation = real_prob[1];
        varElementary5th.real_answer = real_prob[2];
        /*
        System.out.println("\n\n-----------------------------------------------------");
        System.out.println(real_prob[0]);   // real_content
        System.out.println("\n\n-----------------------------------------------------");
        System.out.println(real_prob[1]);   // real_explanation
        System.out.println("\n\n-----------------------------------------------------");
        System.out.println(real_prob[2]);   // real_answer
         */
    }

    // 구버전 - 디버깅용으로 실제 문제 하나 만들어보는 함수였음~~~
    public void realAgeProblem(int level){
        int var_num_per_sentence = 4;   // var1, var2, year1, year2
        int prob_sentence_num = level;  // 상황 문장 갯수 - 문제 난이도에 따라 값 달라짐

        random = new Random(); //랜덤 객체 생성(디폴트 시드값 : 현재시간)
        random.setSeed(System.currentTimeMillis()); //시드값 설정을 따로 할수도 있음

        int name_var_num = prob_sentence_num + 1;
        int var_num = prob_sentence_num * var_num_per_sentence;
        varElementary5th.sentence_category_id_ls = new int[prob_sentence_num];    // 각 상황문장이 어떤 유형의 문장인지를 저장한 배열
        for(int i = 0; i < prob_sentence_num; i++){
            varElementary5th.sentence_category_id_ls[i] = random.nextInt(SENTENCE_CATEGORY_NUM);
        }
        int answer_inx = random.nextInt(name_var_num);  // 구하는 나이의 인덱스
        int condition_inx = (random.nextInt(name_var_num) + answer_inx + 1) % name_var_num;   // 조건으로 값이 주어진 나이의 인덱스, answer_inx와 다른 인덱스가 되도록 설정

        setUseBooleans(level, var_num_per_sentence);

        varElementary5th.var_sign_ls = new int[var_num];
        for(int i = 0; i < var_num; i++){
            varElementary5th.var_sign_ls[i] = random.nextInt(2);
        }

        // ageProblemTemplate() 실행 결과 class 변수 setting 됨
        ageProblemTemplate(prob_sentence_num, var_num_per_sentence, answer_inx, condition_inx); // 문제 템플릿 생성

        // for testing
        printTemplate();


        ///////////////////////////////////////////////////////////////////////////////
        // 실제 문제 생성

        // name 뽑기, 상수 var, name_var 범위 설정
        setNameAndNameVarRange(name_var_num);
        setVarMinMaxLs(prob_sentence_num, var_num_per_sentence);

        // 상수 var, name_var 랜덤 뽑기
        getRandomAgeValue(name_var_num, var_num_per_sentence);

        // template -> problem
        String[] real_prob = templateToProblem(varElementary5th.name_ls, varElementary5th.name_var_ls, varElementary5th.var_ls,
                varElementary5th.content_template, varElementary5th.explanation_template, varElementary5th.answer_template);

        System.out.println("\n\n-----------------------------------------------------");
        System.out.println(real_prob[0]);   // real_content
        System.out.println("\n\n-----------------------------------------------------");
        System.out.println(real_prob[1]);   // real_explanation
        System.out.println("\n\n-----------------------------------------------------");
        System.out.println(real_prob[2]);   // real_answer
        // return new String[] {real_content, real_explanation, real_answer};

    }


    public void saveAgeProblemTemplates(){

        boolean[] tf_set = new boolean[] {true, false};
        int case_id = 0;
        // cond_inx
        for(int prob_sentence_num: new int[] {0, 1, 2}) {
            int name_var_num = prob_sentence_num + 1;
            int var_num = prob_sentence_num * AGE_PROB_VAR_NUM_PER_SENTENCE;
            int answer_inx = random.nextInt(name_var_num);  // 구하는 나이의 인덱스
            int condition_inx = (random.nextInt(name_var_num) + answer_inx + 1) % name_var_num;   // 조건으로 값이 주어진 나이의 인덱스, answer_inx와 다른 인덱스가 되도록 설정
            /*
            create_age_sentence(varElementary5th.sentence_category_id_ls[i], i , var_num_per_sentence, cond_inx_for_sentence,
                    varElementary5th.useYear_ls[i], varElementary5th.useMult_ls[i], varElementary5th.useAddMinus_ls[i],
                    varElementary5th.var_sign_ls[var1_index+1], varElementary5th.var_sign_ls[var1_index+2], varElementary5th.var_sign_ls[var1_index+3]);  // sentence_ls[i] = {content, explanation}
             */
            // useYear_ls, useMult_ls, useAddMinus_ls, var_sign 가능한 모든 조합으로 template 생성 후 DB 저장
            boolean[] useYear_ls = new boolean[prob_sentence_num];
            boolean[] useMult_ls = new boolean[prob_sentence_num];
            boolean[] useAddMinus_ls = new boolean[prob_sentence_num];
            for (boolean useYear : useYear_ls)
                for (boolean useMult : useMult_ls)
                    for (boolean useAddMinus : useAddMinus_ls)
                        for (int year1_sign : new int[]{0, 1})
                            for (int year2_sign : new int[]{0, 1})
                                for (int var_sign : new int[]{0, 1}) {
                                    ageProblemTemplate(prob_sentence_num, AGE_PROB_VAR_NUM_PER_SENTENCE, condition_inx, answer_inx);
                                    //DB에 저장
                                }
        }

    }
    // template 생성 -> varElementary5th.content, explanation, answer에 결과 저장됨
    public void ageProblemTemplate(int prob_sentence_num, int var_num_per_sentence,
                           int answer_inx, int condition_inx) {
        ////////////////////////////////////////////////////////////////////////////////////////////////
        // 문장 생성

        // condition 문장 생성
        String condition = "이때, {"+NAME_STR+condition_inx+"}의 나이는 {"+ NAME_VAR_STR +condition_inx+"}살 입니다.";

        // question 문장 생성
        String question = "그렇다면 {"+NAME_STR+answer_inx+"}의 나이는 몇 살입니까?";

        // answer 문장 생성
        String answer = "{"+ NAME_VAR_STR +answer_inx+"}살";

        // 상황 문장 생성 : {content, explanation}
        String[][] sentence_ls = new String[prob_sentence_num][2];
        // condition index for sentence
        //      0~cond_inx -> age2 given
        //      cond_inx+1~ -> age1 given
        int cond_inx_for_sentence = 1;  // age1, age2 중 age2가 given
        if(prob_sentence_num==1)    cond_inx_for_sentence = condition_inx;
        for(int i = 0; i < condition_inx; i++){ // 0~cond_inx -> age2 given
            int var1_index = i * var_num_per_sentence;
            sentence_ls[i] = create_age_sentence(varElementary5th.sentence_category_id_ls[i], i , var_num_per_sentence, cond_inx_for_sentence,
                    varElementary5th.useYear_ls[i], varElementary5th.useMult_ls[i], varElementary5th.useAddMinus_ls[i],
                    varElementary5th.var_sign_ls[var1_index+1], varElementary5th.var_sign_ls[var1_index+2], varElementary5th.var_sign_ls[var1_index+3]);  // sentence_ls[i] = {content, explanation}
        }
        cond_inx_for_sentence = 0;  // age1, age2 중 age1가 given
        for(int i = condition_inx; i < prob_sentence_num; i++){ // cond_inx+1~ -> age1 given
            int var1_index = i * var_num_per_sentence;
            sentence_ls[i] = create_age_sentence(varElementary5th.sentence_category_id_ls[i], i , var_num_per_sentence, cond_inx_for_sentence,
                    varElementary5th.useYear_ls[i], varElementary5th.useMult_ls[i], varElementary5th.useAddMinus_ls[i],
                    varElementary5th.var_sign_ls[var1_index+1], varElementary5th.var_sign_ls[var1_index+2], varElementary5th.var_sign_ls[var1_index+3]);  // sentence_ls[i] = {content, explanation}
        }

        /////////////////////////////////////////////////////////////////////////////////////////////////////
        // 상황 문장 연결

        // content : sentences + condition + question
        String content = "";
        for(int i = 0; i < sentence_ls.length; i++){ // 상황 문장 content - 순서 섞으면 난이도 올라감
            content += sentence_ls[i][0] + "\n";
        }
        content += condition + "\n" + question;

        // explanation
        // condition_inx = 0 : 0->1->...
        // else: condition_inx+1 -> condition_inx-1 -> ... -> 끝, condition_inx-1 -> condition_inx-2 -> ... -> 0 순서로 연결
        String explanation = "";
        int start_index = (condition_inx + sentence_ls.length - 1) % sentence_ls.length;
        if(condition_inx == 0)  start_index = 0;
        for(int i = start_index; i < sentence_ls.length; i++){    // condition_inx  ~  끝
            explanation += sentence_ls[i][1] + "\n\n";   // 상황 문장 explanation
        }
        for(int i = start_index - 1; i >= 0; i--){ // condition_inx-1 ~ 0
            explanation += sentence_ls[i][1] + "\n\n";   // 상황 문장 exlanation
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        // 결과
        varElementary5th.content_template = content;
        varElementary5th.explanation_template = explanation;
        varElementary5th.answer_template = answer;

    }

    // 이은 색테이프 문제 알고리즘
    public void colorTapeProblem(int level) {}

    // 어떤 수 문제 알고리즘
    public void anyNumberProblem(int level) {

    }

    // 도형에서의 혼합 계산 응용 알고리즘
    public void geometryCalculation() {}


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 설정값 setting
    //

    // 난이도에 따른 useBoolean 변수들 (useYear, useMult, useAddMinus) 설정값
    private void setUseBooleans(int level, int var_num_per_sentence){
        int prob_sentence_num = level;  // 상황 문장 갯수 - 문제 난이도에 따라 값 달라짐

        varElementary5th.useYear_ls = new boolean[prob_sentence_num];
        varElementary5th.useMult_ls = new boolean[prob_sentence_num];
        varElementary5th.useAddMinus_ls = new boolean[prob_sentence_num];

        for(int i = 1; i < prob_sentence_num; i++){
            varElementary5th.useYear_ls[i] = random.nextBoolean();
            varElementary5th.useMult_ls[i] = random.nextBoolean();
            varElementary5th.useAddMinus_ls[i] = random.nextBoolean();
        }

        /*
        level -> prob_sentence_num 결정
        상(3) - prob_sentence_num = 3, useYear = true, t/f, t/f,   useMult = t, t/f, t/f,   useAddMinus = t, t/f, t/f
        중(2) - prob_sentence_num = 2, useYear = true, false,      useMult = t, t/f,        useAddMinus = t, t/f
        하(1) - prob_sentence_num = 1, useYear = false,            useMult = t/f,           useAddMinus = t/f

        if(level == 1){ // 난이도 하
            varElementary5th.useYear_ls[0] = false;
            varElementary5th.useMult_ls[0] = random.nextBoolean();
            varElementary5th.useAddMinus_ls[0] = random.nextBoolean();
        } else{
            varElementary5th.useYear_ls[0] = true;
            varElementary5th.useMult_ls[0] = true;
            varElementary5th.useAddMinus_ls[0] = true;
            for(int i = 1; i < prob_sentence_num; i++){
                varElementary5th.useYear_ls[i] = random.nextBoolean();
                varElementary5th.useMult_ls[i] = random.nextBoolean();
                varElementary5th.useAddMinus_ls[i] = random.nextBoolean();
            }
        }
        */
    }

    // var_min_value_ls, var_max_value_ls 설정
    private void setVarMinMaxLs(int prob_sentence_num, int num_var_per_sentence){
        varElementary5th.var_min_value_ls = new int[prob_sentence_num * num_var_per_sentence];
        varElementary5th.var_max_value_ls = new int[prob_sentence_num * num_var_per_sentence];
        for(int i = 0; i < prob_sentence_num; i++){
            int var1_index = i * num_var_per_sentence;
            if(varElementary5th.useMult_ls[i]) {    // var1
                varElementary5th.var_min_value_ls[var1_index] = 2;
                varElementary5th.var_max_value_ls[var1_index] = 5;
            } else{ // 0~100 =  0~100 * 2 - 100 // 99 *
                varElementary5th.var_min_value_ls[var1_index] = 1;
                varElementary5th.var_max_value_ls[var1_index] = 1;
            }
            if(varElementary5th.useAddMinus_ls[i]){ // var2
                varElementary5th.var_min_value_ls[var1_index+1] = 1;
                varElementary5th.var_max_value_ls[var1_index+1] = 20;
            } else{
                varElementary5th.var_min_value_ls[var1_index+1] = 0;
                varElementary5th.var_max_value_ls[var1_index+1] = 0;
            }
            if(varElementary5th.useYear_ls[i]){
                varElementary5th.var_min_value_ls[var1_index+2] = 1;
                varElementary5th.var_max_value_ls[var1_index+2] = 100;
                varElementary5th.var_min_value_ls[var1_index+3] = 1;
                varElementary5th.var_max_value_ls[var1_index+3] = 100;
            } else{
                varElementary5th.var_min_value_ls[var1_index+2] = 0;
                varElementary5th.var_max_value_ls[var1_index+2] = 0;
                varElementary5th.var_min_value_ls[var1_index+3] = 0;
                varElementary5th.var_max_value_ls[var1_index+3] = 0;
            }
        }
    }

    // name_ls, name_var_min_value_ls, name_var_max_value_ls 설정
    private void setNameAndNameVarRange(int name_var_num){
        varElementary5th.name_var_min_value_ls = new int[name_var_num];
        varElementary5th.name_var_max_value_ls = new int[name_var_num];
        varElementary5th.name_ls = new String[name_var_num];
        for(int i = 0; i < name_var_num; i++){      // DB 연결 -> DB에서 값 받아와야
            varElementary5th.name_var_min_value_ls[i] = 10;
            varElementary5th.name_var_max_value_ls[i] = 100;
            varElementary5th.name_ls[i] = i+"사람"+i;
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 상황 문장 생성
    //

    // 나이 문제 상황 문장 생성
    // create_age_sentence(유형id, 값 참조 시작하는 인덱스) ex) index = 2 -> name2, name3, age2, age3, var2, var3 사용
    private String[] create_age_sentence(int category_id, int index, int var_num_per_sentence, int cond_inx,
                                         boolean useYear, boolean useMult, boolean useAddMinus,
                                         int var_sign, int year1_sign, int year2_sign){
        String name_category_token = age_category_token; // "의 나이"
        String name_unit_token = age_unit_token;
        String var34_unit_token = year_unit_token;
        String after_str_token = time_after_token;
        String before_str_token = time_before_token;

        if(category_id == AGE_CATEGORY_ID_YX){
            return create_sentence_yx(index, var_num_per_sentence, cond_inx,
                    useYear, useMult, useAddMinus,
                    var_sign, year1_sign, year2_sign,
                    name_category_token, name_unit_token, var34_unit_token, after_str_token, before_str_token);               // {content, explanation, sign}
        } else if (category_id == AGE_CATEGORY_ID_SUM_DIFFERENCE){
            return create_sentence_sum_diff(index, var_num_per_sentence, cond_inx,
                    var_sign,
                    name_category_token, name_unit_token);   // {content, explanation, sign}
        } else{
            return null;
        }
    }

    // (name_var1 +- var3) = (name_var2 +- var4) * var1 +- var2
    public String[] create_sentence_yx(int ls_index, int var_num_per_sentence, int cond_inx,
                                       boolean useYear, boolean useMult, boolean useAddMinus,
                                       int var2_sign, int year1_sign, int year2_sign,
                                       String name_category_token, String name_unit_token, String var34_unit_token, String after_str_token, String before_str_token){
        ArrayList<String> content_ls = new ArrayList<>(1);
        ArrayList<String> explanation_ls = new ArrayList<>(10);

        // index
        int index1 = ls_index;
        int index2 = index1 + 1;
        int var_index1 = ls_index * var_num_per_sentence;
        int var_index2 = var_index1 + 1;
        int year1_index = var_index1 + 2;
        int year2_index = var_index1 + 3;


        /*
        // string token used
        String blank_token = " ";
        String ui_token = "의";
        String gwa_token = "과";
        String wa_token = "와";
        String neun_token = "는";
        String eun_token = "은";
        String age_unit_token = "살";
        String bae_token = "배 한 것";
        String than_token = "보다";
        String same_token = "같습니다.";
        String more_amount_token = "많습니다.";
        String less_amount_token = "적습니다.";

        String name_to_age_token = "의 나이";
        String time_past_token = "년 후";
         */

        // token 조합
        String name1_token = VAR_START + NAME_STR + index1 + VAR_END;
        String name2_token = VAR_START + NAME_STR + index2 + VAR_END;
        String name1_with_category_token = name1_token + ui_token + blank_token +  name_category_token;
        String name2_with_category_token = name2_token + ui_token + blank_token +  name_category_token;
        String name_var1_token = VAR_START + NAME_VAR_STR + index1 + VAR_END;
        String name_var2_token = VAR_START + NAME_VAR_STR + index2 + VAR_END;
        String var1_token = VAR_START + VAR_STR + var_index1 + VAR_END;
        String var2_token = VAR_START + VAR_STR + var_index2 + VAR_END;
        String var3_token = VAR_START + VAR_STR + year1_index + VAR_END;    // year1
        String var4_token = VAR_START + VAR_STR + year2_index + VAR_END;   // year2

        String name1_add_str_token = "", name2_add_str_token = "";
        if(useYear){
            if(year1_sign == PLUS_SIGN){
                name1_add_str_token = var3_token + var34_unit_token + blank_token + after_str_token + blank_token; // "{year1}년 후 "
            } else if(year1_sign == MINUS_SIGN){
                name1_add_str_token = var3_token + var34_unit_token + blank_token + before_str_token + blank_token; // "{year1}년 전 "
            } else{
                System.out.println("ERROR:: invalid year sign");
            }
            if(year2_sign == PLUS_SIGN){
                name2_add_str_token = var4_token + var34_unit_token + blank_token + after_str_token + blank_token; // "{year2}년 후 "
            } else if(year2_sign == MINUS_SIGN){
                name2_add_str_token = var4_token + var34_unit_token + blank_token + before_str_token + blank_token; // "{year2}년 전 "
            } else{
                System.out.println("ERROR:: invalid year sign");
            }
        }


        // sign
        String var2_sign_token = "", var2_sign_blank_token = "";
        String var2_inv_sign_token = "", var2_inv_sign_blank_token = "";
        if(var2_sign == PLUS_SIGN){
            var2_sign_token = PLUS_SYM;  var2_sign_blank_token = PLUS_BLANK_STR;
            var2_inv_sign_token = MINUS_SYM; var2_inv_sign_blank_token = MINUS_BLANK_STR;
        } else if(var2_sign == MINUS_SIGN){
            var2_sign_token = MINUS_SYM; var2_sign_blank_token = MINUS_BLANK_STR;
            var2_inv_sign_token = PLUS_SYM;  var2_inv_sign_blank_token = PLUS_BLANK_STR;
        } else{
            System.out.println("ERROR:: SIGN VALUE ERROR");
            return null;
        }

        String var3_sign_token = PLUS_SYM, var3_inv_sign_token = MINUS_SYM;
        String var3_sign_blank_token = PLUS_BLANK_STR, var3_inv_sign_blank_token = MINUS_BLANK_STR;
        if(year1_sign == MINUS_SIGN) {
            var3_sign_token = MINUS_SYM;    var3_inv_sign_token = PLUS_SYM;
            var3_sign_blank_token = MINUS_BLANK_STR;    var3_inv_sign_blank_token = PLUS_BLANK_STR;
        }

        String var4_sign_token = PLUS_SYM, var4_inv_sign_token = MINUS_SYM;
        String var4_sign_blank_token = PLUS_BLANK_STR, var4_inv_sign_blank_token = MINUS_BLANK_STR;
        if(year2_sign == MINUS_SIGN) {
            var4_sign_token = MINUS_SYM;    var4_inv_sign_token = PLUS_SYM;
            var4_sign_blank_token = MINUS_BLANK_STR;    var4_inv_sign_blank_token = PLUS_BLANK_STR;
        }


        // content용 토큰
        String c_mult_token = ui_token + blank_token + var1_token + bae_token;  //"의 {var1}배 한 것"
        String c_mult_end_token = gwa_token + blank_token + same_token; //"과 같습니다.";
        String c_mult_end_wa_token = wa_token + blank_token + same_token; //"와 같습니다.";
        String c_pm_token = than_token + blank_token + var2_token + name_unit_token + blank_token;   //"보다 " + var2_token + "살 ";

        // content
        String content="";
        content = name1_add_str_token + name1_with_category_token + neun_token + blank_token + name2_add_str_token + name2_with_category_token;   // {name1}의 나이는 {name2}의 나이
        if(useMult){
            if(useAddMinus == false){   // 의 {var1}배 한 것과 같습니다.
                content += c_mult_token + c_mult_end_token;
            } else if(var2_sign == PLUS_SIGN){    // 의 {var1}배한 것보다 {var2}살 많습니다.
                content += c_mult_token + c_pm_token + more_amount_token;
            } else if(var2_sign == MINUS_SIGN){   // 의 {var1}배한 것보다 {var2}살 적습니다.
                content += c_mult_token + c_pm_token + less_amount_token;
            } else{
                content = "var2_sign value error\n";
            }
        } else{
            if(useAddMinus == false){   // 와 같습니다.
                content += c_mult_end_wa_token;
            } else if(var2_sign == PLUS_SIGN){   // 보다 {var2}살 많습니다.
                content += c_pm_token + more_amount_token;
            } else if(var2_sign == MINUS_SIGN){   // 보다 {var2}살 적습니다.
                content += c_pm_token + less_amount_token;
            } else{
                content = "var2_sign value error\n";
            }
        }
        content_ls.add(content);

        content = content_ls.get(0);
        for(int i = 1; i < content_ls.size(); i++){
            content += "\n" + content_ls.get(i);
        }


        // explanation token
        /*
        String ex_age_after_year_token = name1_add_str_token +"{"+NAME_STR+"%d}의 나이 = {"+AGE_STR+"%d} + {"+YEAR_STR+"} = [{"
                +AGE_STR+"%d}+{"+YEAR_STR+"}]";
        */
        String mult_blank_var1_token = MULT_BLANK_STR + var1_token;
        String mult_var1_token = MULT_SYM + var1_token;
        if(useMult == false) {
            mult_blank_var1_token = "";
            mult_var1_token = "";
        }
        String inv_add_minus_blank_var2_token = var2_inv_sign_blank_token + var2_token;
        String inv_add_minus_var2_token = var2_inv_sign_token + var2_token;
        String add_minus_blank_var2_token = var2_sign_blank_token + var2_token;
        String add_minus_var2_token = var2_sign_token + var2_token;
        if(useAddMinus == false){
            add_minus_blank_var2_token = "";
            add_minus_var2_token = "";
            inv_add_minus_blank_var2_token = "";
            inv_add_minus_var2_token = "";
        }
        String expr_add_minus_blank_var4_token = var4_sign_blank_token + var4_token;
        String expr_add_minus_blank_var3_token = var3_sign_blank_token + var3_token;
        String expr_add_minus_var4_token = var4_sign_token + var4_token;
        String expr_add_minus_var3_token = var3_sign_token + var3_token;
        if(useYear == false){
            expr_add_minus_var3_token = "";
            expr_add_minus_var4_token = "";
            expr_add_minus_blank_var3_token = "";
            expr_add_minus_blank_var4_token = "";
        }

        String ex_expression_str_start_token = "\"" + content + "\"라는 문장을 식으로 바꾸면";
        String ex_expression_token = name1_add_str_token + name1_with_category_token
                + EQUAL_BLANK_SYM + name2_add_str_token + name2_with_category_token + mult_blank_var1_token + add_minus_blank_var2_token;
        String ex_expression_str_end_token = "가 됩니다.";

        String ex_explain_start_token = "이제 이 식을 다음과 같은 순서로 풀 수 있습니다.";

        // name_var -> name_var +- year
        String ex_name_var1_after_year_token = name1_add_str_token + name1_with_category_token
                + EQUAL_BLANK_SYM + name_var1_token + expr_add_minus_blank_var3_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + name_var1_token + expr_add_minus_var3_token + EXPRESSION_END;
        String ex_name_var2_after_year_token = name2_add_str_token + name2_with_category_token
                + EQUAL_BLANK_SYM + name_var2_token + expr_add_minus_blank_var4_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + name_var2_token + expr_add_minus_var4_token + EXPRESSION_END;

        // name_var +- year -> name_var
        String ex_after_year_to_name_var1_token = name1_with_category_token
                + EQUAL_BLANK_SYM + name1_add_str_token + name1_with_category_token + var3_inv_sign_blank_token + var3_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var2_token + var4_inv_sign_token + var4_token + BRACKET_END + MULT_SYM + var1_token + var2_sign_token + var2_token + EXPRESSION_END + var3_inv_sign_blank_token + var3_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var2_token + var4_inv_sign_token + var4_token + BRACKET_END + MULT_SYM + var1_token + var2_sign_token + var2_token + var3_inv_sign_token + var3_token + EXPRESSION_END;
        String ex_after_year_to_name_var2_token = name2_with_category_token
                + EQUAL_BLANK_SYM + name2_add_str_token + name2_with_category_token + var4_inv_sign_blank_token + var4_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var1_token + var3_inv_sign_token + var3_token + MINUS_SYM + var2_token + BRACKET_END + DIVIDE_SYM + var1_token + EXPRESSION_END + var4_inv_sign_blank_token + var4_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var1_token + var3_inv_sign_token + var3_token + MINUS_SYM + var2_token + BRACKET_END + DIVIDE_SYM + var1_token + var4_inv_sign_token + var4_token + EXPRESSION_END;


        // (%s%s의 나이) = (%s%s의 나이) * %d %s %d = %d * %d %s %d = %d
        // y년후 name1의 나이 = y년후 name2의나이 * var1 +- var2 = [age2+year] * var1 +- var2 = age1
        String ex_cond2_compute_token = name1_add_str_token + name1_with_category_token
                + EQUAL_BLANK_SYM + name2_add_str_token + name2_with_category_token + mult_blank_var1_token + add_minus_blank_var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + name_var2_token + expr_add_minus_var4_token + EXPRESSION_END + mult_blank_var1_token + add_minus_blank_var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var2_token + expr_add_minus_var4_token + BRACKET_END + mult_var1_token + add_minus_var2_token + EXPRESSION_END;

        // if var2 != 0
        // (year년 후 name1의 나이) -+ var2 = age1 + year -+ var2
        String ex_cond1_compute_b4_divide_token = name1_add_str_token + name1_with_category_token + inv_add_minus_blank_var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + name_var1_token + expr_add_minus_var3_token + inv_add_minus_var2_token + EXPRESSION_END;

        // if var1 != 1
        //"(%s%s의 나이) = {age1 + year -+ var2} / var1 = %d\n"
        String ex_cond1_compute_divide_token = name2_add_str_token + name2_with_category_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + name_var1_token + expr_add_minus_var3_token + inv_add_minus_var2_token + EXPRESSION_END + DIVIDE_BLANK_STR + var1_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var1_token + expr_add_minus_var3_token + inv_add_minus_var2_token + BRACKET_END + DIVIDE_SYM + var1_token + EXPRESSION_END;

        // explanation
        String explanation="";

        explanation_ls.add(ex_expression_str_start_token);
        explanation_ls.add(ex_expression_token);
        explanation_ls.add(ex_expression_str_end_token);

        explanation_ls.add(ex_explain_start_token);
        if(cond_inx == 0){  // age1 given, find age2
            explanation_ls.add(ex_name_var1_after_year_token);
            if(useAddMinus) {   //
                explanation_ls.add(ex_cond1_compute_b4_divide_token);
            }
            if(useMult) {
                explanation_ls.add(ex_cond1_compute_divide_token);
            }
            explanation_ls.add(ex_after_year_to_name_var2_token);
        } else{ // age2 given, find age1
            explanation_ls.add(ex_name_var2_after_year_token);
            explanation_ls.add(ex_cond2_compute_token);
            explanation_ls.add(ex_after_year_to_name_var1_token);
        }

        explanation = explanation_ls.get(0);
        for(int i = 1; i < explanation_ls.size(); i++){
            explanation += "\n" + explanation_ls.get(i);
        }
        return new String[] {content, explanation};     // {content, explanation}
    }

    // name_var1 +- name_var2 = var1
    public String[] create_sentence_sum_diff(int ls_index, int var_num_per_sentence, int cond_inx, int sign,
                                             String name_category_token,
                                             String name_unit_token){

        // index
        int index1 = ls_index;
        int index2 = ls_index + 1;
        int var_index = ls_index * var_num_per_sentence;

        /* string tokens
        String blank_token = " ";
        String ui_token = "의";
        String gwa_token = "과";
        String wa_token = "와";
        String neun_token = "는";
        String eun_token = "은";
        String eul_token = "을";
        String reul_token = "를";
        String eseo_token = "에서";

        String age_unit_token = "살";
        String bae_token = "배 한 것";
        String than_token = "보다";
        String same_token = "같습니다.";
        String more_amount_token = "많습니다.";
        String less_amount_token = "적습니다.";
        String sum_token = "합한";
        String difference_token = "뺀";
        String value_token = "값";
        String name_to_age_token = "의 나이";
        String time_past_token = "년 후";
         */

        String name1_token = VAR_START + NAME_STR + index1 + VAR_END;
        String name2_token = VAR_START + NAME_STR + index2 + VAR_END;
        String name_var1_token = VAR_START + NAME_VAR_STR + index1 + VAR_END;
        String name_var2_token = VAR_START + NAME_VAR_STR + index2 + VAR_END;
        String var1_token = VAR_START + VAR_STR + var_index + VAR_END;

        String name1_with_category_token = name1_token + ui_token + blank_token + name_category_token;    // "{name1}의 나이"
        String name2_with_category_token = name2_token + ui_token + blank_token + name_category_token;    // "{name2}의 나이"

        // content
        String content="";
        String sum_start_str_token = name1_with_category_token + wa_token + blank_token + name2_with_category_token + reul_token + blank_token;
        String diff_start_str_token = name1_with_category_token + eseo_token + blank_token + name2_with_category_token + reul_token + blank_token;
        String content_end_token = value_token + eun_token + blank_token + var1_token + wa_token + blank_token + same_token;
        if(sign == PLUS_SIGN){
            content =  sum_start_str_token + sum_token + blank_token + content_end_token;
        } else{
            content = diff_start_str_token + difference_token + blank_token + content_end_token;
        }

        // sign
        String sign_token = "", sign_blank_token="", inv_sign_token = "", inv_sign_blank_token="";
        if(sign== PLUS_SIGN) {
            sign_token = PLUS_SYM;  sign_blank_token = PLUS_BLANK_STR;
            inv_sign_token = MINUS_SYM; inv_sign_blank_token = MINUS_BLANK_STR;
        }
        else if(sign== MINUS_SIGN) {
            sign_token = MINUS_SYM; sign_blank_token = MINUS_BLANK_STR;
            inv_sign_token = PLUS_SYM;  inv_sign_blank_token = PLUS_BLANK_STR;
        }
        else sign_token = "!!!!!!!SIGN_ERROR!!!!";


        // 기본식 : (name1의 나이) +- (name2의 나이) = %d
        // explanation token 조합
        // (name1의 나이) = %d -+ (name2의 나이) = %d // name1 구하기, +/-
        // (name2의 나이) = %d - name1의 나이 = %d // name2 구하기, +
        // (name2의 나이) = name1의 나이 - %d = %d // name2 구하기, -
        String ex_expression_str_start_token = "\"" + content + "\"라는 문장을 식으로 바꾸면";
        String ex_expression_token = name1_with_category_token + sign_blank_token + name2_with_category_token
                + BLANK_SYM + EQUAL_SYM + BLANK_SYM + var1_token;
        String ex_expression_str_end_token = "가 됩니다.";
        String ex_explain_start_token = "이제 이 식을 다음과 같은 순서로 풀 수 있습니다.";
        String ex_name_var1_token = name1_with_category_token
                + EQUAL_BLANK_SYM + var1_token + inv_sign_blank_token + name2_with_category_token
                + EQUAL_BLANK_SYM + var1_token + inv_sign_blank_token + name_var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + var1_token + inv_sign_token + name_var2_token + EXPRESSION_END;
        String ex_name_var2_with_sum_token = name2_with_category_token
                + EQUAL_BLANK_SYM + var1_token + MINUS_BLANK_STR + name1_with_category_token
                + EQUAL_BLANK_SYM + var1_token + MINUS_BLANK_STR + name_var1_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + var1_token + MINUS_SYM + name_var1_token + EXPRESSION_END;
        String ex_name_var2_with_diff_token = name2_with_category_token
                + EQUAL_BLANK_SYM + name1_with_category_token + MINUS_BLANK_STR + var1_token
                + EQUAL_BLANK_SYM + name_var1_token + var1_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + name_var1_token + MINUS_SYM + var1_token + EXPRESSION_END;

        String explanation = "";
        ArrayList<String> explanation_ls = new ArrayList<>(10);
        explanation_ls.add(ex_expression_str_start_token);
        explanation_ls.add(ex_expression_token);
        explanation_ls.add(ex_expression_str_end_token);

        explanation_ls.add(ex_explain_start_token);
        if(cond_inx == 0) {  // age1 given, find age2
            if(sign == PLUS_SIGN) {
                explanation_ls.add(ex_name_var2_with_sum_token);
            } else if(sign == MINUS_SIGN) {
                explanation_ls.add(ex_name_var2_with_diff_token);
            } else{
                explanation_ls.add("SIGN VALUE ERROR");
            }
        } else if(cond_inx == 1){   // age2 given, find age1
            explanation_ls.add(ex_name_var1_token);
        }

        explanation = explanation_ls.get(0);
        for(int i = 1; i < explanation_ls.size(); i++){
            explanation += "\n" + explanation_ls.get(i);
        }

        return new String[] {content, explanation};     // {content, explanation}
    }


    /////////////////////////////////////////////////////////////////////////////////////////
    // 랜덤 숫자 뽑기


    // random int 값 뽑기
    public int getRandomIntValue(int min_value, int max_value){
        if(max_value < min_value){
            System.out.println("ERROR:: getRandomIntValue() : min > max now... should be min <= max");
            return -1;
        } else if(max_value == min_value){
            return 0;
        } else{
            int ret = random.nextInt(max_value-min_value+1) + min_value;
            return ret;
        }
    }


    // 나이 문제 숫자 뽑기
    private void getRandomAgeValue(int age_ls_length, int num_var_per_sentence){
        varElementary5th.name_var_ls = new int[age_ls_length];
        varElementary5th.var_ls = new int[age_ls_length * num_var_per_sentence];

        int age0 = getRandomIntValue(varElementary5th.name_var_min_value_ls[0], varElementary5th.name_var_max_value_ls[0]);
        int given_age = age0;
        int num_sentence = varElementary5th.sentence_category_id_ls.length;
        int start_index = num_sentence - 1;   // 마지막 상황문장부터 숫자 뽑음
        for(int i = start_index; i >= 0; i--){
            int age1_index = i;
            int age2_index = (i + 1) % varElementary5th.name_var_ls.length;
            int var1_index = age1_index * num_var_per_sentence;
            int var2_index = var1_index + 1;
            int year1_index = var1_index + 2;
            int year2_index = var1_index + 3;

            try {
                if (varElementary5th.sentence_category_id_ls[i] == AGE_CATEGORY_ID_YX) {
                    int[] ret_var = getRandomYXValue(given_age,
                            varElementary5th.var_sign_ls[var2_index], varElementary5th.var_sign_ls[year1_index], varElementary5th.var_sign_ls[year2_index],
                            varElementary5th.name_var_min_value_ls[age1_index], varElementary5th.name_var_max_value_ls[age1_index],
                            varElementary5th.var_min_value_ls[var1_index], varElementary5th.var_max_value_ls[var1_index],
                            varElementary5th.var_min_value_ls[var2_index], varElementary5th.var_max_value_ls[var2_index],
                            varElementary5th.var_min_value_ls[year1_index], varElementary5th.var_max_value_ls[year1_index],
                            varElementary5th.var_min_value_ls[year2_index], varElementary5th.var_max_value_ls[year2_index],
                            varElementary5th.useYear_ls[i], varElementary5th.useAddMinus_ls[i], varElementary5th.useMult_ls[i]);
                    varElementary5th.name_var_ls[age1_index] = ret_var[0];
                    varElementary5th.name_var_ls[age2_index] = ret_var[1];
                    varElementary5th.var_ls[var1_index] = ret_var[2];
                    varElementary5th.var_ls[var2_index] = ret_var[3];
                    varElementary5th.var_ls[year1_index] = ret_var[4];
                    varElementary5th.var_ls[year2_index] = ret_var[5];
                } else if (varElementary5th.sentence_category_id_ls[i] == AGE_CATEGORY_ID_SUM_DIFFERENCE) {
                    int[] ret_var = getRandomX1X2Value(given_age, varElementary5th.var_sign_ls[i],
                            varElementary5th.name_var_min_value_ls[age1_index], varElementary5th.name_var_max_value_ls[age1_index]);
                    varElementary5th.name_var_ls[age1_index] = ret_var[0];
                    varElementary5th.name_var_ls[age2_index] = ret_var[1];
                    varElementary5th.var_ls[var1_index] = ret_var[2];

                } else {
                    System.out.println("ERROR:: invalid category id");
                }
            } catch (TimeoutException e){
                    i = start_index;
                    continue;
            }
        }
    }

    // (name_var1 +- var3) = (name_var2 +- var4) * var1 +- var2
    // name_var2 given
    // return new int[] {name_var1, name_var2, var1, var2, var3, var4}
    private int[] getRandomYXValue(int given_name_var2, int var_sign, int year1_sign, int year2_sign,
                                   int name_var1_min_value, int name_var1_max_value,
                                   int var1_min_value, int var1_max_value,
                                   int var2_min_value, int var2_max_value,
                                   int var3_min_value, int var3_max_value,
                                   int var4_min_value, int var4_max_value,
                                   boolean useYear, boolean useAddMinus, boolean useMult)
    throws TimeoutException { // name_var2, year given
        int var1=1, var2=0, var3=0, var4=0, name_var1=0, name_var2=given_name_var2;

        long timeoutInMn = 3;   // timeout 시간
        LocalDateTime startTime = LocalDateTime.now();

        // name_var1 = name_var2 * var1 +- var2
        while(name_var1 < name_var1_min_value || name_var1 > name_var1_max_value) {
            var1 = getRandomIntValue(var1_min_value, var1_max_value);
            var2 = getRandomIntValue(var2_min_value, var2_max_value);
            var3 = getRandomIntValue(var3_min_value, var3_max_value);
            var4 = getRandomIntValue(var4_min_value, var4_max_value);
            if(useMult == false)  {
                var1 = 1;
            }
            if(useAddMinus == false){
                var2 = 0;
            }
            if(useYear == false) {
                var3 = 0;
                var4 = 0;
            }

            int name_var2_with_var4 = 0;
            int name_var1_with_var3 = 0;
            if(year2_sign == PLUS_SIGN){
                name_var2_with_var4 = name_var2 + var4;
            } else{
                name_var2_with_var4 = name_var2 - var4;
            }
            if(var_sign == PLUS_SIGN) {
                name_var1_with_var3 = name_var2_with_var4 * var1 + var2;
            } else{
                name_var1_with_var3 = name_var2_with_var4 * var1 - var2;
            }
            if(year1_sign == PLUS_SIGN){
                name_var1 = name_var1_with_var3 - var3;
            } else{
                name_var1 = name_var1_with_var3 + var3;
            }

            // timeout
            if(ChronoUnit.MINUTES.between(startTime, LocalDateTime.now()) > timeoutInMn){
                throw new TimeoutException();
            }
        }

        return new int[] {name_var1, name_var2, var1, var2, var3, var4};
    }

    // name_var1 +- name_var2 = var1
    // name_var2 given
    // 단, name_var1 > name_var2 여야 (초등교육과정)
    // return new int[] {name_var1, name_var2, var1};
    private int[] getRandomX1X2Value(int given_name_var2, int sign,
                                     int name_var1_min_value, int name_var1_max_value)
    throws TimeoutException { // age2 given
        int age1 = 0;
        int age2 = given_name_var2;

        long timeoutInMn = 3;   // timeout 시간
        LocalDateTime startTime = LocalDateTime.now();

        int var1 = -1;
        while (var1 < 0){
            age1 = getRandomIntValue(name_var1_min_value, name_var1_max_value);
            if(sign == PLUS_SIGN){  // age1 + age2 = var1
                var1 = age1 + age2;
            } else { // age1 - age2 = var1
                var1 = age1 - age2;
            }

            // timeout
            if(ChronoUnit.MINUTES.between(startTime, LocalDateTime.now()) > timeoutInMn){
                throw new TimeoutException();
            }
        }
        return new int[] {age1, age2, var1};
    }


    /////////////////////////////////////////////////////////////////////////////////////////////
    // template -> problem

    // return new String[] {real_content, real_explanation, real_answer};
    public String[] templateToProblem(String[] name_ls, int[] name_var_ls, int[] var_ls,
                                      String content_template, String explanation_template, String answer_template){
        // content, explanation, answer
        String real_content = content_template;
        String real_explanation = explanation_template;
        String real_answer = answer_template;
        for(int i = 0; i < name_ls.length; i++){
            String old_value = VAR_START + NAME_STR+i + VAR_END;
            String new_value = name_ls[i];
            real_content = real_content.replace(old_value, new_value);
            real_explanation = real_explanation.replace(old_value, new_value);
            real_answer = real_answer.replace(old_value, new_value);
        }
        for(int i = 0; i < name_var_ls.length;i++){
            String old_value = VAR_START + NAME_VAR_STR+i + VAR_END;
            String new_value = String.valueOf(name_var_ls[i]);
            real_content = real_content.replace(old_value, new_value);
            real_explanation = real_explanation.replace(old_value, new_value);
            real_answer = real_answer.replace(old_value, new_value);
        }
        for(int i = 0; i < var_ls.length; i++){
            String old_value = VAR_START + VAR_STR+i + VAR_END;
            String new_value = String.valueOf(var_ls[i]);
            real_content = real_content.replace(old_value, new_value);
            real_explanation = real_explanation.replace(old_value, new_value);
            real_answer = real_answer.replace(old_value, new_value);
        }

        real_content = calcExpr(real_content);
        real_explanation = calcExpr(real_explanation);
        real_answer = calcExpr(real_answer);

        return new String[] {real_content, real_explanation, real_answer};
    }

    // templateToProblem에서 [식내용] -> 계산한 값
    public String calcExpr(String target){
        // [] 속 식 계산
        /*
        ScriptEngineManager s = new ScriptEngineManager();
        ScriptEngine engine = s.getEngineByName("JavaScript");
        String str = "(10+20)*2";
        int num = 0;
        try {
            num = (int)engine.eval(str);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
        System.out.println(str + " = " + num);
         */
        String res = "";
        ArrayList<Character> expression = new ArrayList<>();
        boolean inExpr = false;
        for(int i = 0; i < target.length(); i++){
            char char_i = target.charAt(i);
            if(char_i == '['){
                expression.clear();
                inExpr = true;
            } else if(char_i == ']'){
                inExpr = false;
                String calc_res = "DEFAULT_EXPR_STR:: ";

                // calculate

                // 임시 dummy 값
                for(int j = 0; j < expression.size(); j++){
                    calc_res += expression.get(j);
                }

                res += calc_res;
            } else {
                if (inExpr == true) {   // 식 속의 문자
                    expression.add(char_i);
                } else {    // 식 밖의 문자
                    res += char_i;
                }
            }
        }
        return res;
    }

    // 디버깅용 - template 출력
    public void printTemplate(){
        System.out.println("CONTENT ------------------------------------");
        System.out.println(varElementary5th.content_template);
        System.out.println("EXPLANATION ------------------------------------");
        System.out.println(varElementary5th.explanation_template);
        System.out.println("ANSWER ------------------------------------");
        System.out.println(varElementary5th.answer_template);
        System.out.println("SENTENCE_CATEGORY_ID ------------------------------");
        System.out.println(Arrays.toString(varElementary5th.sentence_category_id_ls));
        System.out.println("VAR_SIGN ------------------------------------");
        System.out.println(Arrays.toString(varElementary5th.var_sign_ls));
        System.out.println("\n\n");
    }


}