package com.travel.demo.shareBoard.dto;

import lombok.Data;
import org.springframework.core.io.Resource;

import java.sql.Timestamp;

@Data
public class ShareBoardResponseDTO {
    private long post_id;
    private long user_id;
    private long plan_id;
    private String title;
    private String content;
    private Timestamp created_time;
    private String imageUrl;
}
