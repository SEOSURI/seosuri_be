package com.onejo.seosuri.service.algorithm.createTemplate;

import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;
import com.onejo.seosuri.service.algorithm.category.Category;
import com.onejo.seosuri.service.algorithm.problem.ProblemValueStruct;

public class CreateUnknownNumTemplate extends CreateTemplate{


    CreateUnknownNumTemplate(ProblemValueStruct problemValueStruct) {
        super(problemValueStruct);
    }

    @Override
    public void createOneTemplate(int prob_sentence_num, int var_num_per_sentence, int answer_inx, int condition_inx) {

        prob_sentence_num = 2;  // 바르게 계산한 수 문장, 잘못 계산한 수 문장
        answer_inx = ProblemTokenStruct.UNKNOWN_PROB_CORRECT_NUM_INDEX;    // 바르게 계산한 수
        condition_inx = ProblemTokenStruct.UNKNOWN_PROB_WRONG_NUM_INDEX;   // 잘못 계산한 수
        var_num_per_sentence = ProblemTokenStruct.UNKNOWNNUM_PROB_VAR_NUM_PER_SENTECE;

        // 바르게 계산한 수(name_var0), 잘못 계산한 수(name_var1), 어떤 수(name_var2)
        int[] name_var_index_in_correctNum_ls = new int[] {ProblemTokenStruct.UNKNOWN_PROB_CORRECT_NUM_INDEX, ProblemTokenStruct.UNKNOWN_PROB_X_INDEX};
        int[] name_var_index_in_wrongNum_ls = new int[] {ProblemTokenStruct.UNKNOWN_PROB_WRONG_NUM_INDEX, ProblemTokenStruct.UNKNOWN_PROB_X_INDEX};

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
        String condition = "이때, {"+ ProblemTokenStruct.NAME_STR+condition_inx+"}의 값은 {"+ ProblemTokenStruct.NAME_VAR_STR +condition_inx+"}입니다.";

        // question 문장 생성
        // 그렇다면 {바르게 계산한 수}의 값은 얼마입니까?
        String question = "그렇다면 {"+ ProblemTokenStruct.NAME_STR+answer_inx+"}의 값은 얼마입니까?";

        // answer 문장 생성
        String answer = "{"+ ProblemTokenStruct.NAME_VAR_STR +answer_inx+"}";

        // 상황 문장 생성 : {content, explanation}
        String[][] sentence_ls = new String[prob_sentence_num][2];

        int cond_inx_for_sentence = ProblemTokenStruct.UNKNOWN_PROB_WRONG_NUM_INDEX;  // 잘못 계산한 수가 given

        // 어떤 수 문장 생성
        int var1_index = 0;
        sentence_ls[0] = createSentence(problemValueStruct.category_ls[0], true, name_var_index_in_correctNum_ls[0], name_var_index_in_correctNum_ls[1],
                problemValueStruct.sentence_category_id_ls[0], 0 , var_num_per_sentence, cond_inx_for_sentence,
                problemValueStruct.useYear1_ls[0], problemValueStruct.useYear2_ls[0], problemValueStruct.useMult_ls[0], problemValueStruct.useAddMinus_ls[0],
                problemValueStruct.var_sign_ls[var1_index+1], problemValueStruct.var_sign_ls[var1_index+2], problemValueStruct.var_sign_ls[var1_index+3]);  // sentence_ls[i] = {content, explanation}


        // 잘못 계산한 수 문장 생성
        var1_index = var_num_per_sentence;
        sentence_ls[1] = createSentence(problemValueStruct.category_ls[1], false, name_var_index_in_wrongNum_ls[0], name_var_index_in_wrongNum_ls[1], problemValueStruct.sentence_category_id_ls[1], 1 , var_num_per_sentence, cond_inx_for_sentence,
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

    @Override
    public String[] createSentence(Category category, boolean isCorrectNumSentence, int name_var_index1, int name_var_index2, int category_id, int index, int var_num_per_sentence, int cond_inx, boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus, int var_sign, int year1_sign, int year2_sign) {
        final String name_category_token = "값";
        final String name_unit_token = "";
        String var34_unit_token = "가 더해진";
        if(year2_sign == ProblemTokenStruct.MINUS_SIGN) var34_unit_token = "가 빼어진";
        final String after_str_token = "";
        final String before_str_token = "";

        String content = category.createContent(isCorrectNumSentence, name_var_index1, name_var_index2,
                index, var_num_per_sentence, useYear1, useYear2, useMult, useAddMinus, var_sign, year1_sign, ProblemTokenStruct.PLUS_SIGN, "", "", "", "", "");
        String explanation = category.createExplanation(content, name_var_index1, name_var_index2, index, var_num_per_sentence, cond_inx,
                useYear1, useYear2, useMult, useAddMinus,
                var_sign, year1_sign, year2_sign,
                name_category_token, name_unit_token, var34_unit_token, after_str_token, before_str_token);
        return new String[] {content, explanation};
    }

}
