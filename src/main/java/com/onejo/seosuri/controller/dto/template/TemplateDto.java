package com.onejo.seosuri.controller.dto.template;

import com.google.type.Expr;
import com.onejo.seosuri.domain.classification.Category;
import com.onejo.seosuri.domain.problem.ProblemTemplate;
import com.onejo.seosuri.service.algorithm.exprCategory.ExprCategory;
import com.onejo.seosuri.service.algorithm.exprCategory.SumDiffExprCategory;
import com.onejo.seosuri.service.algorithm.exprCategory.YXAgeExprCategory;
import com.onejo.seosuri.service.algorithm.exprCategory.YXUnkownNumExprCategory;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


@Getter
public class TemplateDto {

    private static final String DELIMETER = " ";

    //@Column(name="prob_temp_level")
    private String level;
    private int int_level;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name="category_id")
    private Category category;


    //@Column(name="prob_temp_content")
    private String content;

    //@Column(name="prob_temp_answer")
    private String answer;

    //@Column(name="prob_temp_explanation")
    private String explanation;

    //@Column(name="prob_sentence_category_list")
    private String sentenceCategoryList;
    private String exprCategoryList;
    private String varSignList;
    private String useYear1List;
    private String useYear2List;
    private String useMultList;
    private String useAddMinusList;

    private int[] sentence_expr_category_id_ls;
    private ExprCategory[] expr_category_ls;
    private int[] var_sign_ls;

    private boolean[] useYear1_ls;
    private boolean[] useYear2_ls;
    private boolean[] useMult_ls;
    private boolean[] useAddMinus_ls;

    public TemplateDto(int level, Category category, String content, String answer, String explanation,
                       int[] sentence_expr_category_id_ls, ExprCategory[] expr_category_ls, int[] var_sign_ls, boolean[] useYear1_ls, boolean[] useYear2_ls, boolean[] useMult_ls, boolean[] useAddMinus_ls) {
        this.int_level = level;
        this.category = category;
        this.content = content;
        this.answer = answer;
        this.explanation = explanation;

        this.sentence_expr_category_id_ls = sentence_expr_category_id_ls;
        this.expr_category_ls = expr_category_ls;
        this.var_sign_ls = var_sign_ls;
        this.useYear1_ls = useYear1_ls;
        this.useYear2_ls = useYear2_ls;
        this.useMult_ls = useMult_ls;
        this.useAddMinus_ls = useAddMinus_ls;

        this.sentenceCategoryList = intArrayToString(this.sentence_expr_category_id_ls);
        this.exprCategoryList = exprCategoryArrayToString(this.expr_category_ls);
        this.varSignList = intArrayToString(this.var_sign_ls);
        this.useYear1List = booleanArrayToString(this.useYear1_ls);
        this.useYear2List = booleanArrayToString(this.useYear2_ls);
        this.useMultList = booleanArrayToString(this.useMult_ls);
        this.useAddMinusList = booleanArrayToString(this.useAddMinus_ls);
    }

    public String exprCategoryArrayToString(ExprCategory[] target){
        String output = "";
        for(int i = 0; i < target.length; i++){
            output += DELIMETER + target[i].toString();
        }
        return output;
    }

    public ExprCategory[] stringToExprCategoryArray(String target){
        String[] res_str = target.split(DELIMETER);
        ExprCategory[] res = new ExprCategory[res_str.length];
        for(int i = 0; i < res.length; i++){
            String id = res_str[i];
            res[i] = null;
            ExprCategory[] possible_values = new ExprCategory[] {
                    new SumDiffExprCategory(), new YXAgeExprCategory(), new YXUnkownNumExprCategory()};
            for(int j = 0; j < possible_values.length; j++){
                if(id == possible_values[j].toString()){
                    res[i] = possible_values[i];
                }
            }
        }
        return res;
    }


    public String intArrayToString(int[] target){
        String output = "";
        for(int item: target){
            output += DELIMETER + String.valueOf(item);
        }
        return output;
    }

    public int[] stringToIntArray(String target){
        String[] res_str = target.split(DELIMETER);
        int[] res = new int[res_str.length];
        for(int i = 0; i < res.length; i++){
            res[i] = Integer.valueOf(res_str[i]);
        }
        return res;
    }

    public String booleanArrayToString(boolean[] target){
        String output = "";
        for(boolean item: target){
            output += DELIMETER + String.valueOf(item);
        }
        return output;
    }

    public boolean[] stringToBooleanArray(String target){
        String[] res_str = target.split(DELIMETER);
        boolean[] res = new boolean[res_str.length];
        for(int i = 0; i < res.length; i++){
            res[i] = Boolean.valueOf(res_str[i]);
        }
        return res;
    }

    public ProblemTemplate toEntity(){
        return ProblemTemplate.builder()
                .level(level)
                .category(category)
                .content(content)
                .answer(answer)
                .explanation(explanation)
                .sentenceCategoryList(sentenceCategoryList)
                .exprCategoryList(exprCategoryList)
                .varSignList(varSignList)
                .useYear1List(useYear1List)
                .useYear2List(useYear2List)
                .useMultList(useMultList)
                .useAddMinusList(useAddMinusList)
                .build();
    }

}
