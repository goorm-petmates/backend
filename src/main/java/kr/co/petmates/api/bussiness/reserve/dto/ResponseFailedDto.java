package kr.co.petmates.api.bussiness.reserve.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

/**
 * 실패에 대한 응답 처리 객체
 */
@Getter
@Builder
public class ResponseFailedDto {
    private final String result = "failed";
    private Data data;

    // ResponseFailedDto의 빌더를 위한 정적 메서드
    public static ResponseFailedDtoBuilder builder(String reason) {
        Data data = Data.builder().reason(reason).build();
        return new ResponseFailedDtoBuilder().data(data);
    }

    @Getter
    @Builder
    public static class Data {
        private String reason;
        @Builder.Default
        private Map<String, Object> additionalData = new HashMap<>();

        // Data 객체에 추가 데이터를 삽입하는 메서드
        public Data addData(String key, Object value) {
            this.additionalData.put(key, value);
            return this;
        }
    }
}

