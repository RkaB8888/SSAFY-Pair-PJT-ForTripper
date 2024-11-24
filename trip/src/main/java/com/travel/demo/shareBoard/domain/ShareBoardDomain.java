package com.travel.demo.shareBoard.domain;

import lombok.Data;

@Data
public class ShareBoardDomain {
    private long plan_id;
    private long user_id;
    private String title;
    private String content;
    private String image_name;
}
