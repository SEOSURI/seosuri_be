package com.onejo.seosuri.service.algorithm.saveTemplate;

import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;
import com.onejo.seosuri.service.algorithm.createTemplate.CreateTemplate;
import com.onejo.seosuri.service.algorithm.problem.ProblemValueStruct;

import java.util.ArrayList;

public class SaveAllAgeTemplates extends SaveAllTemplates{


    public SaveAllAgeTemplates(int[] category_id_ls, ProblemValueStruct problemValueStruct, CreateTemplate createTemplate) {
        super(category_id_ls, problemValueStruct, createTemplate);
    }

    @Override
    public void saveAllTemplates() {
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
        int var_num_per_sentence = ProblemTokenStruct.AGE_PROB_VAR_NUM_PER_SENTENCE;

        for(int prob_sentence_num: new int[] {3, 2, 1}) {
            int name_var_num = prob_sentence_num + 1;
            int var_num = prob_sentence_num * var_num_per_sentence;
            int[] inx_ls = new int[prob_sentence_num];
            for(int i = 0; i < prob_sentence_num; i++){
                inx_ls[i] = i;
            }

            setSentence_category_id_ls_ls(prob_sentence_num);   // category_num(2)^prob_sentence_num
            setCategory_ls_ls(prob_sentence_num);
            set_useBoolean_ls_ls(prob_sentence_num);    // 2^prob_sentence_num
            setVar_sign_ls_ls(var_num);                 // 2^var_num = 2^(prob_sentence_num*va4_num_per_sentence(=4)) = 16 * 2^prob_sentence_num)

            for(int c_inx=0; c_inx<sentence_category_id_ls_ls.length; c_inx++){    // 2^prob_sentence_num
                problemValueStruct.sentence_category_id_ls = sentence_category_id_ls_ls[c_inx].stream().mapToInt(i->i).toArray();
                problemValueStruct.category_ls = arrayListToCategoryArray(category_ls_ls[c_inx]);
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
                                                if ((problemValueStruct.sentence_category_id_ls[i] == ProblemTokenStruct.CATEGORY_ID_SUM_DIFFERENCE // 합차 유형에서 사용되는 변수는 var1(mult_offset에 해당하는 변수) 뿐, var1의 부호는 양수
                                                        && !(problemValueStruct.var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_MULT_VAR_OFFSET] == ProblemTokenStruct.PLUS_SIGN
                                                        && problemValueStruct.var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_ADDMIN_VAR_OFFSET] == ProblemTokenStruct.PLUS_SIGN
                                                        && problemValueStruct.var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_YEAR_VAR1_OFFSET] == ProblemTokenStruct.PLUS_SIGN
                                                        && problemValueStruct.var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_YEAR_VAR2_OFFSET] == ProblemTokenStruct.PLUS_SIGN))
                                                        || (useYear1_ls[i] == false  // year1 사용하지 않는 경우, year sign에 따른 변화 무시
                                                        && problemValueStruct.var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_YEAR_VAR1_OFFSET] == ProblemTokenStruct.MINUS_SIGN)
                                                        || (useYear2_ls[i] == false  // year2 사용하지 않는 경우, year sign에 따른 변화 무시
                                                        && problemValueStruct.var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_YEAR_VAR2_OFFSET] == ProblemTokenStruct.MINUS_SIGN)
                                                        || (useMult_ls[i] == false && (problemValueStruct.var_sign_ls[i * var_num_per_sentence] == ProblemTokenStruct.MINUS_SIGN)) // mult 사용하지 않는 경우, mult sign 에 따른 변화 무시
                                                        || (useAddMinus_ls[i] == false && (problemValueStruct.var_sign_ls[i * var_num_per_sentence + ProblemTokenStruct.AGE_PROB_ADDMIN_VAR_OFFSET] == ProblemTokenStruct.MINUS_SIGN))) // addminus 사용하지 않는 경우, addminus sign 에 따른 변화 무시
                                                {
                                                    generateTemplate = false;
                                                    break;
                                                }
                                            }

                                            // template 생성, DB에 저장
                                            if (generateTemplate) {
                                                createTemplate.createOneTemplate(prob_sentence_num, var_num_per_sentence, condition_inx, answer_inx);
                                                /*
                                                System.out.println("" + template_id + ":: \n"
                                                        + problemValueStruct.content_template + "\n\n"
                                                        + problemValueStruct.answer_template + "\n\n"
                                                        + problemValueStruct.explanation_template + "\n\n");
                                                */

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
                }
            }
        }
    }

    @Override
    public void saveInDB() {
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

    }
}
