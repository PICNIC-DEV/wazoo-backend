package wazoo.dto;

import lombok.*;
import java.util.List;

@Data
public class SurveyQuestionDto {
    private int id;
    private String text;
    private List<String> typeGroups;
}
