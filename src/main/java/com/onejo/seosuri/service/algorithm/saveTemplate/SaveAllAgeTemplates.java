package com.onejo.seosuri.service.algorithm.saveTemplate;

import com.onejo.seosuri.controller.dto.template.TemplateDto;
import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;
import com.onejo.seosuri.service.algorithm.exprCategory.ExprCategory;
import com.onejo.seosuri.service.algorithm.exprCategory.SumDiffExprCategory;
import com.onejo.seosuri.service.algorithm.exprCategory.YXAgeExprCategory;
import com.onejo.seosuri.service.algorithm.createTemplate.CreateAgeTemplate;
import com.onejo.seosuri.service.algorithm.ProblemValueStruct;

import java.util.ArrayList;

public class SaveAllAgeTemplates extends SaveAllTemplates{


    public SaveAllAgeTemplates() {
        super(new int[] {ProblemTokenStruct.EXPR_CATEGORY_ID_SUM_DIFFERENCE, ProblemTokenStruct.EXPR_CATEGORY_ID_YX});
        possible_Expr_category_ls = new ExprCategory[] {new SumDiffExprCategory(), new YXAgeExprCategory()};

        problemValueStruct = new ProblemValueStruct();
        createTemplate = new CreateAgeTemplate(problemValueStruct);
    }

    @Override
    public void saveAllTemplates(int start_template_id, int end_template_id) {
        int template_id = 0;    // template_id = 0, 1, 2, ...
        int var_num_per_sentence = ProblemTokenStruct.AGE_PROB_VAR_NUM_PER_SENTENCE;

        for(int prob_sentence_num: new int[] {3}) {
            problemValueStruct.template_level = prob_sentence_num;
            int name_var_num = prob_sentence_num + 1;
            int var_num = prob_sentence_num * var_num_per_sentence;

            setSentence_category_id_ls_ls(prob_sentence_num);   // category_num(2)^prob_sentence_num
            setCategory_ls_ls(prob_sentence_num);
            set_useBoolean_ls_ls(prob_sentence_num);    // 2^prob_sentence_num
            setVar_sign_ls_ls(var_num);                 // 2^var_num = 2^(prob_sentence_num*va4_num_per_sentence(=4)) = 16 * 2^prob_sentence_num)

            for(int c_inx=0; c_inx<sentence_category_id_ls_ls.length; c_inx++){    // 2^prob_sentence_num
                problemValueStruct.sentence_expr_category_id_ls = sentence_category_id_ls_ls[c_inx].stream().mapToInt(i->i).toArray();
                problemValueStruct.expr_category_ls = arrayListToCategoryArray(category_ls_ls[c_inx]);
                for(int answer_inx = 0; answer_inx < name_var_num; answer_inx++) {   // prob_sentence_num
                    for (int condition_inx = 0; condition_inx < name_var_num; condition_inx++) {
                        if(answer_inx == condition_inx) continue;
                        for (boolean[] useYear1_ls : useBoolean_ls_ls) { // 2^prob_sentence_num
                            problemValueStruct.useYear1_ls = useYear1_ls;
                            for (boolean[] useYear2_ls : useBoolean_ls_ls) { // 2^prob_sentence_num
                                problemValueStruct.useYear2_ls = useYear2_ls;
                                for (boolean[] useMult_ls : useBoolean_ls_ls) { // 2^prob_sentence_num
                                    problemValueStruct.useMult_ls = useMult_ls;
                                    for (boolean[] useAddMinus_ls : useBoolean_ls_ls) { // 2^prob_sentence_num
                                        problemValueStruct.useAddMinus_ls = useAddMinus_ls;
                                        for (ArrayList<Integer> var_sign_ls : var_sign_ls_ls) { // 16 * 2^prob_sentence_num
                                            problemValueStruct.constant_var_sign_ls = var_sign_ls.stream().mapToInt(i -> i).toArray();
                                            if(template_id >= start_template_id) {
                                                // template 생성할 것인지 여부 결정
                                                // ex) year 사용 안 하는 경우 -> year에 따른 sign value 변화는 무시해도 좋음
                                                boolean generateTemplate = true;
                                                for (int i = 0; i < prob_sentence_num; i++) {
                                                    if ((problemValueStruct.useMult_ls[i] == false && problemValueStruct.useAddMinus_ls[i] == false && problemValueStruct.useYear1_ls[i] == false && problemValueStruct.useYear2_ls[i] == false)
                                                            || (problemValueStruct.sentence_expr_category_id_ls[i] == ProblemTokenStruct.EXPR_CATEGORY_ID_SUM_DIFFERENCE // 합차 유형에서 사용되는 변수는 var1(mult_offset에 해당하는 변수) 뿐, var1의 부호는 양수
                                                            && !(problemValueStruct.constant_var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_MULT_VAR_OFFSET] == ProblemTokenStruct.PLUS_SIGN
                                                            && problemValueStruct.constant_var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_YEAR_VAR1_OFFSET] == ProblemTokenStruct.PLUS_SIGN
                                                            && problemValueStruct.constant_var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_YEAR_VAR2_OFFSET] == ProblemTokenStruct.PLUS_SIGN
                                                            && problemValueStruct.useYear1_ls[i] == true
                                                            && problemValueStruct.useYear2_ls[i] == true
                                                            && problemValueStruct.useMult_ls[i] == true
                                                            && problemValueStruct.useAddMinus_ls[i] == true))
                                                            || (problemValueStruct.sentence_expr_category_id_ls[i] == ProblemTokenStruct.EXPR_CATEGORY_ID_YX
                                                            && problemValueStruct.constant_var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_MULT_VAR_OFFSET] == ProblemTokenStruct.MINUS_SIGN)
                                                            || (useYear1_ls[i] == false  // year1 사용하지 않는 경우, year sign에 따른 변화 무시
                                                            && problemValueStruct.constant_var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_YEAR_VAR1_OFFSET] == ProblemTokenStruct.MINUS_SIGN)
                                                            || (useYear2_ls[i] == false  // year2 사용하지 않는 경우, year sign에 따른 변화 무시
                                                            && problemValueStruct.constant_var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_YEAR_VAR2_OFFSET] == ProblemTokenStruct.MINUS_SIGN)
                                                            || (useMult_ls[i] == false && (problemValueStruct.constant_var_sign_ls[i * var_num_per_sentence] == ProblemTokenStruct.MINUS_SIGN)) // mult 사용하지 않는 경우, mult sign 에 따른 변화 무시
                                                            || (useAddMinus_ls[i] == false && (problemValueStruct.constant_var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_ADDMIN_VAR_OFFSET] == ProblemTokenStruct.MINUS_SIGN))) // addminus 사용하지 않는 경우, addminus sign 에 따른 변화 무시
                                                    {
                                                        generateTemplate = false;
                                                        break;
                                                    }
                                                }

                                                // template 생성, DB에 저장
                                                if (generateTemplate && template_id >= start_template_id) {
                                                    createTemplate.createOneTemplate(prob_sentence_num, var_num_per_sentence, condition_inx, answer_inx);
                                                    //saveInList();
                                                    TemplateDto templateDto = new TemplateDto(problemValueStruct.template_level, problemValueStruct.category,
                                                            problemValueStruct.content_template, problemValueStruct.answer_template, problemValueStruct.explanation_template,
                                                            problemValueStruct.sentence_expr_category_id_ls, problemValueStruct.expr_category_ls, problemValueStruct.constant_var_sign_ls,
                                                            problemValueStruct.useYear1_ls, problemValueStruct.useYear2_ls, problemValueStruct.useMult_ls, problemValueStruct.useAddMinus_ls);
                                                    templateDtos.add(templateDto);
                                                }
                                            }

                                            if(template_id >= end_template_id){
                                                return;
                                            }
                                            template_id++;
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
