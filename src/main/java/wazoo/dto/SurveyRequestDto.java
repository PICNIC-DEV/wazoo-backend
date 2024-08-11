package wazoo.dto;

import lombok.*;

import java.util.List;

@Data
public class SurveyRequestDto {
    private int userNo;
    private List<Answer> answers;

    @Data
    public static class Answer {
        private int questionId;
        private int answer;
    }
}