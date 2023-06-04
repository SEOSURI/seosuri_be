package com.onejo.seosuri.service.algorithm.createTemplate;

import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;
import com.onejo.seosuri.service.algorithm.exprCategory.ExprCategory;
import com.onejo.seosuri.service.algorithm.problem.ProblemValueStruct;

public class CreateAgeTemplate extends CreateTemplate{

    public CreateAgeTemplate(ProblemValueStruct problemValueStruct){
        super(problemValueStruct);
    }


    @Override
    public void createOneTemplate(int prob_sentence_num, int var_num_per_sentence, int answer_inx, int condition_inx) {
        ////////////////////////////////////////////////////////////////////////////////////////////////
        // 문장 생성

        // condition 문장 생성
        String condition = "이때, {"+ ProblemTokenStruct.NAME_STR+condition_inx+"}의 나이는 {"+ ProblemTokenStruct.NAME_VAR_STR +condition_inx+"}살 입니다.";

        // question 문장 생성
        String question = "그렇다면 {"+ ProblemTokenStruct.NAME_STR+answer_inx+"}의 나이는 몇 살입니까?";

        // answer 문장 생성
        String answer = "{"+ ProblemTokenStruct.NAME_VAR_STR +answer_inx+"}살";

        // 상황 문장 생성 : {content, explanation}
        String[][] sentence_ls = new String[prob_sentence_num][2];
        // condition index for sentence
        //      0~cond_inx -> age2 given
        //      cond_inx+1~ -> age1 given
        int cond_inx_for_sentence = 1;  // age1, age2 중 age2가 given
        if(prob_sentence_num==1)    cond_inx_for_sentence = condition_inx;
        for(int i = 0; i < condition_inx; i++){ // 0~cond_inx -> age2 given
            int var1_index = i * var_num_per_sentence;
            /*
            Category category, boolean isCorrectNumSentence,
                                   int name_var_index1, int name_var_index2, int category_id, int index,
                                   int var_num_per_sentence, int cond_inx,
                                   boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus,
                                   int var_sign, int year1_sign, int year2_sign
             */
            sentence_ls[i] = this.createSentence(problemValueStruct.exprCategory_ls[i], true, i, i+1, problemValueStruct.sentence_category_id_ls[i], i , var_num_per_sentence, cond_inx_for_sentence,
                    problemValueStruct.useYear1_ls[i], problemValueStruct.useYear2_ls[i], problemValueStruct.useMult_ls[i], problemValueStruct.useAddMinus_ls[i],
                    problemValueStruct.var_sign_ls[var1_index+1], problemValueStruct.var_sign_ls[var1_index+2], problemValueStruct.var_sign_ls[var1_index+3]);  // sentence_ls[i] = {content, explanation}
        }
        cond_inx_for_sentence = 0;  // age1, age2 중 age1가 given
        for(int i = condition_inx; i < prob_sentence_num; i++){ // cond_inx+1~ -> age1 given
            int var1_index = i * var_num_per_sentence;
            sentence_ls[i] = createSentence(problemValueStruct.exprCategory_ls[i], true, i, i+1, problemValueStruct.sentence_category_id_ls[i], i , var_num_per_sentence, cond_inx_for_sentence,
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

    @Override
    public String[] createSentence(ExprCategory exprCategory, boolean isCorrectNumSentence,
                                   int name_var_index1, int name_var_index2, int category_id, int index,
                                   int var_num_per_sentence, int cond_inx,
                                   boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus,
                                   int var_sign, int year1_sign, int year2_sign) {
        String name_category_token = ProblemTokenStruct.age_category_token; // "나이"
        String name_unit_token = ProblemTokenStruct.age_unit_token;    // "살"
        String var34_unit_token = ProblemTokenStruct.year_unit_token;  // "년"
        String after_str_token = ProblemTokenStruct.time_after_token;  // "후"
        String before_str_token = ProblemTokenStruct.time_before_token; // "전"

        String content = exprCategory.createSentenceContent(isCorrectNumSentence, name_var_index1, name_var_index2,
                index, var_num_per_sentence, useYear1, useYear2, useMult, useAddMinus, var_sign, year1_sign, year2_sign, name_category_token, name_unit_token, var34_unit_token, after_str_token, before_str_token);
        String explanation = exprCategory.createSentenceExplanation(content, name_var_index1, name_var_index2, index, var_num_per_sentence, cond_inx,
                useYear1, useYear2, useMult, useAddMinus,
                var_sign, year1_sign, year2_sign,
                name_category_token, name_unit_token, var34_unit_token, after_str_token, before_str_token);
        return new String[] {content, explanation};
    }
}
