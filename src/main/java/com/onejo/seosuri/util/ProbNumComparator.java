package com.onejo.seosuri.util;

import com.onejo.seosuri.controller.dto.problem.CreateProbRes;

import java.util.Comparator;

public class ProbNumComparator implements Comparator<CreateProbRes> {
    @Override
    public int compare(CreateProbRes prob1, CreateProbRes prob2) {
        if(prob1.getNum() > prob2.getNum()) {
            return 1;
        }
        else if(prob1.getNum() < prob2.getNum()){
            return -1;
        }
        else {
            return 0;
        }
    }

}

