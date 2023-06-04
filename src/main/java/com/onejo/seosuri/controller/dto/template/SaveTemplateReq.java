package com.onejo.seosuri.controller.dto.template;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title="저장할 템플릿 정보")
public class SaveTemplateReq {
    @Schema(description = "저장 시작할 템플릿의 index", example = "0")
    private int start_template_id;

    @Schema(description = "마지막으로 저장할 템플릿의 index", example = "100")
    private int end_template_id;
}
