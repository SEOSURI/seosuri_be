package com.onejo.seosuri.service.algorithm.createTemplate;

import com.onejo.seosuri.service.algorithm.category.Category;
import com.onejo.seosuri.service.algorithm.problem.ProblemValueStruct;

public abstract class CreateTemplate {

    ProblemValueStruct problemValueStruct = new ProblemValueStruct();

    public CreateTemplate(ProblemValueStruct problemValueStruct) {
        this.problemValueStruct = problemValueStruct;
    }

    abstract public void createOneTemplate(int prob_sentence_num, int var_num_per_sentence,
                                    int answer_inx, int condition_inx);

    abstract public String[] createSentence(Category category, boolean isCorrectNumSentence, int name_var_index1, int name_var_index2, int category_id, int index, int var_num_per_sentence, int cond_inx,
                                            boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus,
                                            int var_sign, int year1_sign, int year2_sign);

    //////////////////////////////////////////////////////////////////////////////////////////
    // 문장 연결
    public String concateContent(String[][] sentence_ls, String condition, String question){
        String content = "";
        for(int i = 0; i < sentence_ls.length; i++){ // 상황 문장 content - 순서 섞으면 난이도 올라감
            content += sentence_ls[i][0] + "\n";
        }
        content += condition + "\n" + question;
        return content;
    }
    public String concateExplanation(String[][] sentence_ls, int condition_inx){
        // explanation
        // condition_inx = 0 : 0->1->...
        // else: condition_inx+1 -> condition_inx-1 -> ... -> 끝, condition_inx-1 -> condition_inx-2 -> ... -> 0 순서로 연결
        String explanation = "";
        int start_index = (condition_inx + sentence_ls.length - 1) % sentence_ls.length;
        if(condition_inx == 0)  start_index = 0;
        for(int i = start_index; i < sentence_ls.length; i++){    // condition_inx  ~  끝
            explanation += sentence_ls[i][1] + "\n\n";   // 상황 문장 explanation
        }
        for(int i = start_index - 1; i >= 0; i--){ // condition_inx-1 ~ 0
            explanation += sentence_ls[i][1] + "\n\n";   // 상황 문장 exlanation
        }
        return explanation;
    }

}
