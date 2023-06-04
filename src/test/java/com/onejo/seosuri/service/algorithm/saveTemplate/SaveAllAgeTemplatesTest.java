package com.onejo.seosuri.service.algorithm.saveTemplate;

import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;
import com.onejo.seosuri.service.algorithm.exprCategory.ExprCategory;
import com.onejo.seosuri.service.algorithm.exprCategory.SumDiffExprCategory;
import com.onejo.seosuri.service.algorithm.exprCategory.YXAgeExprCategory;
import com.onejo.seosuri.service.algorithm.problem.CreateAgeProblem;
import com.onejo.seosuri.service.algorithm.problem.CreateProblem;
import com.onejo.seosuri.service.algorithm.problem.CreateUnknownNumProblem;
import com.onejo.seosuri.service.algorithm.problem.ProblemValueStruct;
import org.junit.jupiter.api.Test;

class SaveAllAgeTemplatesTest {

    @Test
    void createAgeProblem(){
        ProblemValueStruct problemValueStruct1 = new ProblemValueStruct();
        CreateProblem createAgeProblem = new CreateAgeProblem(problemValueStruct1);
        createAgeProblem.createProblem(3);
        problemValueStruct1.printTemplate();
        problemValueStruct1.printProblem();
    }

    @Test
    void createUnknownNumProblem(){
        ProblemValueStruct problemValueStruct1 = new ProblemValueStruct();
        CreateProblem createUnknownNumProblem = new CreateUnknownNumProblem(problemValueStruct1);
        createUnknownNumProblem.createProblem(3);
        problemValueStruct1.printTemplate();
        problemValueStruct1.printProblem();
    }

    @Test
    void saveAllUnknownNumTemplates() {
        int[] temp = {0, 0};
        ProblemValueStruct problemValueStruct = new ProblemValueStruct();
        SaveAllUnknownNumTemplates saveAllUnknownNumTemplates = new SaveAllUnknownNumTemplates(temp, problemValueStruct);
        saveAllUnknownNumTemplates.saveAllTemplates();
    }
    @Test
    void saveAllAgeTemplates() {
        int[] temp = {0, 0};
        ProblemValueStruct problemValueStruct = new ProblemValueStruct();
        SaveAllAgeTemplates saveAllAgeTemplates = new SaveAllAgeTemplates(temp, problemValueStruct);
        saveAllAgeTemplates.saveAllTemplates();
    }

    ProblemTokenStruct problemValueStruct = new ProblemTokenStruct();
    @Test
    void create_age_sentence_yx() {
        YXAgeExprCategory yxAgeCategory = new YXAgeExprCategory();
        int ls_index = 1;
        int var_num_per_sentence = 1;

        String name_category_token = problemValueStruct.age_category_token; // "의 나이"
        String name_unit_token = problemValueStruct.age_unit_token;
        String var34_unit_token = problemValueStruct.year_unit_token;
        String after_str_token = problemValueStruct.time_after_token;
        String before_str_token = problemValueStruct.time_before_token;

        ExprCategory exprCategory = new YXAgeExprCategory();

        boolean[] tf_set = new boolean[] {true, false};
        int case_id = 0;
        // cond_inx
        for(int cond_inx: new int[] {0, 1})
            for(boolean useYear1: tf_set)
                for(boolean useYear2: tf_set)
                    for(boolean useMult: tf_set)
                        for(boolean useAddMinus: tf_set)/*
                            for(int year1_sign: new int[] {0, 1})
                                for(int year2_sign: new int[] {0, 1})
                                    for(int var_sign: new int[] {0, 1})*/ {
                            case_id++;
                            int year1_sign=0, year2_sign=0, var_sign=0;
                            System.out.println("\n\nCASE" + case_id + "\t@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                            System.out.println("cond="+cond_inx+" y1="+useYear1+" y2="+useYear2+" m="+useMult+" am="+useAddMinus+" yrs="+year1_sign+year2_sign+" vars="+var_sign);

                            String content = exprCategory.createSentenceContent(true, 1, 2,
                                    1, var_num_per_sentence, useYear1, useYear2, useMult, useAddMinus, var_sign, year1_sign, year2_sign, name_category_token, name_unit_token, var34_unit_token, after_str_token, before_str_token);
                            String explanation = exprCategory.createSentenceExplanation(content, 1, 2, 1, var_num_per_sentence, cond_inx,
                                    useYear1, useYear2, useMult, useAddMinus,
                                    var_sign, year1_sign, year2_sign,
                                    name_category_token, name_unit_token, var34_unit_token, after_str_token, before_str_token);

                            System.out.println(content);
                            System.out.println(explanation);
                        }
    }
    @Test
    void create_age_sentence_sum_difference() {
        ExprCategory exprCategory = new SumDiffExprCategory();
        int ls_index = 0;
        int var_num_per_sentence = 1;
        boolean useYear1 = false, useYear2 = false, useMult = false, useAddMinus = false;
        int var_sign = 0, year1_sign=0, year2_sign=0;
        String name_unit_token="", name_category_token="", var34_unit_token="",after_str_token="", before_str_token="";
        for(int cond_inx: new int[] {0, 1})
            for(int sign: new int[] {0, 1}) {
                System.out.println("content-------------------------");
                String content = exprCategory.createSentenceContent(true, 1, 2,
                        1, var_num_per_sentence, useYear1, useYear2, useMult, useAddMinus, var_sign, year1_sign, year2_sign, name_category_token, name_unit_token, var34_unit_token, after_str_token, before_str_token);
                String explanation = exprCategory.createSentenceExplanation(content, 1, 2, 1, var_num_per_sentence, cond_inx,
                        useYear1, useYear2, useMult, useAddMinus,
                        var_sign, year1_sign, year2_sign,
                        name_category_token, name_unit_token, var34_unit_token, after_str_token, before_str_token);

                System.out.println(content);
                System.out.println(explanation);
            }
    }


}