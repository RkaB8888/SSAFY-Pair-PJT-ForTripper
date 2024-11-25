package com.travel.demo.shareBoard.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ShareBoardEntity {
    private long post_id;
    private long user_id;
    private long plan_id;
    private String title;
    private String content;
    private Timestamp created_time;
    private Timestamp modified_time;
    private String image_name;
}
