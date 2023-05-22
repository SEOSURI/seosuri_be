package com.onejo.seosuri.controller.dto.problem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(title = "10문제")
public class ProbRes {
    @Schema(description = "시험지 id", example = "")
    private Long testPaperId;
    @Schema(description = "문제 번호", example = "")
    private Long num;
    @Schema(description = "시험지 난이도", example = "")
    private String level;
    @Schema(description = "문제", example = "어떤 수에다 5를 뺐더니 15가 되었을 때 어떤 수에 19를 더할 때 그 결과를 구하시오")
    private String content;
    @Schema(description = "해설", example = "")
    private String explanation;
    @Schema(description = "답", example = "")
    private String answer;


}
