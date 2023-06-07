package com.onejo.seosuri.service.algorithm;

import com.onejo.seosuri.domain.classification.Category;
import com.onejo.seosuri.domain.problem.ProblemTemplate;
import com.onejo.seosuri.domain.testpaper.TestPaper;
import com.onejo.seosuri.domain.word.Word;
import com.onejo.seosuri.service.algorithm.exprCategory.ExprCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.onejo.seosuri.service.algorithm.ProblemTokenStruct.AGE_PROB_NAME_VAR_NUM;

@Getter
@Setter
public class ProblemValueStruct {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // template
    public List<Word> wordList = new ArrayList<>();
    public TestPaper testPaper;
    public ProblemTemplate problemTemplate;
    public Long probNum;
    public int template_level;
    public Category category;
    public String content_template = "";
    public String explanation_template = "";
    public String answer_template = "";

    // boolean 변수값
    // 10(year1 = var3)년 후 어머니(name1)의 나이(namevar1)는      5(year2 = var4)년 전 동생(name2)의 나이(variant_var2)에     3(var1)을 곱한 후     7(var2)을 뺀 값과 같습니다.
    // num_constant_var_per_sentence : 문장 하나에 등장하는 최대 변수 갯수 : yx식유형 4개 = 4
    // (y +- constant_var3) = (x +- constant_var4) * var1 +- constant_var2         : y = namevar1,  x = namevar2
    //
    // {어머니나이} +- year1
    public boolean[] useYear1_ls;   // 10년 후    // var3
    public boolean[] useYear2_ls;   // 5년 전 // var4
    public boolean[] useMult_ls; // 3을 곱해   // var1
    public boolean[] useAddMinus_ls;    // 7을 빼 // var2

    // 상황 문장 1번에 var1을 -> index 4(상황문장하나에등장하는constant_var수) * 1(첫번째상황문장) + 0(var1 OFFSET)
    // 4 : num_constnat_var_per_sentence - 인자로 주어짐
    /*
    public static final int AGE_PROB_MULT_VAR_OFFSET = 0;
    public static final int AGE_PROB_ADDMIN_VAR_OFFSET = 1;
    public static final int AGE_PROB_YEAR_VAR1_OFFSET = 2;
    public static final int AGE_PROB_YEAR_VAR2_OFFSET = 3;
     */


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 실제 문제
    public String real_content = "";
    public String real_explanation = "";
    public String real_answer = "";



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // template 생성, 실제 문제 생성 시 이용

    // 상황문장 유형id
    public int[] sentence_expr_category_id_ls;
    public ExprCategory[] expr_category_ls;

    // sign
    public int[] constant_var_sign_ls;  // input


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 실제 문제 생성 시에만 이용

    // min, max 범위
    public int[] constant_var_min_value_ls; // input - 상수
    public int[] constant_var_max_value_ls; // input - 상수
    public int[] variant_var_min_value_ls;    // input - 이름
    public int[] variant_var_max_value_ls;    // input - 이름

    // 이름
    public String[] variant_var_string_ls;

    // 숫자값
    public int[] variant_var_ls;   // 나이 - 51살에서 "51"   - 10년 후에서 "10"은 나이 아님
    public int[] constant_var_ls;   // 상수 - 5를 곱한다에서 "5" 같은 그런 친구

    public void printTemplate(){
        System.out.println("CONTENT ------------------------------------");
        System.out.println(content_template);
        System.out.println("EXPLANATION ------------------------------------");
        System.out.println(explanation_template);
        System.out.println("ANSWER ------------------------------------");
        System.out.println(answer_template);
        System.out.println("SENTENCE_CATEGORY_ID ------------------------------");
        System.out.println(Arrays.toString(sentence_expr_category_id_ls));
        System.out.println("VAR_SIGN ------------------------------------");
        System.out.println(Arrays.toString(constant_var_sign_ls));
        System.out.println("\n\n");
    }
    public void printProblem(){
        System.out.println("CONTENT ------------------------------------");
        System.out.println(real_content);
        System.out.println("EXPLANATION ------------------------------------");
        System.out.println(real_explanation);
        System.out.println("ANSWER ------------------------------------");
        System.out.println(real_answer);
        System.out.println("SENTENCE_CATEGORY_ID ------------------------------");
        System.out.println(Arrays.toString(sentence_expr_category_id_ls));
        System.out.println("VAR_SIGN ------------------------------------");
        System.out.println(Arrays.toString(constant_var_sign_ls));
        System.out.println("\n\n");
    }

    public void setWordListDirect(List<Word> wordList_){
        for(int i=0; i<AGE_PROB_NAME_VAR_NUM*template_level; i++){
            this.wordList.add(wordList_.get(i));
        }
    }

    public void setSentence_expr_category_id_lsDirect(int[] sentence_expr_category_id_ls){
        int[] tmp = new int[sentence_expr_category_id_ls.length];
        for(int i=0; i<sentence_expr_category_id_ls.length; i++){
            tmp[i] = sentence_expr_category_id_ls[i];
        }
        this.sentence_expr_category_id_ls = tmp;
    }

    public void setExpr_category_lsDirect(ExprCategory[] expr_category_ls){
        ExprCategory[] tmp = new ExprCategory[expr_category_ls.length];
        for(int i=0; i<expr_category_ls.length; i++){
            tmp[i] = expr_category_ls[i];
            System.out.println(tmp[i]);  // test>>>>>>>>>>>>>>>>>>>>>>>>>>>
        }
        this.expr_category_ls = tmp;
    }

    public void setConstant_var_sign_lsDirect(int[] var_sign_ls){
        int[] tmp = new int[var_sign_ls.length];
        for(int i=0; i<var_sign_ls.length; i++){
            tmp[i] = var_sign_ls[i];
        }
        this.constant_var_sign_ls = tmp;
    }

    public void setUseYear1_lsDirect(boolean[] useYear1_ls){
        boolean[] tmp = new boolean[useYear1_ls.length];
        for(int i=0; i<useYear1_ls.length; i++){
            tmp[i] = useYear1_ls[i];
        }
        this.useYear1_ls = tmp;
    }

    public void setUseYear2_lsDirect(boolean[] useYear2_ls){
        boolean[] tmp = new boolean[useYear2_ls.length];
        for(int i=0; i<useYear2_ls.length; i++){
            tmp[i] = useYear2_ls[i];
        }
        this.useYear2_ls = tmp;
    }

    public void setUseMult_lsDirect(boolean[] useMult_ls){
        boolean[] tmp = new boolean[useMult_ls.length];
        for(int i=0; i<useMult_ls.length; i++){
            tmp[i] = useMult_ls[i];
        }
        this.useMult_ls = tmp;
    }

    public void setUseAddMinus_lsDirect(boolean[] useAddMinus_ls){
        boolean[] tmp  = new boolean[useAddMinus_ls.length];
        for(int i=0; i<useAddMinus_ls.length; i++){
            tmp[i] = useAddMinus_ls[i];
        }
        this.useAddMinus_ls = tmp;
    }



}
