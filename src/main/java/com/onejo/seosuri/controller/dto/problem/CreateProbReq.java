package com.onejo.seosuri.controller.dto.problem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title="문제 생성 정보")
public class CreateProbReq {
    @Schema(description = "카테고리", example = "나이_구하기\n이은_색테이프\n어떤수\n도형_혼합계산_응용")
    private String categoryTitle;

    @Schema(description = "난이도", example = "상\n중\n하")
    private String level;
}
