package com.travel.demo.shareBoard.model.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.demo.database.TempFileStorageService;
import com.travel.demo.shareBoard.domain.ShareBoardDomain;
import com.travel.demo.shareBoard.domain.SharePlanDomain;
import com.travel.demo.shareBoard.dto.ShareAddRequestDTO;
import com.travel.demo.shareBoard.domain.SharePlaceDomain;
import com.travel.demo.users.model.mapper.AuthMapper;
import com.travel.demo.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShareBoardServiceImpl implements ShareBoardService{

    private final TempFileStorageService tempFileStorageService;
    private final JWTUtil jwtUtil;
    private final AuthMapper authMapper;

    @Override
    public int addSharePost(String token, ShareAddRequestDTO requestDTO) throws IOException {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String email = jwtUtil.getIdFromToken(token); //token에서 ID(email) 파싱해서 가져옴
            long user_id = authMapper.findByEmail(email).getUser_id();

            //SharePlan 세팅 후 DB 저장
            SharePlanDomain sharePlanDomain = new SharePlanDomain();
            sharePlanDomain.setTotal_date(requestDTO.getTotalDate());
            sharePlanDomain.setUser_id(user_id);

            //ShareBoard 세팅 후 DB 저장
            ShareBoardDomain shareBoardDomain = new ShareBoardDomain();
            shareBoardDomain.setUser_id(user_id);
            shareBoardDomain.setTitle(requestDTO.getTitle());
            shareBoardDomain.setContent(requestDTO.getContent());
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, List<SharePlaceDomain>> dailySchedules = objectMapper.readValue(requestDTO.getDailySchedules(),
                    new TypeReference<Map<String, List<SharePlaceDomain>>>() {});

            for (String s : dailySchedules.keySet()) {

            }

            if (requestDTO.getImage() != null && !requestDTO.getImage().isEmpty()) {
                String filename = tempFileStorageService.saveFile(requestDTO.getImage(), String.valueOf(user_id));
                System.out.println("File saved: " + requestDTO.getImage().getOriginalFilename());
            }


        } else {
            throw new IllegalArgumentException("Invalid Authorization header!");
        }
        return 0;
    }
}
