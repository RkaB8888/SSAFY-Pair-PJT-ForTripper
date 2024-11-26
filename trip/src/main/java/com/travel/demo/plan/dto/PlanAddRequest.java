package com.travel.demo.plan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(title="Plan : Plan 생성 DTO")
public class PlanAddRequest {
    @NotBlank(message = "여행 계획에 대한 이름을 입력해주세요.")
    @Schema(description = "여행 계획 이름", example = "대만 가족여행")
    private String plan_title;

    @NotBlank(message = "여행 시작 날짜를 입력해주세요.")
    @Schema(description = "여행 시작 날짜", example = "2025-05-07")
    private LocalDate start_date;

    @NotBlank(message = "여행 마지막 날짜를 입력해주세요.")
    @Schema(description = "여행 마지막 날짜", example = "2025-05-10")
    private LocalDate end_date;

    @NotBlank(message = "해당 여행에 대한 부가적인 설명을 입력해주세요")
    @Schema(description = "여행 설명", example = "우리 가족 4년만의 가족여행!")
    private String description;
}
