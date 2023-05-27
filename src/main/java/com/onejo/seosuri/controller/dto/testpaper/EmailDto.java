package com.onejo.seosuri.controller.dto.testpaper;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title="이메일 발송")
public class EmailDto {

    @Schema(description = "보낼 이메일 주소", example = "example01@gmail.com")
    private String email;

    @Schema(description = "보낼 시험지 id", example = "1")
    private Long testPaperId;
}
