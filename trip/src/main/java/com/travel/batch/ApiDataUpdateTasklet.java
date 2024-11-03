package com.travel.batch;

import com.travel.place.mapper.PlaceMapper;
import com.travel.place.model.PlaceDto;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ApiDataUpdateTasklet implements Tasklet {

    private final PlaceMapper placeMapper;

    public ApiDataUpdateTasklet(PlaceMapper placeMapper) {
        this.placeMapper = placeMapper;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // DB에서 모든 PlaceDto 가져오기
        List<PlaceDto> dbPlaces = (List<PlaceDto>) placeMapper.selectAll();

        // API로부터 데이터 가져오기
        RestTemplate restTemplate = new RestTemplate();
        PlaceDto[] apiPlaces = restTemplate.getForObject("API_URL", PlaceDto[].class); // 여기에 API URL 입력

        // DB 데이터와 API 데이터 비교 후 업데이트
        for (PlaceDto apiPlace : apiPlaces) {
            for (PlaceDto dbPlace : dbPlaces) {
                if (apiPlace.getPlace_id() == dbPlace.getPlace_id() &&
                        !apiPlace.getModified_time().equals(dbPlace.getModified_time())) {
                    // modified_time이 다르면 업데이트
                    placeMapper.updateContent(apiPlace);
                }
            }
        }

        return RepeatStatus.FINISHED;
    }
}
