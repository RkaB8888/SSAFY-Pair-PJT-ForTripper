package com.travel.demo.shareBoard.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.travel.demo.shareBoard.dto.ShareAddRequestDTO;

import java.io.IOException;

public interface ShareBoardService {
    int addSharePost(String token, ShareAddRequestDTO requestDTO) throws IOException;
}
