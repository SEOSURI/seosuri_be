package com.onejo.seosuri.service.algorithm.problem;

import com.onejo.seosuri.service.algorithm.category.Category;

import java.util.Arrays;

public class ProblemValueStruct {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // template
    public String content_template = "";
    public String explanation_template = "";
    public String answer_template = "";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 실제 문제
    public String real_content = "";
    public String real_explanation = "";
    public String real_answer = "";

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 템플릿 생성시만 이용

    // boolean 변수값 template 생성시 이용
    public boolean[] useYear1_ls;
    public boolean[] useYear2_ls;
    public boolean[] useMult_ls;
    public boolean[] useAddMinus_ls;


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // template 생성, 실제 문제 생성 시 이용

    // 상황문장 유형id
    public int[] sentence_category_id_ls;
    public Category[] category_ls;

    // sign
    public int[] var_sign_ls;  // input


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 실제 문제 생성 시에만 이용

    // min, max 범위
    public int[] constant_var_min_value_ls; // input - 상수
    public int[] constant_var_max_value_ls; // input - 상수
    public int[] variant_var_min_value_ls;    // input - 이름
    public int[] variant_var_max_value_ls;    // input - 이름

    // 이름
    public String[] variant_var_string_ls;

    // 숫자값
    public int[] variant_var_ls;   // 결과
    public int[] constant_var_ls;   // 결과

    public void printTemplate(){
        System.out.println("CONTENT ------------------------------------");
        System.out.println(content_template);
        System.out.println("EXPLANATION ------------------------------------");
        System.out.println(explanation_template);
        System.out.println("ANSWER ------------------------------------");
        System.out.println(answer_template);
        System.out.println("SENTENCE_CATEGORY_ID ------------------------------");
        System.out.println(Arrays.toString(sentence_category_id_ls));
        System.out.println("VAR_SIGN ------------------------------------");
        System.out.println(Arrays.toString(var_sign_ls));
        System.out.println("\n\n");
    }
    public void printProblem(){
        System.out.println("CONTENT ------------------------------------");
        System.out.println(real_content);
        System.out.println("EXPLANATION ------------------------------------");
        System.out.println(real_explanation);
        System.out.println("ANSWER ------------------------------------");
        System.out.println(real_answer);
        System.out.println("SENTENCE_CATEGORY_ID ------------------------------");
        System.out.println(Arrays.toString(sentence_category_id_ls));
        System.out.println("VAR_SIGN ------------------------------------");
        System.out.println(Arrays.toString(var_sign_ls));
        System.out.println("\n\n");
    }



}
