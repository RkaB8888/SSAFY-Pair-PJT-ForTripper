package com.travel.demo.shareBoard.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.travel.demo.shareBoard.dto.ShareAddRequestDTO;
import com.travel.demo.shareBoard.dto.ShareBoardResponseDTO;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ShareBoardService {
    int addSharePost(long plan_id, String token, ShareAddRequestDTO requestDTO) throws IOException, ParseException;
    List<ShareBoardResponseDTO> findShareBoardList() throws IOException;
}
