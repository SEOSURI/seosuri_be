package com.onejo.seosuri.service.algorithm.problem;

import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;

public class CreateUnknownNumProblem extends CreateProblem{
    public CreateUnknownNumProblem(ProblemValueStruct problemValueStruct) {
        super(problemValueStruct);
    }

    @Override
    public void createProblem(int level)  {
        int prob_sentence_num = 2;  // 잘못 계산한 수, 바르게 계산한 수
        int var_num_per_sentence = ProblemTokenStruct.UNKNOWNNUM_PROB_VAR_NUM_PER_SENTECE;

        int variable_var_num = prob_sentence_num + 1;   // 잘못 계산한 수, 바르게 계산한 수
        int constant_var_num = prob_sentence_num * var_num_per_sentence;

        createProblem(prob_sentence_num, constant_var_num, variable_var_num, var_num_per_sentence);
    }


    @Override
    protected void setVariantVarMinMaxLsAndStringLs(int name_var_num){
        problemValueStruct.variant_var_min_value_ls = new int[name_var_num];
        problemValueStruct.variant_var_max_value_ls = new int[name_var_num];
        problemValueStruct.variant_var_string_ls = new String[name_var_num];
        if(name_var_num == 3){
            problemValueStruct.variant_var_string_ls[ProblemTokenStruct.UNKNOWN_PROB_X_INDEX] = "어떤 수";
            problemValueStruct.variant_var_string_ls[ProblemTokenStruct.UNKNOWN_PROB_CORRECT_NUM_INDEX] = "바르게 계산한 수";
            problemValueStruct.variant_var_string_ls[ProblemTokenStruct.UNKNOWN_PROB_WRONG_NUM_INDEX] = "잘못 계산한 수";
            for(int i = 0; i < name_var_num; i++){
                problemValueStruct.variant_var_min_value_ls[i] = 1;
                problemValueStruct.variant_var_max_value_ls[i] = 100;
            }
        } else {
            System.out.println("ERROR :: name_var_num should be 3 in unknown num problem");
        }
    }

}
