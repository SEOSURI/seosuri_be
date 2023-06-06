package com.onejo.seosuri.controller.dto.template;

import com.onejo.seosuri.domain.classification.Category;
import com.onejo.seosuri.domain.problem.Problem;
import com.onejo.seosuri.domain.problem.ProblemTemplate;
import com.onejo.seosuri.service.algorithm.exprCategory.ExprCategory;
import com.onejo.seosuri.service.algorithm.exprCategory.SumDiffExprCategory;
import com.onejo.seosuri.service.algorithm.exprCategory.YXAgeExprCategory;
import com.onejo.seosuri.service.algorithm.exprCategory.YXUnkownNumExprCategory;
import com.onejo.seosuri.service.algorithm.ProblemValueStruct;
import lombok.Getter;

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
        this.level = String.valueOf(this.int_level);

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

    // 실제 DB : ProblemTemplate
    // TemplateDto - DB에서 값 가져오거나 저장할 때만 사용
    // 알고리즘 : ProblemValueStruct

    // TemplateDto안의 값들을 ProblemValueStruct에 저장
    public void setProblemValueStruct(ProblemValueStruct problemValueStruct){
        problemValueStruct.template_level = this.int_level;
        problemValueStruct.category = this.category;
        problemValueStruct.content_template = content;
        problemValueStruct.answer_template = answer;
        problemValueStruct.explanation_template = explanation;
        problemValueStruct.sentence_expr_category_id_ls = this.sentence_expr_category_id_ls;
        problemValueStruct.expr_category_ls = this.expr_category_ls;
        problemValueStruct.constant_var_sign_ls = this.var_sign_ls;
        problemValueStruct.useYear1_ls = this.useYear1_ls;
        problemValueStruct.useYear2_ls = this.useYear2_ls;
        problemValueStruct.useMult_ls = this.useMult_ls;
        problemValueStruct.useAddMinus_ls = this.useAddMinus_ls;
    }

    public TemplateDto (ProblemTemplate problemTemplate){
        this.int_level = Integer.parseInt(problemTemplate.getLevel());
        this.level = problemTemplate.getLevel();
        this.category = problemTemplate.getCategory();
        this.content = problemTemplate.getContent();
        this.answer = problemTemplate.getAnswer();
        this.explanation = problemTemplate.getExplanation();

        this.sentence_expr_category_id_ls = stringToIntArray(problemTemplate.getSentenceCategoryList());
        this.expr_category_ls = stringToExprCategoryArray(problemTemplate.getExprCategoryList());
        this.var_sign_ls = stringToIntArray(problemTemplate.getVarSignList());
        this.useYear1_ls = stringToBooleanArray(problemTemplate.getUseYear1List());
        this.useYear2_ls = stringToBooleanArray(problemTemplate.getUseYear2List());
        this.useMult_ls = stringToBooleanArray(problemTemplate.getUseMultList());
        this.useAddMinus_ls = stringToBooleanArray(problemTemplate.getUseAddMinusList());

        this.sentenceCategoryList = problemTemplate.getSentenceCategoryList();
        this.exprCategoryList = problemTemplate.getExprCategoryList();
        this.varSignList = problemTemplate.getVarSignList();
        this.useYear1List = problemTemplate.getUseYear1List();
        this.useYear2List = problemTemplate.getUseYear2List();
        this.useMultList = problemTemplate.getUseMultList();
        this.useAddMinusList = problemTemplate.getUseAddMinusList();
    }

    public void printTemplateDto(){
        System.out.println("\t\t\tlevel:: \t"+ int_level);
        System.out.println("\t\t\tcategory:: \t"+category);
        System.out.println("\t\t\tcontent:: \t"+content);
        System.out.println("\t\t\tanswer:: \t"+answer);
        System.out.println("\t\t\texplanation:: \t"+explanation);

        System.out.println("\t\t\tsentenceCategoryList:: \t"+sentenceCategoryList);
        System.out.println("\t\t\texprCategoryList:: \t"+exprCategoryList);
        System.out.println("\t\t\tvarSignList:: \t"+varSignList);
        System.out.println("\t\t\tuseYear1List:: \t"+useYear1List);
        System.out.println("\t\t\tuseYear2List:: \t"+useYear2List);
        System.out.println("\t\t\tuseMultList:: \t"+useMultList);
        System.out.println("\t\t\tuseAddMinusList:: \t"+useAddMinusList);
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
                .problems(new ArrayList<Problem>())
                .build();
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
