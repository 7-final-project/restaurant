package com.qring.restaurant.presentation.v1.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PutCategoryDTOV1 {

    @Valid
    @NotNull(message = "카테고리 정보를 입력해주세요.")
    private PostCategoryReqDTOV1.Category category;

    @Getter
    public static class Category {

        @NotBlank(message = "카테고리 이름을 입력해주세요.")
        private String name;

    }
}
