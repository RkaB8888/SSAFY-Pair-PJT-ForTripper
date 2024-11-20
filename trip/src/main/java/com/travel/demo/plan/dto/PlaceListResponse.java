package com.travel.demo.plan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PlaceListResponse {
    @NotBlank(message = "google place의 id가 입력됩니다.")
    @Schema(description = "Google Place API의 Place ID (특정 장소에 대한 ID)", example = "ChIJQdQyeiZu5kcRUfQHfB-OCLA")
    private String visit_google_id;

    @NotBlank(message = "화면에 표시될 장소 이름이 입력됩니다.")
    @Schema(description = "특정 장소에 대한 이름", example = "국립중앙박물관")
    private String display_name;

    @NotBlank(message = "해당 장소의 주소가 입력됩니다.")
    @Schema(description = "특정 장소에 대한 주소", example = "서울 용산구 서빙고로 137 국립중앙박물관")
    private String address;

    @NotBlank(message = "해당 장소의 전화번호가 입력됩니다.")
    @Schema(description = "특정 장소에 대한 전화번호", example = "02-2077-9000")
    private String phone_number;

    @NotBlank(message = "해당 장소의 위도가 입력됩니다.")
    @Schema(description = "특정 장소에 대한 위도", example = "37.5238521834591")
    private BigDecimal latitude;

    @NotBlank(message = "해당 장소의 경도가 입력됩니다.")
    @Schema(description = "특정 장소에 대한 경도", example = "126.98046326637268")
    private BigDecimal longitude;

    @NotBlank(message = "해당 장소를 방문하는 날짜를 입력합니다.")
    @Schema(description = "해당 장소 방문 날짜", example = "2017-02-16")
    private LocalDate visit_date;

    @NotBlank(message = "해당 장소 방문일에 방문하는 순번을 입력합니다.")
    @Schema(description = "해당 장소 방문 순번", example = "3")
    private int visit_order;
}
