// 이곳에 문제 생성 알고리즘을 넣어두고, ProblemSerice에서 필요한 method를 호출하면 될 듯 싶습니다..

package com.onejo.seosuri.service.algorithm;

import com.onejo.seosuri.service.algorithm.problem.ProblemValueStruct;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

/*
남은 일
    saveAgeProblemTemplates() 코드 작성

    // Problem Service로 옮길 메소드에서 해야되는 일
    ageProblem() DB에서 값 가져오는 코드 작성
    setNameAndNameVarRange() DB에서 name, name_var범위 값 가져오게 수정
 */


public class ProblemTokenStruct {
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

}