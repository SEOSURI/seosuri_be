package com.onejo.seosuri.service.algorithm.problem;

import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;
import com.onejo.seosuri.service.algorithm.ProblemValueStruct;

import org.hibernate.usertype.BaseUserTypeSupport;

public class CreateAgeProblem extends CreateProblem{

    public CreateAgeProblem(ProblemValueStruct problemValueStruct) {
        super(problemValueStruct);
    }


    // 나이 구하기 문제 알고리즘 -> 실제 문제 생성: 결과: varElementary5th.real_content, real_explanation, real_answer에 저장됨
    @Override
    public void createProblem(int level) throws BusinessException {
        int prob_sentence_num = level;  // 상황 문장 갯수 - 문제 난이도에 따라 값 달라짐
        int var_num_per_sentence = ProblemTokenStruct.AGE_PROB_VAR_NUM_PER_SENTENCE;

        int variable_var_num = prob_sentence_num + 1;
        int constant_var_num = prob_sentence_num * var_num_per_sentence;

        createProblem(prob_sentence_num, constant_var_num, variable_var_num, var_num_per_sentence);
    }


}
