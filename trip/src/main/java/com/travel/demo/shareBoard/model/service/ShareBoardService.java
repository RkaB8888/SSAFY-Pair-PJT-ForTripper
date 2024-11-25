package com.travel.demo.shareBoard.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.travel.demo.plan.dto.PlanAddRequest;
import com.travel.demo.shareBoard.dto.ShareAddRequestDTO;
import com.travel.demo.shareBoard.dto.ShareBoardResponseDTO;
import com.travel.demo.shareBoard.dto.SharePlaceListDTO;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

public interface ShareBoardService {
    int addSharePost(long plan_id, String token, ShareAddRequestDTO requestDTO) throws IOException, ParseException;
    List<ShareBoardResponseDTO> findShareBoardList() throws IOException;
    SharePlaceListDTO findVisitPlacesByPlanId(Long planId);
    void copyAddPlan(String token, Long planId, PlanAddRequest planAddRequest) throws ParseException;

}
