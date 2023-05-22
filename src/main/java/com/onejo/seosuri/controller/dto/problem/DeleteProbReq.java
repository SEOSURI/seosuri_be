package com.onejo.seosuri.controller.dto.problem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title="삭제할 문항 정보")
public class DeleteProbReq {
    @Schema(description = "시험지 id", example = "1")
    private Long testPaperId;

    @Schema(description = "문제 번호", example = "1")
    private Long probNum;
}
