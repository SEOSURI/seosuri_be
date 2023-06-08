package com.onejo.seosuri.controller.dto.classification;

import com.onejo.seosuri.domain.classification.CategoryTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title="유형 분류 결과")
public class CategoryResDto {
    @Schema(description = "문제 유형", example = "나이_구하기")
    CategoryTitle categoryTitle;
}
