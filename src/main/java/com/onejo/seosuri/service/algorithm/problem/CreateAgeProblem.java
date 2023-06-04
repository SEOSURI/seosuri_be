package com.onejo.seosuri.service.algorithm.problem;

import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;

import java.util.Random;

public class CreateAgeProblem extends CreateProblem{

    public CreateAgeProblem(ProblemValueStruct problemValueStruct) {
        super(problemValueStruct);
    }

    // 나이 구하기 문제 알고리즘 -> 실제 문제 생성: 결과: varElementary5th.real_content, real_explanation, real_answer에 저장됨
    @Override
    public void createProblem(int level){
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
        int var_num_per_sentence = ProblemTokenStruct.AGE_PROB_VAR_NUM_PER_SENTENCE;

        Random random = new Random(); //랜덤 객체 생성(디폴트 시드값 : 현재시간)
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


}
