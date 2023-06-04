package com.onejo.seosuri.service.algorithm.saveTemplate;

import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;
import com.onejo.seosuri.service.algorithm.exprCategory.ExprCategory;
import com.onejo.seosuri.service.algorithm.exprCategory.YXUnkownNumExprCategory;
import com.onejo.seosuri.service.algorithm.createTemplate.CreateUnknownNumTemplate;
import com.onejo.seosuri.service.algorithm.problem.ProblemValueStruct;

import java.util.ArrayList;

public class SaveAllUnknownNumTemplates extends SaveAllTemplates{

    public SaveAllUnknownNumTemplates(int[] category_id_ls, ProblemValueStruct problemValueStruct) {
        super(category_id_ls, problemValueStruct, new CreateUnknownNumTemplate(problemValueStruct));
        possible_Expr_category_ls = new ExprCategory[] {new YXUnkownNumExprCategory()};
    }

    @Override
    public void saveAllTemplates() {
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
        final int var_num_per_sentence = ProblemTokenStruct.UNKNOWNNUM_PROB_VAR_NUM_PER_SENTECE;
        final int prob_sentence_num = 2;
        final int answer_inx = ProblemTokenStruct.UNKNOWN_PROB_CORRECT_NUM_INDEX;    // 바르게 계산한 수
        final int condition_inx = ProblemTokenStruct.UNKNOWN_PROB_WRONG_NUM_INDEX;   // 잘못 계산한 수
        final int name_var_num = prob_sentence_num + 1;
        final int var_num = prob_sentence_num * var_num_per_sentence;
        int[] inx_ls = new int[prob_sentence_num];
        for(int i = 0; i < prob_sentence_num; i++){
            inx_ls[i] = i;
        }
        setCategory_ls_ls(prob_sentence_num);
        setSentence_category_id_ls_ls(prob_sentence_num);   // category_num(2)^prob_sentence_num
        set_useBoolean_ls_ls(prob_sentence_num);    // 2^prob_sentence_num
        setVar_sign_ls_ls(var_num);                 // 2^var_num = 2^(prob_sentence_num*va4_num_per_sentence(=4)) = 16 * 2^prob_sentence_num)
        problemValueStruct.useYear1_ls = new boolean[prob_sentence_num];
        problemValueStruct.useYear2_ls = new boolean[prob_sentence_num];
        problemValueStruct.sentence_category_id_ls = new int[prob_sentence_num];
        for(int i = 0; i < prob_sentence_num; i++){
            problemValueStruct.useYear1_ls[i] = false;
            problemValueStruct.useYear2_ls[i] = true;
            problemValueStruct.sentence_category_id_ls[i] = ProblemTokenStruct.CATEGORY_ID_YX;
        }

        for(int c_inx=0; c_inx<sentence_category_id_ls_ls.length; c_inx++) {    // 2^prob_sentence_num
            problemValueStruct.sentence_category_id_ls = sentence_category_id_ls_ls[c_inx].stream().mapToInt(i -> i).toArray();
            problemValueStruct.exprCategory_ls = arrayListToCategoryArray(category_ls_ls[c_inx]);
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
                            if ((problemValueStruct.useYear1_ls[i] == false && problemValueStruct.var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_YEAR_VAR1_OFFSET] == ProblemTokenStruct.MINUS_SIGN)  // year1 사용하지 않는 경우, year sign에 따른 변화 무시
                                    || (problemValueStruct.useYear2_ls[i] == false && problemValueStruct.var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_YEAR_VAR2_OFFSET] == ProblemTokenStruct.MINUS_SIGN)   // year2 사용하지 않는 경우, year sign에 따른 변화 무시
                                    || (problemValueStruct.useMult_ls[i] == false && (problemValueStruct.var_sign_ls[i * var_num_per_sentence] == ProblemTokenStruct.MINUS_SIGN)) // mult 사용하지 않는 경우, mult sign 에 따른 변화 무시
                                    || (problemValueStruct.useAddMinus_ls[i] == false && (problemValueStruct.var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_ADDMIN_VAR_OFFSET] == ProblemTokenStruct.MINUS_SIGN))) // addminus 사용하지 않는 경우, addminus sign 에 따른 변화 무시
                            {
                                generateTemplate = false;
                                break;
                            }
                        }

                        // template 생성, DB에 저장
                        if (generateTemplate) {
                            //unknownnumProblemTemplate();
                            createTemplate.createOneTemplate(prob_sentence_num, var_num_per_sentence, condition_inx, answer_inx);
                            saveInDB();
                            template_id++;
                            //System.out.println("" + template_id);
                        }
                    }
                }
            }
        }
    }

}
