package com.travel.demo.shareBoard.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class ShareAddRequestDTO {
    private String title;
    private String content;
    private String dailySchedules;
    private int totalDate;
    private MultipartFile image;
}