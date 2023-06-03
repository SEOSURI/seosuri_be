package com.onejo.seosuri.service.algorithm;

import com.onejo.seosuri.service.algorithm.problem.ProblemValueStruct;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class Elementary5th_Backup {
// 이곳에 문제 생성 알고리즘을 넣어두고, ProblemSerice에서 필요한 method를 호출하면 될 듯 싶습니다..


    Random random;
    public static final int PLUS_SIGN = 0;
    public static final int MINUS_SIGN = 1;

    // 상황 문장 id
    public static final int SENTENCE_CATEGORY_NUM = 2;  // 상황 문장 유형 갯수
    public static final int CATEGORY_ID_YX = 0;
    public static final int CATEGORY_ID_SUM_DIFFERENCE = 1;

    // 상황문장 1개당 상수 변수의 개수
    // 참고 - 문제에서 등장하는 변수 = 구하는값 관련 변수(나이) + 상수 변수(year, 곱해지는 수(var1), 더하거나 빼는 수(var2))
    public static final int AGE_PROB_VAR_NUM_PER_SENTENCE = 4;     // var1, var2, year1, year2
    public static final int UNKNOWNNUM_PROB_VAR_NUM_PER_SENTECE = 4;


    public static final int AGE_PROB_MULT_VAR_OFFSET = 0;
    public static final int AGE_PROB_ADDMIN_VAR_OFFSET = 1;
    public static final int AGE_PROB_YEAR_VAR1_OFFSET = 2;
    public static final int AGE_PROB_YEAR_VAR2_OFFSET = 3;


    public static final int UNKNOWN_PROB_CORRECT_NUM_INDEX = 0;
    public static final int UNKNOWN_PROB_WRONG_NUM_INDEX = 1;
    public static final int UNKNOWN_PROB_X_INDEX = 2;


    // 변수명 string 규칙
    public static final String NAME_VAR_STR = "name_var";    // age 변수 : {age0}, {age1}, {age2}, ...
    public static final String NAME_STR = "name";  // name 변수 : {name0}, {name1}, {name2}, ...
    public static final String VAR_STR = "var";    // 상수 변수 : {var0}, {var1}, {var2}, ...
    public static final String YEAR_STR = "year";  // year 변수
    public static final String VAR_START = "{";
    public static final String VAR_END = "}";
    public static final String EXPRESSION_START = "[";
    public static final String EXPRESSION_END = "]";
    public static final String BRACKET_START = "(";    // expression 내부에서 사용하는 괄호(시작)
    public static final String BRACKET_END = ")";      // expression 내부에서 사용하는 괄호(끝)
    public static final String PLUS_SYM = "+";
    public static final String MINUS_SYM = "-";
    public static final String MULT_SYM = "*";
    public static final String DIVIDE_SYM = "/";
    public static final String EQUAL_SYM = "=";
    public static final String BLANK_SYM = " ";
    public static final String EQUAL_D_BLANK_SYM = " = ";
    public static final String EQUAL_NL_BLANK_SYM = "\n= ";
    public static final String PLUS_BLANK_STR = " + ";
    public static final String MINUS_BLANK_STR = " - ";
    public static final String MULT_BLANK_STR = " * ";
    public static final String DIVIDE_BLANK_STR = " / ";


    // string tokens
    public static final String blank_token = " ";
    public static final String ui_token = "의";
    public static final String gwa_token = "과";
    public static final String wa_token = "와";
    public static final String neun_token = "는";
    public static final String eun_token = "은";
    public static final String eul_token = "을";
    public static final String reul_token = "를";
    public static final String eseo_token = "에서";

    public static final String bae_token = "배 한 것";
    public static final String than_token = "보다";
    public static final String same_token = "같습니다.";
    public static final String more_amount_token = "많습니다.";
    public static final String less_amount_token = "적습니다.";
    public static final String sum_token = "합한";
    public static final String difference_token = "뺀";
    public static final String value_token = "값";

    public static final String age_category_token = "나이";
    public static final String age_unit_token = "살";
    public static final String year_unit_token = "년";
    public static final String time_after_token = "후";
    public static final String time_before_token = "전";
    //String time_past_token = "년 후";


    private ProblemValueStruct problemValueStruct = new ProblemValueStruct();



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
        int var_num_per_sentence = AGE_PROB_VAR_NUM_PER_SENTENCE;

        random = new Random(); //랜덤 객체 생성(디폴트 시드값 : 현재시간)
        random.setSeed(System.currentTimeMillis()); //시드값 설정을 따로 할수도 있음

        int variable_var_num = prob_sentence_num + 1;
        int constant_var_num = prob_sentence_num * var_num_per_sentence;


        problemValueStruct.sentence_category_id_ls = new int[prob_sentence_num];    // 각 상황문장이 어떤 유형의 문장인지를 저장한 배열
        // DB에서 sentence_category_id_ls 가져오기!!!!
        problemValueStruct.var_sign_ls = new int[constant_var_num];    // DB에서 가져오기!!!
        problemValueStruct.useYear1_ls = new boolean[prob_sentence_num];   // DB에서 가져오기!!!
        problemValueStruct.useYear2_ls = new boolean[prob_sentence_num];    // DB에서 가져오기!!!
        problemValueStruct.useMult_ls = new boolean[prob_sentence_num];   // DB에서 가져오기!!!
        problemValueStruct.useAddMinus_ls = new boolean[prob_sentence_num];   // DB에서 가져오기!!!

        // DB에서 템플릿 가져오기 - 난이도로 템플릿 고르고 그 중에서 랜덤 뽑기!!!
        problemValueStruct.content_template = "내용";
        problemValueStruct.explanation_template = "설명";
        problemValueStruct.answer_template = "답";


        // name 뽑기, name_var 범위 설정  -> 문제 숫자 값 랜덤 뽑기 시 이용됨
        // DB에서 name, name_var범위 값 가져오게 수정해야!!!
        setVariantVarMinMaxLsAndStringLs(variable_var_num);          // name_ls, name_var_min_value_ls, name_var_max_value_ls 설정

        // 상수 var 범위 설정 -> 문제 숫자 값 랜덤 뽑기 시 이용됨
        setConstantVarMinMaxLs(prob_sentence_num, var_num_per_sentence);    // var_min_value_ls, var_max_value_ls 설정

        // 상수 var, name_var 랜덤 뽑기 -> 숫자 변경 시 여기부터 다시 실행하면 됨!!!
        // ageProblem의 variable_var_num = 4, var_num_per_sentence = level
        // 인자 외에 내부에서 이용하는 값 : name_var_min_value_ls, name_value_max_value_ls, sentence_category_id_ls, var_sign_ls, var_min_value_ls, var_max_value_ls, useYear_ls, useMult_ls, useAddMinus_ls
        setVar(variable_var_num, constant_var_num, var_num_per_sentence);  // name_var_ls, var_ls 설정

        // template -> problem
        String[] real_prob = templateToProblem(problemValueStruct.variant_var_string_ls, problemValueStruct.variant_var_ls, problemValueStruct.constant_var_ls,
                problemValueStruct.content_template, problemValueStruct.explanation_template, problemValueStruct.answer_template);

        problemValueStruct.real_content = real_prob[0];
        problemValueStruct.real_explanation = real_prob[1];
        problemValueStruct.real_answer = real_prob[2];
    /*
    System.out.println("\n\n-----------------------------------------------------");
    System.out.println(real_prob[0]);   // real_content
    System.out.println("\n\n-----------------------------------------------------");
    System.out.println(real_prob[1]);   // real_explanation
    System.out.println("\n\n-----------------------------------------------------");
    System.out.println(real_prob[2]);   // real_answer
     */
    }

/*
// 구버전(지금은 아무데서도 사용 안 함) - 디버깅용으로 실제 문제 하나 만들어보는 함수였음~~~
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
*/


    // 이은 색테이프 문제 알고리즘
    public void colorTapeProblem(int level) {}

    // 어떤 수 문제 알고리즘
    public void anyNumberProblem(int level) {
        int prob_sentence_num = 2;  // 잘못 계산한 수, 바르게 계산한 수
        int var_num_per_sentence = UNKNOWNNUM_PROB_VAR_NUM_PER_SENTECE;

        random = new Random(); //랜덤 객체 생성(디폴트 시드값 : 현재시간)
        random.setSeed(System.currentTimeMillis()); //시드값 설정을 따로 할수도 있음

        int variable_var_num = prob_sentence_num + 1;   // 잘못 계산한 수, 바르게 계산한 수
        int constant_var_num = prob_sentence_num * var_num_per_sentence;

        problemValueStruct.sentence_category_id_ls = new int[prob_sentence_num];    // 각 상황문장이 어떤 유형의 문장인지를 저장한 배열
        // DB에서 sentence_category_id_ls 가져오기!!!!
        problemValueStruct.var_sign_ls = new int[constant_var_num];    // DB에서 가져오기!!!
        problemValueStruct.useYear1_ls = new boolean[prob_sentence_num];   // DB에서 가져오기!!!
        problemValueStruct.useYear2_ls = new boolean[prob_sentence_num];   // DB에서 가져오기!!!
        problemValueStruct.useMult_ls = new boolean[prob_sentence_num];   // DB에서 가져오기!!!
        problemValueStruct.useAddMinus_ls = new boolean[prob_sentence_num];   // DB에서 가져오기!!! // entire addminus should be false

        // DB에서 템플릿 가져오기 - 난이도로 템플릿 고르고 그 중에서 랜덤 뽑기!!!
        problemValueStruct.content_template = "내용";
        problemValueStruct.explanation_template = "설명";
        problemValueStruct.answer_template = "답";

        // 변수 string, 변수 var 결정
        setUnknownNumProbNameAndNameVarRange(variable_var_num); // name_ls, name_var_min_value_ls, name_var_max_value_ls 설정

        // 상수 var 범위 설정 -> 문제 숫자 값 랜덤 뽑기 시 이용됨
        setConstantVarMinMaxLs(prob_sentence_num, var_num_per_sentence);    // var_min_value_ls, var_max_value_ls 설정

        // 상수 var, name_var 랜덤 뽑기 -> 숫자 변경 시 여기부터 다시 실행하면 됨!!!
        // ageProblem의 variable_var_num = 4, var_num_per_sentence = level
        // 인자 외에 내부에서 이용하는 값 : name_var_min_value_ls, name_value_max_value_ls, sentence_category_id_ls, var_sign_ls, var_min_value_ls, var_max_value_ls, useYear_ls, useMult_ls, useAddMinus_ls
        setVar(variable_var_num, constant_var_num, var_num_per_sentence);  // name_var_ls, var_ls 설정

        // template -> problem
        String[] real_prob = templateToProblem(problemValueStruct.variant_var_string_ls, problemValueStruct.variant_var_ls, problemValueStruct.constant_var_ls,
                problemValueStruct.content_template, problemValueStruct.explanation_template, problemValueStruct.answer_template);

        problemValueStruct.real_content = real_prob[0];
        problemValueStruct.real_explanation = real_prob[1];
        problemValueStruct.real_answer = real_prob[2];
    /*
    System.out.println("\n\n-----------------------------------------------------");
    System.out.println(real_prob[0]);   // real_content
    System.out.println("\n\n-----------------------------------------------------");
    System.out.println(real_prob[1]);   // real_explanation
    System.out.println("\n\n-----------------------------------------------------");
    System.out.println(real_prob[2]);   // real_answer
     */
    }

    // 도형에서의 혼합 계산 응용 알고리즘
    public void geometryCalculation() {}










    /////////////////////////////////////////////////////////////////////////////////////////////
    // 다 옮긴 함수

    // 나이 문제 숫자 뽑기
    // input : name_var_min_value_ls, name_value_max_value_ls, sentence_category_id_ls, var_sign_ls, var_min_value_ls, var_max_value_ls, useYear_ls, useMult_ls, useAddMinus_ls
    // output : name_var_ls, var_ls
    private void setVar(int variable_var_num, int constant_var_num, int num_constant_var_per_sentence){
        problemValueStruct.variant_var_ls = new int[variable_var_num];
        problemValueStruct.constant_var_ls = new int[constant_var_num];

        int age0 = getRandomIntValue(problemValueStruct.variant_var_min_value_ls[0], problemValueStruct.variant_var_max_value_ls[0]);
        int given_age = age0;
        int num_sentence = problemValueStruct.sentence_category_id_ls.length;
        int start_index = num_sentence - 1;   // 마지막 상황문장부터 숫자 뽑음
        for(int i = start_index; i >= 0; i--){
            int age1_index = i;
            int age2_index = (i + 1) % problemValueStruct.variant_var_ls.length;
            int var1_index = age1_index * num_constant_var_per_sentence;
            int var2_index = var1_index + 1;
            int year1_index = var1_index + 2;
            int year2_index = var1_index + 3;

            try {
                if (problemValueStruct.sentence_category_id_ls[i] == CATEGORY_ID_YX) {
                    int[] ret_var = getRandomYXValue(given_age,
                            problemValueStruct.var_sign_ls[var2_index], problemValueStruct.var_sign_ls[year1_index], problemValueStruct.var_sign_ls[year2_index],
                            problemValueStruct.variant_var_min_value_ls[age1_index], problemValueStruct.variant_var_max_value_ls[age1_index],
                            problemValueStruct.constant_var_min_value_ls[var1_index], problemValueStruct.constant_var_max_value_ls[var1_index],
                            problemValueStruct.constant_var_min_value_ls[var2_index], problemValueStruct.constant_var_max_value_ls[var2_index],
                            problemValueStruct.constant_var_min_value_ls[year1_index], problemValueStruct.constant_var_max_value_ls[year1_index],
                            problemValueStruct.constant_var_min_value_ls[year2_index], problemValueStruct.constant_var_max_value_ls[year2_index],
                            problemValueStruct.useYear1_ls[i], problemValueStruct.useYear2_ls[i], problemValueStruct.useAddMinus_ls[i], problemValueStruct.useMult_ls[i]);
                    problemValueStruct.variant_var_ls[age1_index] = ret_var[0];
                    problemValueStruct.variant_var_ls[age2_index] = ret_var[1];
                    problemValueStruct.constant_var_ls[var1_index] = ret_var[2];
                    problemValueStruct.constant_var_ls[var2_index] = ret_var[3];
                    problemValueStruct.constant_var_ls[year1_index] = ret_var[4];
                    problemValueStruct.constant_var_ls[year2_index] = ret_var[5];
                } else if (problemValueStruct.sentence_category_id_ls[i] == CATEGORY_ID_SUM_DIFFERENCE) {
                    int[] ret_var = getRandomX1X2Value(given_age, problemValueStruct.var_sign_ls[i],
                            problemValueStruct.variant_var_min_value_ls[age1_index], problemValueStruct.variant_var_max_value_ls[age1_index]);
                    problemValueStruct.variant_var_ls[age1_index] = ret_var[0];
                    problemValueStruct.variant_var_ls[age2_index] = ret_var[1];
                    problemValueStruct.constant_var_ls[var1_index] = ret_var[2];

                } else {
                    System.out.println("ERROR:: invalid category id");
                }
            } catch (TimeoutException e){
                i = start_index;
                continue;
            }
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    // 실제 문제 생성 설정값 setting

    // 난이도에 따른 useBoolean 변수들 (useYear, useMult, useAddMinus) 설정값
    private void setUseBooleans(int level, int var_num_per_sentence){
        int prob_sentence_num = level;  // 상황 문장 갯수 - 문제 난이도에 따라 값 달라짐

        problemValueStruct.useYear1_ls = new boolean[prob_sentence_num];
        problemValueStruct.useYear2_ls = new boolean[prob_sentence_num];
        problemValueStruct.useMult_ls = new boolean[prob_sentence_num];
        problemValueStruct.useAddMinus_ls = new boolean[prob_sentence_num];

        for(int i = 1; i < prob_sentence_num; i++){
            problemValueStruct.useYear1_ls[i] = random.nextBoolean();
            problemValueStruct.useYear2_ls[i] = random.nextBoolean();
            problemValueStruct.useMult_ls[i] = random.nextBoolean();
            problemValueStruct.useAddMinus_ls[i] = random.nextBoolean();
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
    private void setConstantVarMinMaxLs(int prob_sentence_num, int num_var_per_sentence){
        problemValueStruct.constant_var_min_value_ls = new int[prob_sentence_num * num_var_per_sentence];
        problemValueStruct.constant_var_max_value_ls = new int[prob_sentence_num * num_var_per_sentence];
        for(int i = 0; i < prob_sentence_num; i++){
            int var1_index = i * num_var_per_sentence;
            if(problemValueStruct.useMult_ls[i]) {    // var1
                problemValueStruct.constant_var_min_value_ls[var1_index] = 2;
                problemValueStruct.constant_var_max_value_ls[var1_index] = 5;
            } else{ // 0~100 =  0~100 * 2 - 100 // 99 *
                problemValueStruct.constant_var_min_value_ls[var1_index] = 1;
                problemValueStruct.constant_var_max_value_ls[var1_index] = 1;
            }
            if(problemValueStruct.useAddMinus_ls[i]){ // var2
                problemValueStruct.constant_var_min_value_ls[var1_index+1] = 1;
                problemValueStruct.constant_var_max_value_ls[var1_index+1] = 20;
            } else{
                problemValueStruct.constant_var_min_value_ls[var1_index+1] = 0;
                problemValueStruct.constant_var_max_value_ls[var1_index+1] = 0;
            }
            if(problemValueStruct.useYear1_ls[i]){
                problemValueStruct.constant_var_min_value_ls[var1_index+2] = 1;
                problemValueStruct.constant_var_max_value_ls[var1_index+2] = 100;
            } else{
                problemValueStruct.constant_var_min_value_ls[var1_index+2] = 0;
                problemValueStruct.constant_var_max_value_ls[var1_index+2] = 0;
            }
            if(problemValueStruct.useYear2_ls[i]){
                problemValueStruct.constant_var_min_value_ls[var1_index+3] = 1;
                problemValueStruct.constant_var_max_value_ls[var1_index+3] = 100;
            } else{
                problemValueStruct.constant_var_min_value_ls[var1_index+3] = 0;
                problemValueStruct.constant_var_max_value_ls[var1_index+3] = 0;
            }
        }
    }

    // name_ls, name_var_min_value_ls, name_var_max_value_ls 설정
    private void setVariantVarMinMaxLsAndStringLs(int name_var_num){
        problemValueStruct.variant_var_min_value_ls = new int[name_var_num];
        problemValueStruct.variant_var_max_value_ls = new int[name_var_num];
        problemValueStruct.variant_var_string_ls = new String[name_var_num];
        for(int i = 0; i < name_var_num; i++){      // DB 연결 -> DB에서 값 받아와야
            problemValueStruct.variant_var_min_value_ls[i] = 10;
            problemValueStruct.variant_var_max_value_ls[i] = 100;
            problemValueStruct.variant_var_string_ls[i] = i+"사람"+i;
        }
    }

    private void setUnknownNumProbNameAndNameVarRange(int name_var_num){
        problemValueStruct.variant_var_min_value_ls = new int[name_var_num];
        problemValueStruct.variant_var_max_value_ls = new int[name_var_num];
        problemValueStruct.variant_var_string_ls = new String[name_var_num];
        if(name_var_num == 3){
            problemValueStruct.variant_var_string_ls[UNKNOWN_PROB_X_INDEX] = "어떤 수";
            problemValueStruct.variant_var_string_ls[UNKNOWN_PROB_CORRECT_NUM_INDEX] = "바르게 계산한 수";
            problemValueStruct.variant_var_string_ls[UNKNOWN_PROB_WRONG_NUM_INDEX] = "잘못 계산한 수";
            for(int i = 0; i < name_var_num; i++){
                problemValueStruct.variant_var_min_value_ls[i] = 1;
                problemValueStruct.variant_var_max_value_ls[i] = 100;
            }
        } else {
            System.out.println("ERROR :: name_var_num should be 3 in unknown num problem");
        }
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
        System.out.println(problemValueStruct.content_template);
        System.out.println("EXPLANATION ------------------------------------");
        System.out.println(problemValueStruct.explanation_template);
        System.out.println("ANSWER ------------------------------------");
        System.out.println(problemValueStruct.answer_template);
        System.out.println("SENTENCE_CATEGORY_ID ------------------------------");
        System.out.println(Arrays.toString(problemValueStruct.sentence_category_id_ls));
        System.out.println("VAR_SIGN ------------------------------------");
        System.out.println(Arrays.toString(problemValueStruct.var_sign_ls));
        System.out.println("\n\n");
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 나이 문제 모든 템플릿 생성
    //

    // 순열 메서드(cnt는 선택 횟수)
    public boolean[][] useBoolean_ls_ls;
    private static final boolean[] target_boolean = new boolean[] {true, false};
    public void set_useBoolean_ls_ls(int prob_sentence_num){
        int row_num = (int)Math.pow(2.0f, prob_sentence_num);
        useBoolean_ls_ls = new boolean[row_num][prob_sentence_num];
        for(int i = 0; i < row_num; i++){
            useBoolean_ls_ls[i] = new boolean[prob_sentence_num];
        }
        bool_permutation(0);
    }

    // starts with permutation(0)
    public void bool_permutation(int cnt) {
        int N = useBoolean_ls_ls.length;
        int prob_sentence_num = useBoolean_ls_ls[0].length;
        if (cnt == useBoolean_ls_ls[0].length) {
            return;
        }
        // 대상 집합을 순회하며 숫자를 하나 선택한다.
        for (int i = 0; i < target_boolean.length; i++) {
            // ex) 8개 종류
            // 2개로 나눠 -> 0 ~ N/2-1 : true, N/2 ~ 2N/2-1 : false
            //      cnt = 0     -> 2^1
            // 4개로 나눠 -> 0 ~ N/4-1 : true, N/4 ~ 2N/4-1 : false, 2N/4 ~ 3N/4-1 : true, 3N/4 ~ 4N/4-1 : false
            //      cnt = 1     -> 2^2
            // 8개로 나눠 -> ...
            int offset = N/(int)Math.pow(2, cnt+1);
            for(int j = i * offset; j < N; j += 2 * offset){
                for(int row = j; row < j+offset; row++){
                    useBoolean_ls_ls[row][cnt] = target_boolean[i];
                }
            }
            bool_permutation(cnt + 1);
        }
    }

    ArrayList<Integer>[] sentence_category_id_ls_ls;
    ArrayList<Integer>[] var_sign_ls_ls;

    public void setSentence_category_id_ls_ls(int prob_sentence_num){
        int row_num = (int)Math.pow(SENTENCE_CATEGORY_NUM, prob_sentence_num);
        sentence_category_id_ls_ls = new ArrayList[row_num];   // 모든 순열 리스트
        for(int i = 0; i < row_num; i++){
            sentence_category_id_ls_ls[i] = new ArrayList<Integer>();
        }
        int_permutation(0, sentence_category_id_ls_ls, new int[] {CATEGORY_ID_YX, CATEGORY_ID_SUM_DIFFERENCE}, prob_sentence_num);
    }

    public void setVar_sign_ls_ls(int var_num){
        int row_num = (int)Math.pow(2, var_num);
        var_sign_ls_ls = new ArrayList[row_num];
        for(int i = 0; i < row_num; i++){
            var_sign_ls_ls[i] = new ArrayList<Integer>();
        }
        int_permutation(0, var_sign_ls_ls, new int[] {PLUS_SIGN, MINUS_SIGN}, var_num);
    }


    // n^r개 배열 나옴
    // n = target.length
    public void int_permutation(int cnt, ArrayList<Integer>[] dest, int[] target, int r) {
        // target에서 숫자 골라 중복순열 만들기
        // cnt는 현재 탐색 깊이 (depth)
        int n = target.length;
        int N = (int)Math.pow(n, r);
        if (cnt == r) {
            return;
        }
        // 대상 집합을 순회하며 숫자를 하나 선택한다.
        for (int i = 0; i < target.length; i++) {
            // ex) 8개 종류
            // 2개로 나눠 -> 0 ~ N/2-1 : true, N/2 ~ 2N/2-1 : false
            //      cnt = 0     -> 2^1
            // 4개로 나눠 -> 0 ~ N/4-1 : true, N/4 ~ 2N/4-1 : false, 2N/4 ~ 3N/4-1 : true, 3N/4 ~ 4N/4-1 : false
            //      cnt = 1     -> 2^2
            // 8개로 나눠 -> ...
            int offset = N/(int)Math.pow(n, cnt+1);
            for(int j = i * offset; j < N; j += 2 * offset) {
                for (int row = j; row < j + offset; row++) {
                    dest[row].add(target[i]);
                }
            }
        }
        int_permutation(cnt + 1, dest, target, r);
    }


    // 나이문제 템플릿의 모든 조합을 DB에 저장
    public void saveAgeProblemTemplates(){
    /*
    prob_sentence_num(상황문장 갯수)
    answer_inx
    condition_inx
    sentence_category_id
    useYear
    useMult
    useAddMinus
    var1_sign
    var2_sign
    var3_sign
    var4_sign
    */

        int template_id = 0;    // template_id = 0, 1, 2, ...
        int var_num_per_sentence = AGE_PROB_VAR_NUM_PER_SENTENCE;

        for(int prob_sentence_num: new int[] {3, 2, 1}) {
            int name_var_num = prob_sentence_num + 1;
            int var_num = prob_sentence_num * var_num_per_sentence;
            int[] inx_ls = new int[prob_sentence_num];
            for(int i = 0; i < prob_sentence_num; i++){
                inx_ls[i] = i;
            }

            setSentence_category_id_ls_ls(prob_sentence_num);   // category_num(2)^prob_sentence_num
            set_useBoolean_ls_ls(prob_sentence_num);    // 2^prob_sentence_num
            setVar_sign_ls_ls(var_num);                 // 2^var_num = 2^(prob_sentence_num*va4_num_per_sentence(=4)) = 16 * 2^prob_sentence_num)

            for(ArrayList<Integer> sentence_category_id_ls: sentence_category_id_ls_ls){    // 2^prob_sentence_num
                problemValueStruct.sentence_category_id_ls = sentence_category_id_ls.stream().mapToInt(i->i).toArray();
                for(int answer_inx: inx_ls) {   // prob_sentence_num
                    for (int condition_inx = (answer_inx + 1) % name_var_num; condition_inx != answer_inx; condition_inx++) {
                        for (boolean[] useYear1_ls : useBoolean_ls_ls) { // 2^prob_sentence_num
                            problemValueStruct.useYear1_ls = useYear1_ls;
                            for (boolean[] useYear2_ls : useBoolean_ls_ls) { // 2^prob_sentence_num
                                problemValueStruct.useYear2_ls = useYear2_ls;
                                for (boolean[] useMult_ls : useBoolean_ls_ls) { // 2^prob_sentence_num
                                    problemValueStruct.useMult_ls = useMult_ls;
                                    for (boolean[] useAddMinus_ls : useBoolean_ls_ls) { // 2^prob_sentence_num
                                        problemValueStruct.useAddMinus_ls = useAddMinus_ls;
                                        for (ArrayList<Integer> var_sign_ls : var_sign_ls_ls) { // 16 * 2^prob_sentence_num
                                            problemValueStruct.var_sign_ls = var_sign_ls.stream().mapToInt(i -> i).toArray();

                                            // template 생성할 것인지 여부 결정
                                            // ex) year 사용 안 하는 경우 -> year에 따른 sign value 변화는 무시해도 좋음
                                            boolean generateTemplate = true;
                                            for (int i = 0; i < prob_sentence_num; i++) {
                                                if ((problemValueStruct.sentence_category_id_ls[i] == CATEGORY_ID_SUM_DIFFERENCE // 합차 유형에서 사용되는 변수는 var1(mult_offset에 해당하는 변수) 뿐, var1의 부호는 양수
                                                        && !(problemValueStruct.var_sign_ls[i * var_num_per_sentence + AGE_PROB_MULT_VAR_OFFSET] == PLUS_SIGN
                                                        && problemValueStruct.var_sign_ls[i * var_num_per_sentence + AGE_PROB_ADDMIN_VAR_OFFSET] == PLUS_SIGN
                                                        && problemValueStruct.var_sign_ls[i * var_num_per_sentence + AGE_PROB_YEAR_VAR1_OFFSET] == PLUS_SIGN
                                                        && problemValueStruct.var_sign_ls[i * var_num_per_sentence + AGE_PROB_YEAR_VAR2_OFFSET] == PLUS_SIGN))
                                                        || (useYear1_ls[i] == false  // year1 사용하지 않는 경우, year sign에 따른 변화 무시
                                                        && problemValueStruct.var_sign_ls[i * var_num_per_sentence + AGE_PROB_YEAR_VAR1_OFFSET] == MINUS_SIGN)
                                                        || (useYear2_ls[i] == false  // year2 사용하지 않는 경우, year sign에 따른 변화 무시
                                                        && problemValueStruct.var_sign_ls[i * var_num_per_sentence + AGE_PROB_YEAR_VAR2_OFFSET] == MINUS_SIGN)
                                                        || (useMult_ls[i] == false && (problemValueStruct.var_sign_ls[i * var_num_per_sentence] == MINUS_SIGN)) // mult 사용하지 않는 경우, mult sign 에 따른 변화 무시
                                                        || (useAddMinus_ls[i] == false && (problemValueStruct.var_sign_ls[i * var_num_per_sentence + AGE_PROB_ADDMIN_VAR_OFFSET] == MINUS_SIGN))) // addminus 사용하지 않는 경우, addminus sign 에 따른 변화 무시
                                                {
                                                    generateTemplate = false;
                                                    break;
                                                }
                                            }

                                            // template 생성, DB에 저장
                                            if (generateTemplate) {
                                                ageProblemTemplate(prob_sentence_num, var_num_per_sentence, condition_inx, answer_inx);
                                                System.out.println("" + template_id + ":: \n"
                                                        + problemValueStruct.content_template + "\n\n"
                                                        + problemValueStruct.answer_template + "\n\n"
                                                        + problemValueStruct.explanation_template + "\n\n");


                                                //DB에 저장 - 다음 값들은 위에서 저장됨 -> 이제 DB에 저장해보자!!!
                                                //Long id = template_id
                                                //Category category = ???
                                                //Long category_id
                                                //SmallUnit smallUnit
                                                //CategoryTitle title
                                                //List<TestPaper> testPapers
                                                //List<ProblemTemplate> problemTemplates
                                                //String content = varElementary5th.content_template;
                                                //String level = prob_sentence_num -> '상', '중', '하'
                                                //String explanation = varElementary5th.explanation_template;
                                                //String answer = varElementary5th.answer_template;
                                                //String sentenceCategoryList = varElementary5th.sentence_category_id_ls;
                                                //List<Problem> problems = ????

                                                // 다음은 DB에 column 생성한 후 저장해야
                                                //varElementary5th.var_sign_ls;
                                                //varElementary5th.useYear1_ls;
                                                //varElementary5th.useMult_ls;
                                                //varElementary5th.useAddMinus_ls;

                                                template_id++;
                                                //System.out.println("" + template_id);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 어떤수문제 템플릿의 모든 조합을 DB에 저장
    public void saveUnknownNumProblemTemplates(){
    /*

    useYear
    useMult
    useAddMinus
    var1_sign
    var2_sign
    var3_sign
    var4_sign
    */

        int template_id = 0;    // template_id = 0, 1, 2, ...
        final int var_num_per_sentence = UNKNOWNNUM_PROB_VAR_NUM_PER_SENTECE;
        final int prob_sentence_num = 2;
        final int name_var_num = prob_sentence_num + 1;
        final int var_num = prob_sentence_num * var_num_per_sentence;
        int[] inx_ls = new int[prob_sentence_num];
        for(int i = 0; i < prob_sentence_num; i++){
            inx_ls[i] = i;
        }
        set_useBoolean_ls_ls(prob_sentence_num);    // 2^prob_sentence_num
        setVar_sign_ls_ls(var_num);                 // 2^var_num = 2^(prob_sentence_num*va4_num_per_sentence(=4)) = 16 * 2^prob_sentence_num)
        problemValueStruct.useYear1_ls = new boolean[prob_sentence_num];
        problemValueStruct.useYear2_ls = new boolean[prob_sentence_num];
        problemValueStruct.sentence_category_id_ls = new int[prob_sentence_num];
        for(int i = 0; i < prob_sentence_num; i++){
            problemValueStruct.useYear1_ls[i] = false;
            problemValueStruct.useYear2_ls[i] = true;
            problemValueStruct.sentence_category_id_ls[i] = CATEGORY_ID_YX;
        }


        for (boolean[] useAddMinus_ls : useBoolean_ls_ls) { // 2^prob_sentence_num
            problemValueStruct.useAddMinus_ls = useAddMinus_ls;
            for (boolean[] useMult_ls : useBoolean_ls_ls) { // 2^prob_sentence_num
                problemValueStruct.useMult_ls = useMult_ls;
                for (ArrayList<Integer> var_sign_ls : var_sign_ls_ls) { // 16 * 2^prob_sentence_num
                    problemValueStruct.var_sign_ls = var_sign_ls.stream().mapToInt(i -> i).toArray();
                    // template 생성할 것인지 여부 결정
                    // ex) year 사용 안 하는 경우 -> year에 따른 sign value 변화는 무시해도 좋음
                    boolean generateTemplate = true;
                    for (int i = 0; i < prob_sentence_num; i++) {
                        if ((problemValueStruct.useYear1_ls[i] == false && problemValueStruct.var_sign_ls[i * var_num_per_sentence + AGE_PROB_YEAR_VAR1_OFFSET] == MINUS_SIGN)  // year1 사용하지 않는 경우, year sign에 따른 변화 무시
                                || (problemValueStruct.useYear2_ls[i] == false && problemValueStruct.var_sign_ls[i * var_num_per_sentence + AGE_PROB_YEAR_VAR2_OFFSET] == MINUS_SIGN)   // year2 사용하지 않는 경우, year sign에 따른 변화 무시
                                || (problemValueStruct.useMult_ls[i] == false && (problemValueStruct.var_sign_ls[i * var_num_per_sentence] == MINUS_SIGN)) // mult 사용하지 않는 경우, mult sign 에 따른 변화 무시
                                || (problemValueStruct.useAddMinus_ls[i] == false && (problemValueStruct.var_sign_ls[i * var_num_per_sentence + AGE_PROB_ADDMIN_VAR_OFFSET] == MINUS_SIGN))) // addminus 사용하지 않는 경우, addminus sign 에 따른 변화 무시
                        {
                            generateTemplate = false;
                            break;
                        }
                    }

                    // template 생성, DB에 저장
                    if (generateTemplate) {
                        unknownnumProblemTemplate();
                        System.out.println("" + template_id + ":: \n"
                                + problemValueStruct.content_template + "\n\n"
                                + problemValueStruct.answer_template + "\n\n"
                                + problemValueStruct.explanation_template + "\n\n");

                        //DB에 저장

                        template_id++;
                        //System.out.println("" + template_id);
                    }
                }
            }
        }
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // 템플릿 생성

    // 나이문제 템플릿 생성
    // template 생성 -> varElementary5th.content, explanation, answer에 결과 저장됨
    public void ageProblemTemplate (int prob_sentence_num, int var_num_per_sentence,
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
            sentence_ls[i] = create_age_sentence(i, i+1, problemValueStruct.sentence_category_id_ls[i], i , var_num_per_sentence, cond_inx_for_sentence,
                    problemValueStruct.useYear1_ls[i], problemValueStruct.useYear2_ls[i], problemValueStruct.useMult_ls[i], problemValueStruct.useAddMinus_ls[i],
                    problemValueStruct.var_sign_ls[var1_index+1], problemValueStruct.var_sign_ls[var1_index+2], problemValueStruct.var_sign_ls[var1_index+3]);  // sentence_ls[i] = {content, explanation}
        }
        cond_inx_for_sentence = 0;  // age1, age2 중 age1가 given
        for(int i = condition_inx; i < prob_sentence_num; i++){ // cond_inx+1~ -> age1 given
            int var1_index = i * var_num_per_sentence;
            sentence_ls[i] = create_age_sentence(i, i+1, problemValueStruct.sentence_category_id_ls[i], i , var_num_per_sentence, cond_inx_for_sentence,
                    problemValueStruct.useYear1_ls[i], problemValueStruct.useYear2_ls[i], problemValueStruct.useMult_ls[i], problemValueStruct.useAddMinus_ls[i],
                    problemValueStruct.var_sign_ls[var1_index+1], problemValueStruct.var_sign_ls[var1_index+2], problemValueStruct.var_sign_ls[var1_index+3]);  // sentence_ls[i] = {content, explanation}
        }

        /////////////////////////////////////////////////////////////////////////////////////////////////////
        // 상황 문장 연결
        String content = concateContent(sentence_ls, condition, question);
        String explanation = concateExplanation(sentence_ls, condition_inx);

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        // 결과
        problemValueStruct.content_template = content;
        problemValueStruct.explanation_template = explanation;
        problemValueStruct.answer_template = answer;

    }

    // 어떤 수 문제 템플릿 생성
    // useAddMinus = false로 고정 (함수 실행 전 setting에서 설정해야!!)
    // prob_sentence_num = 2로 고정 (인자로 받지 않고 함수 내부에서 상수로 이용
    public void unknownnumProblemTemplate() {
        final int prob_sentence_num = 2;  // 바르게 계산한 수 문장, 잘못 계산한 수 문장
        final int answer_inx = UNKNOWN_PROB_CORRECT_NUM_INDEX;    // 바르게 계산한 수
        final int condition_inx = UNKNOWN_PROB_WRONG_NUM_INDEX;   // 잘못 계산한 수
        final int var_num_per_sentence = UNKNOWNNUM_PROB_VAR_NUM_PER_SENTECE;

        // 바르게 계산한 수(name_var0), 잘못 계산한 수(name_var1), 어떤 수(name_var2)
        int[] name_var_index_in_correctNum_ls = new int[] {UNKNOWN_PROB_CORRECT_NUM_INDEX, UNKNOWN_PROB_X_INDEX};
        int[] name_var_index_in_wrongNum_ls = new int[] {UNKNOWN_PROB_WRONG_NUM_INDEX, UNKNOWN_PROB_X_INDEX};

     /*
     useYear1 = false로 고정
     즉, var3 = 0, var7 = 0 으로 고정
     y = (x +- var4) * var1 -+ var3

    // 어떤 수
    네모(name_var3) = (어떤 수(name_var1) +- var8) * var5 -+ var7        // 곱셈 : 2, 0   (함수의 name_var_index1, name_var_index2 인자 값)

    네모(name_var3) = (잘못 계산한 수(name_var2) +- var4) * var1 -+ var3    // 곱셈 : 2, 1

    // 상황문장 2개
    {name0}의 나이는 {name1}의 나이의 {var0}배 한 것보다 {var1}살 많습니다.
    year_token = "에"
    time_after_token = "를 더한 후"
    time_before_token = "를 뺀 후"
    어떤 수, 잘못 계산한 수, 바르게 계산한 수
    배 한 것보다 -> 을 곱하고
    많습니다 -> 를 더해야 할 것을 / 더했습니다.
    적습니다 -> 를 빼야 할 것을 / 뺐습니다.

    {어떤 수}에 {var4}를 (더한/뺀) 후 {var1}을 곱하고 {var2}를 (더해야/빼야) 할 것을
        바르게 계산한 수 = (어떤 수 +-var4) * var1 +- var2        // 바르게, 어떤
    잘못해서 var8를 (더한/뺀) 후 var5을 곱하고 var6를 (더했/뺐)습니다.
        잘못 계산한 수 = (어떤 수 +- var8) * var5 +- var6    // 잘못, 어떤
    // 조건
    이때, 잘못 계산한 수의 값은 {name_var2}입니다.
    // 질문
    그렇다면 바르게 계산한 수의 값은 얼마입니까?

    // 답
    {name_var1}



     */

        ////////////////////////////////////////////////////////////////////////////////////////////////
        // 문장 생성

        // condition 문장 생성
        // 이때 {잘못 계산한 수}의 값은 {}입니다.
        String condition = "이때, {"+NAME_STR+condition_inx+"}의 값은 {"+ NAME_VAR_STR +condition_inx+"}입니다.";

        // question 문장 생성
        // 그렇다면 {바르게 계산한 수}의 값은 얼마입니까?
        String question = "그렇다면 {"+NAME_STR+answer_inx+"}의 값은 얼마입니까?";

        // answer 문장 생성
        String answer = "{"+ NAME_VAR_STR +answer_inx+"}";

        // 상황 문장 생성 : {content, explanation}
        String[][] sentence_ls = new String[prob_sentence_num][2];

        int cond_inx_for_sentence = UNKNOWN_PROB_WRONG_NUM_INDEX;  // 잘못 계산한 수가 given

        // 어떤 수 문장 생성
        int var1_index = 0;
        sentence_ls[0] = create_unknownnum_sentence(true, name_var_index_in_correctNum_ls[0], name_var_index_in_correctNum_ls[1],
                problemValueStruct.sentence_category_id_ls[0], 0 , var_num_per_sentence, cond_inx_for_sentence,
                problemValueStruct.useYear1_ls[0], problemValueStruct.useYear2_ls[0], problemValueStruct.useMult_ls[0], problemValueStruct.useAddMinus_ls[0],
                problemValueStruct.var_sign_ls[var1_index+1], problemValueStruct.var_sign_ls[var1_index+2], problemValueStruct.var_sign_ls[var1_index+3]);  // sentence_ls[i] = {content, explanation}


        // 잘못 계산한 수 문장 생성
        var1_index = var_num_per_sentence;
        sentence_ls[1] = create_unknownnum_sentence(false, name_var_index_in_wrongNum_ls[0], name_var_index_in_wrongNum_ls[1], problemValueStruct.sentence_category_id_ls[1], 1 , var_num_per_sentence, cond_inx_for_sentence,
                problemValueStruct.useYear1_ls[1], problemValueStruct.useYear2_ls[1], problemValueStruct.useMult_ls[1], problemValueStruct.useAddMinus_ls[1],
                problemValueStruct.var_sign_ls[var1_index+1], problemValueStruct.var_sign_ls[var1_index+2], problemValueStruct.var_sign_ls[var1_index+3]);  // sentence_ls[i] = {content, explanation}


        /////////////////////////////////////////////////////////////////////////////////////////////////////
        // 상황 문장 연결
        String content = concateContent(sentence_ls, condition, question);
        String explanation = concateExplanation(sentence_ls, condition_inx);

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        // 결과
        problemValueStruct.content_template = content;
        problemValueStruct.explanation_template = explanation;
        problemValueStruct.answer_template = answer;

    }


    //////////////////////////////////////////////////////////////////////////////////////////
    // 문장 연결
    private String concateContent(String[][] sentence_ls, String condition, String question){
        String content = "";
        for(int i = 0; i < sentence_ls.length; i++){ // 상황 문장 content - 순서 섞으면 난이도 올라감
            content += sentence_ls[i][0] + "\n";
        }
        content += condition + "\n" + question;
        return content;
    }
    private String concateExplanation(String[][] sentence_ls, int condition_inx){
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
        return explanation;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 상황 문장 생성
    //

    // 나이 문제 상황 문장 생성
    // create_age_sentence(유형id, 값 참조 시작하는 인덱스) ex) index = 2 -> name2, name3, age2, age3, var2, var3 사용
    private String[] create_age_sentence(int name_var_index1, int name_var_index2, int category_id, int index, int var_num_per_sentence, int cond_inx,
                                         boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus,
                                         int var_sign, int year1_sign, int year2_sign){
        String name_category_token = age_category_token; // "나이"
        String name_unit_token = age_unit_token;    // "살"
        String var34_unit_token = year_unit_token;  // "년"
        String after_str_token = time_after_token;  // "후"
        String before_str_token = time_before_token; // "전"


        if(category_id == CATEGORY_ID_YX){
            String content = create_ageProb_content_yx(name_var_index1, name_var_index2, index, var_num_per_sentence, cond_inx,
                    useYear1, useYear2, useMult, useAddMinus,
                    var_sign, year1_sign, year2_sign,
                    name_category_token, name_unit_token, var34_unit_token, after_str_token, before_str_token);
            String explanation = create_explanation_yx(content, name_var_index1, name_var_index2, index, var_num_per_sentence, cond_inx,
                    useYear1, useYear2, useMult, useAddMinus,
                    var_sign, year1_sign, year2_sign,
                    name_category_token, name_unit_token, var34_unit_token, after_str_token, before_str_token);
            return new String[] {content, explanation};               // {content, explanation, sign}
        } else if (category_id == CATEGORY_ID_SUM_DIFFERENCE){
            return create_sentence_sum_diff(index, var_num_per_sentence, cond_inx,
                    var_sign,
                    name_category_token, name_unit_token);   // {content, explanation, sign}
        } else{
            return null;
        }
    }


    // 어떤수 문제 상황 문장 생성
    // create_age_sentence(유형id, 값 참조 시작하는 인덱스) ex) index = 2 -> name2, name3, age2, age3, var2, var3 사용
    private String[] create_unknownnum_sentence(boolean isCorrectNumSentence, int name_var_index1, int name_var_index2, int category_id, int index, int var_num_per_sentence, int cond_inx,
                                                boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus,
                                                int var_sign, int year1_sign, int year2_sign){
        final String name_category_token = "값";
        final String name_unit_token = "";
        String var34_unit_token = "가 더해진";
        if(year2_sign == MINUS_SIGN) var34_unit_token = "가 빼어진";
        final String after_str_token = "";
        final String before_str_token = "";

        String content = create_UnknownNumProb_content_yx(isCorrectNumSentence, name_var_index1, name_var_index2,
                index, var_num_per_sentence, useYear1, useYear2, useMult, useAddMinus, var_sign, year1_sign);
        String explanation = create_explanation_yx(content, name_var_index1, name_var_index2, index, var_num_per_sentence, cond_inx,
                useYear1, useYear2, useMult, useAddMinus,
                var_sign, year1_sign, year2_sign,
                name_category_token, name_unit_token, var34_unit_token, after_str_token, before_str_token);
        return new String[] {content, explanation};
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

    // (name_var1 +- var3) = (name_var2 +- var4) * var1 +- var2
    // name_var2 given
    // return new int[] {name_var1, name_var2, var1, var2, var3, var4}
    private int[] getRandomYXValue(int given_name_var2, int var_sign, int year1_sign, int year2_sign,
                                   int name_var1_min_value, int name_var1_max_value,
                                   int var1_min_value, int var1_max_value,
                                   int var2_min_value, int var2_max_value,
                                   int var3_min_value, int var3_max_value,
                                   int var4_min_value, int var4_max_value,
                                   boolean useYear1, boolean useYear2, boolean useAddMinus, boolean useMult)
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
            if(useYear1 == false) {
                var3 = 0;
            }
            if(useYear2 == false) {
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



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 식 유형에 따른 상황문장 생성

    // (name_var1 +- var3) = (name_var2 +- var4) * var1 +- var2
    public String create_ageProb_content_yx(int name_var_index1, int name_var_index2,
                                            int ls_index, int var_num_per_sentence, int cond_inx,
                                            boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus,
                                            int var2_sign, int year1_sign, int year2_sign,
                                            String name_category_token, String name_unit_token, String var34_unit_token, String after_str_token, String before_str_token) {
        String name_chosa_token = ui_token;

        ArrayList<String> content_ls = new ArrayList<>(1);

        // index
        //int name_var_index1 = ls_index;
        //int name_var_index2 = name_var_index1 + 1;

        int var_index1 = ls_index * var_num_per_sentence;
        int var_index2 = var_index1 + 1;
        int year1_index = var_index1 + 2;
        int year2_index = var_index1 + 3;

        // token 조합
        String name1_token = VAR_START + NAME_STR + name_var_index1 + VAR_END;
        String name2_token = VAR_START + NAME_STR + name_var_index2 + VAR_END;
        String name1_with_category_token = name1_token + ui_token + blank_token +  name_category_token;
        String name2_with_category_token = name2_token + ui_token + blank_token +  name_category_token;
        String var1_token = VAR_START + VAR_STR + var_index1 + VAR_END;
        String var2_token = VAR_START + VAR_STR + var_index2 + VAR_END;
        String var3_token = VAR_START + VAR_STR + year1_index + VAR_END;    // year1
        String var4_token = VAR_START + VAR_STR + year2_index + VAR_END;   // year2

        String name1_add_str_token = "", name2_add_str_token = "";
        if(useYear1) {
            if (year1_sign == PLUS_SIGN) {
                name1_add_str_token = var3_token + var34_unit_token + blank_token + after_str_token + blank_token; // "{year1}년 후 "
            } else if (year1_sign == MINUS_SIGN) {
                name1_add_str_token = var3_token + var34_unit_token + blank_token + before_str_token + blank_token; // "{year1}년 전 "
            } else {
                System.out.println("ERROR:: invalid year sign");
            }
        }
        if(useYear2) {
            if(year2_sign == PLUS_SIGN){
                name2_add_str_token = var4_token + var34_unit_token + blank_token + after_str_token + blank_token; // "{year2}년 후 "
            } else if(year2_sign == MINUS_SIGN){
                name2_add_str_token = var4_token + var34_unit_token + blank_token + before_str_token + blank_token; // "{year2}년 전 "
            } else{
                System.out.println("ERROR:: invalid year sign");
            }
        }

        // content용 토큰
        String mult_str_token = bae_token;  // "배 한 것" "를 곱한 것"
        String c_mult_token = name_chosa_token + blank_token + var1_token + mult_str_token;  //"의 {var1}배 한 것" "에 {var1}를 곱한 것"
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

        return content;
    }

    public String create_UnknownNumProb_content_yx(boolean isCorrectNumSentence, int name_var_index1, int name_var_index2,
                                                   int ls_index, int var_num_per_sentence,
                                                   boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus,
                                                   int var2_sign, int year1_sign){
        ArrayList<String> content_ls = new ArrayList<>(1);

        int var_index1 = ls_index * var_num_per_sentence;
        int var_index2 = var_index1 + 1;
        int year1_index = var_index1 + 2;
        int year2_index = var_index1 + 3;

        // token 조합
        String name1_token = VAR_START + NAME_STR + name_var_index1 + VAR_END;
        String name2_token = VAR_START + NAME_STR + name_var_index2 + VAR_END;
        String var1_token = VAR_START + VAR_STR + var_index1 + VAR_END;
        String var2_token = VAR_START + VAR_STR + var_index2 + VAR_END;
        String var3_token = VAR_START + VAR_STR + year1_index + VAR_END;    // year1
        String var4_token = VAR_START + VAR_STR + year2_index + VAR_END;   // year2

        String var4_addMin_token = "더한";
        if(year1_sign == MINUS_SIGN){
            var4_addMin_token = "뺀";
        }

        String var2_addMin_token = "더해";
        if(var2_sign == MINUS_SIGN){
            var2_addMin_token = "빼";
        }
        if(isCorrectNumSentence == false){
            var2_addMin_token = "더했";
            if(var2_sign == MINUS_SIGN){
                var2_addMin_token = "뺐";
            }
        }

        String mult_token = "곱";
        String mult_to_am_token = "하고";
        String mult_to_end_token = "해";

        String end_correct_token = "야 할 것을";
        String end_wrong_token = "습니다.";
        String end_token = end_correct_token;
        if(isCorrectNumSentence == false){
            end_token = end_wrong_token;
        }
    /*
    {어떤 수}에 {var4}를 (더한/뺀) 후 {var1}을 곱하고 {var2}를 (더해/빼)야 할 것을
        잘못 계산한 수 = (어떤 수 +-var4) * var1 +- var2
    잘못해서 var8를 (더한/뺀) 후 var5을 곱하고 var6를 (더했/뺐)습니다.
        바르게 계산한 수 = (어떤 수 +- var8) * var5 +- var6
     */

        String content_pre_correct_token = name1_token + "에";
        String content_pre_wrong_token = "잘못해서";
        String addmin_var4_token = blank_token + var4_token + reul_token + blank_token + var4_addMin_token + blank_token + "후";
        String mult_var1_token = blank_token + var1_token + eul_token + blank_token + mult_token;
        String addmin_var2_token = blank_token + var2_token + reul_token + blank_token + var2_addMin_token;
        String content = "";
        if(isCorrectNumSentence){
            content += content_pre_correct_token;
        } else{
            content += content_pre_wrong_token;
        }
        if(useYear2){
            content += addmin_var4_token;
        }
        if(useMult){
            content += mult_var1_token;
        }
        if(useMult && useAddMinus){
            content += mult_to_am_token;
        }
        if(useAddMinus){
            content += addmin_var2_token;
        }
        content += end_token;

        return content;
    }


    public String create_explanation_yx(String content, int name_var_index1, int name_var_index2,
                                        int ls_index, int var_num_per_sentence, int cond_inx,
                                        boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus,
                                        int var2_sign, int year1_sign, int year2_sign,
                                        String name_category_token, String name_unit_token, String var34_unit_token, String after_str_token, String before_str_token){
        String name_chosa_token = ui_token;

        ArrayList<String> explanation_ls = new ArrayList<>(10);

        // index
        //int name_var_index1 = ls_index;
        //int name_var_index2 = name_var_index1 + 1;

        int var_index1 = ls_index * var_num_per_sentence;
        int var_index2 = var_index1 + 1;
        int year1_index = var_index1 + 2;
        int year2_index = var_index1 + 3;

        // token 조합
        String name1_token = VAR_START + NAME_STR + name_var_index1 + VAR_END;
        String name2_token = VAR_START + NAME_STR + name_var_index2 + VAR_END;
        String name1_with_category_token = name1_token + ui_token + blank_token +  name_category_token;
        String name2_with_category_token = name2_token + ui_token + blank_token +  name_category_token;
        String name_var1_token = VAR_START + NAME_VAR_STR + name_var_index1 + VAR_END;
        String name_var2_token = VAR_START + NAME_VAR_STR + name_var_index2 + VAR_END;
        String var1_token = VAR_START + VAR_STR + var_index1 + VAR_END;
        String var2_token = VAR_START + VAR_STR + var_index2 + VAR_END;
        String var3_token = VAR_START + VAR_STR + year1_index + VAR_END;    // year1
        String var4_token = VAR_START + VAR_STR + year2_index + VAR_END;   // year2

        String name1_add_str_token = "", name2_add_str_token = "";
        if(useYear1) {
            if (year1_sign == PLUS_SIGN) {
                name1_add_str_token = var3_token + var34_unit_token + blank_token + after_str_token + blank_token; // "{year1}년 후 "
            } else if (year1_sign == MINUS_SIGN) {
                name1_add_str_token = var3_token + var34_unit_token + blank_token + before_str_token + blank_token; // "{year1}년 전 "
            } else {
                System.out.println("ERROR:: invalid year sign");
            }
        }
        if(useYear2) {
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

        String mult_str_token = bae_token;  // "배 한 것" "를 곱한 것"
        String c_mult_token = name_chosa_token + blank_token + var1_token + mult_str_token;  //"의 {var1}배 한 것" "에 {var1}를 곱한 것"
        String c_mult_end_token = gwa_token + blank_token + same_token; //"과 같습니다.";
        String c_mult_end_wa_token = wa_token + blank_token + same_token; //"와 같습니다.";
        String c_pm_token = than_token + blank_token + var2_token + name_unit_token + blank_token;   //"보다 " + var2_token + "살 ";


        // explanation token
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
        if(useYear1 == false){
            expr_add_minus_var3_token = "";
            expr_add_minus_blank_var3_token = "";
        }
        if(useYear2 == false){
            expr_add_minus_var4_token = "";
            expr_add_minus_blank_var4_token = "";
        }


        String ex_expression_str_start_token = "\"" + content + "\"라는 문장을 식으로 바꾸면";
        String ex_expression_token = name1_add_str_token + name1_with_category_token
                + EQUAL_NL_BLANK_SYM + name2_add_str_token + name2_with_category_token + mult_blank_var1_token + add_minus_blank_var2_token;
        String ex_expression_str_end_token = "가 됩니다.";

        String ex_explain_start_token = "이제 이 식을 다음과 같은 순서로 풀 수 있습니다.";

        // name_var -> name_var +- year   또는   name_var 값 선언
        String ex_name_var1_after_year_token = name1_add_str_token + name1_with_category_token
                + EQUAL_NL_BLANK_SYM + name_var1_token + expr_add_minus_blank_var3_token;
        String ex_name_var2_after_year_token = name2_add_str_token + name2_with_category_token
                + EQUAL_NL_BLANK_SYM + name_var2_token + expr_add_minus_blank_var4_token;
        if(useYear1){
            ex_name_var1_after_year_token += EQUAL_NL_BLANK_SYM + EXPRESSION_START + name_var1_token + expr_add_minus_var3_token + EXPRESSION_END;
        }
        if(useYear2){
            ex_name_var2_after_year_token += EQUAL_NL_BLANK_SYM + EXPRESSION_START + name_var2_token + expr_add_minus_var4_token + EXPRESSION_END;
        }


        String expr_name_var1_token = BRACKET_START + name_var2_token + expr_add_minus_var4_token + BRACKET_END + mult_var1_token + add_minus_var2_token;
        String expr_name_var2_token = BRACKET_START + name_var1_token + expr_add_minus_var3_token + inv_add_minus_var2_token + BRACKET_END + DIVIDE_SYM + var1_token;


        // if var2 != 0
        // (year년 후 name1의 나이) -+ var2 = age1 + year -+ var2
        String ex_cond1_compute_b4_divide_token = name1_add_str_token + name1_with_category_token + inv_add_minus_blank_var2_token
                + EQUAL_NL_BLANK_SYM + EXPRESSION_START + name_var1_token + expr_add_minus_var3_token + inv_add_minus_var2_token + EXPRESSION_END;
        String ex_eq_name_var2_token = EQUAL_NL_BLANK_SYM + name2_add_str_token + name2_with_category_token;
        String expr_cond1_computed_b4_divide_token = name_var1_token + expr_add_minus_var3_token + inv_add_minus_var2_token;
        // if var1 != 1
        //"(%s%s의 나이) = {age1 + year -+ var2} / var1 = %d\n"
        // age1 given, find age2
        String ex_cond1_compute_divide_token = name2_add_str_token + name2_with_category_token
                + EQUAL_NL_BLANK_SYM + EXPRESSION_START + expr_cond1_computed_b4_divide_token + EXPRESSION_END + DIVIDE_BLANK_STR + var1_token
                + EQUAL_NL_BLANK_SYM + EXPRESSION_START + BRACKET_START + expr_cond1_computed_b4_divide_token + BRACKET_END + DIVIDE_SYM + var1_token + EXPRESSION_END;

        // (%s%s의 나이) = (%s%s의 나이) * %d %s %d = %d * %d %s %d = %d
        // y년후 name1의 나이 = y년후 name2의나이 * var1 +- var2 = [age2+year] * var1 +- var2 = age1
        // age2 given, find age1
        String ex_cond2_compute_token = name1_add_str_token + name1_with_category_token
                + EQUAL_NL_BLANK_SYM + name2_add_str_token + name2_with_category_token + mult_blank_var1_token + add_minus_blank_var2_token
                + EQUAL_NL_BLANK_SYM + EXPRESSION_START + name_var2_token + expr_add_minus_var4_token + EXPRESSION_END + mult_blank_var1_token + add_minus_blank_var2_token;
        if(!(useMult == false && useAddMinus == false)){
            ex_cond2_compute_token += EQUAL_NL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var2_token + expr_add_minus_var4_token + BRACKET_END + mult_var1_token + add_minus_var2_token + EXPRESSION_END;
        }

        // name_var +- year -> name_var
        String ex_after_year_to_name_var1_token = name1_with_category_token
                + EQUAL_NL_BLANK_SYM + name1_add_str_token + name1_with_category_token + var3_inv_sign_blank_token + var3_token
                + EQUAL_NL_BLANK_SYM + EXPRESSION_START + expr_name_var1_token + EXPRESSION_END + var3_inv_sign_blank_token + var3_token
                + EQUAL_NL_BLANK_SYM + EXPRESSION_START + expr_name_var1_token + var3_inv_sign_token + var3_token + EXPRESSION_END;
        String ex_after_year_to_name_var2_token = name2_with_category_token
                + EQUAL_NL_BLANK_SYM + name2_add_str_token + name2_with_category_token + var4_inv_sign_blank_token + var4_token
                + EQUAL_NL_BLANK_SYM + EXPRESSION_START + expr_name_var2_token + EXPRESSION_END + var4_inv_sign_blank_token + var4_token
                + EQUAL_NL_BLANK_SYM + EXPRESSION_START + expr_name_var2_token + var4_inv_sign_token + var4_token + EXPRESSION_END;



        // explanation
        String explanation="";

        // ~라는 문장을 식으로 바꾸면 ~~가 됩니다.
        explanation_ls.add(ex_expression_str_start_token);
        explanation_ls.add(ex_expression_token);
        explanation_ls.add(ex_expression_str_end_token);

        // 이제 이 식을 다음과 같은 순서로 풀 수 있습니다.
        explanation_ls.add(ex_explain_start_token);

        // 풀이 과정 설명
        if(cond_inx == 0){  // age1 given, find age2
            if(useMult==false && useAddMinus == false){
                explanation_ls.add(ex_name_var1_after_year_token + ex_eq_name_var2_token);
            } else {
                explanation_ls.add(ex_name_var1_after_year_token);
            }

            if(useAddMinus) {
                if(useMult==false){
                    explanation_ls.add(ex_cond1_compute_b4_divide_token + ex_eq_name_var2_token);
                } else{
                    explanation_ls.add(ex_cond1_compute_b4_divide_token);
                }
            }

            if(useMult) {
                explanation_ls.add(ex_cond1_compute_divide_token);
            }

            if(useYear2) {
                explanation_ls.add(ex_after_year_to_name_var2_token);
            }

        } else{ // age2 given, find age1
            explanation_ls.add(ex_name_var2_after_year_token);
            explanation_ls.add(ex_cond2_compute_token);
            if(useYear1) {
                explanation_ls.add(ex_after_year_to_name_var1_token);
            }
        }


        explanation = explanation_ls.get(0);
        for(int i = 1; i < explanation_ls.size(); i++){
            explanation += "\n\n" + explanation_ls.get(i);
        }
        return explanation;     // {content, explanation}
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
                + EQUAL_NL_BLANK_SYM + var1_token + inv_sign_blank_token + name2_with_category_token
                + EQUAL_NL_BLANK_SYM + var1_token + inv_sign_blank_token + name_var2_token
                + EQUAL_NL_BLANK_SYM + EXPRESSION_START + var1_token + inv_sign_token + name_var2_token + EXPRESSION_END;
        String ex_name_var2_with_sum_token = name2_with_category_token
                + EQUAL_NL_BLANK_SYM + var1_token + MINUS_BLANK_STR + name1_with_category_token
                + EQUAL_NL_BLANK_SYM + var1_token + MINUS_BLANK_STR + name_var1_token
                + EQUAL_NL_BLANK_SYM + EXPRESSION_START + var1_token + MINUS_SYM + name_var1_token + EXPRESSION_END;
        String ex_name_var2_with_diff_token = name2_with_category_token
                + EQUAL_NL_BLANK_SYM + name1_with_category_token + MINUS_BLANK_STR + var1_token
                + EQUAL_NL_BLANK_SYM + name_var1_token + MINUS_BLANK_STR + var1_token
                + EQUAL_NL_BLANK_SYM + EXPRESSION_START + name_var1_token + MINUS_SYM + var1_token + EXPRESSION_END;

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

    public String create_sum_diff_content(int ls_index, int var_num_per_sentence, int cond_inx, int sign,
                                          String name_category_token){
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

        return content;
    }


    public String create_sum_diff_explanation(int ls_index, int var_num_per_sentence, int cond_inx, int sign,
                                              String name_category_token, String content){
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
                + EQUAL_NL_BLANK_SYM + var1_token + inv_sign_blank_token + name2_with_category_token
                + EQUAL_NL_BLANK_SYM + var1_token + inv_sign_blank_token + name_var2_token
                + EQUAL_NL_BLANK_SYM + EXPRESSION_START + var1_token + inv_sign_token + name_var2_token + EXPRESSION_END;
        String ex_name_var2_with_sum_token = name2_with_category_token
                + EQUAL_NL_BLANK_SYM + var1_token + MINUS_BLANK_STR + name1_with_category_token
                + EQUAL_NL_BLANK_SYM + var1_token + MINUS_BLANK_STR + name_var1_token
                + EQUAL_NL_BLANK_SYM + EXPRESSION_START + var1_token + MINUS_SYM + name_var1_token + EXPRESSION_END;
        String ex_name_var2_with_diff_token = name2_with_category_token
                + EQUAL_NL_BLANK_SYM + name1_with_category_token + MINUS_BLANK_STR + var1_token
                + EQUAL_NL_BLANK_SYM + name_var1_token + MINUS_BLANK_STR + var1_token
                + EQUAL_NL_BLANK_SYM + EXPRESSION_START + name_var1_token + MINUS_SYM + var1_token + EXPRESSION_END;

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

        return explanation;
    }


}
