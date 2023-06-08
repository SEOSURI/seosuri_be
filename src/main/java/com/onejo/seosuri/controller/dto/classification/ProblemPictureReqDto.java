package com.onejo.seosuri.controller.dto.classification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title="문제 분류 요청할 문제 사진 정보")
public class ProblemPictureReqDto {
    @Schema(description = "사진 파일 경로", example = "")
    String filePath;
}
