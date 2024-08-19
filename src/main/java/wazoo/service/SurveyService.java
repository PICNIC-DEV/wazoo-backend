package wazoo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import wazoo.entity.User;
import wazoo.repository.UserRepository;
import wazoo.dto.SurveyRequestDto;
import wazoo.dto.SurveyQuestionDto;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SurveyService {
    private final UserRepository userRepository;
    private final JsonNode travelTypeTree;              // 여행타입 저장
    private final List<SurveyQuestionDto> questions;    // 질문 리스트 저장

    // request 한 Json 파일 읽기
    public SurveyService(UserRepository userRepository) throws IOException {
        this.userRepository = userRepository;
        ObjectMapper mapper = new ObjectMapper();
        travelTypeTree = mapper.readTree(new ClassPathResource("types.json").getInputStream());
        questions = mapper.readValue(new ClassPathResource("questions.json").getInputStream(), new TypeReference<>() {});
    }

    public Map<String, Object> processResponses(SurveyRequestDto surveyRequestDto){
        int userNo = surveyRequestDto.getUserNo();

        // 응답을 질문 ID와 그에 대한 답변으로 매핑
        Map<Integer, Integer> answersMap = surveyRequestDto.getAnswers().stream()
                .collect(Collectors.toMap(SurveyRequestDto.Answer::getQuestionId, SurveyRequestDto.Answer::getAnswer));

        // 각 질문 유형 그룹별로 답변을 분류하기 위한 맵
        Map<String, List<Integer>> groupedAnswers = new HashMap<>();
        for (SurveyQuestionDto question : questions) {
            int answer = answersMap.get(question.getId()); // 해당 질문의 답변
            for (String typeGroup : question.getTypeGroups()) {
                groupedAnswers.putIfAbsent(typeGroup, new ArrayList<>()); // 유형 그룹이 없으면 추가
                groupedAnswers.get(typeGroup).add(answer); // 답변을 유형 그룹에 추가
            }
        }

        String type1 = determineType(groupedAnswers.get("유동적/계획적"), new String[]{"유동적", "계획적"});
        String type2 = determineType(groupedAnswers.get("플렉스/가성비"), new String[]{"플렉스", "가성비"});
        String type3 = determineType(groupedAnswers.get("유명 관광지/현지인 체험"), new String[]{"유명 관광지", "현지인 체험"});
        String type4 = determineType(groupedAnswers.get("혼자/다같이"), new String[]{"혼자", "다같이"});

        String travelType = travelTypeTree
                .path(type1)
                .path(type2)
                .path(type3)
                .path(type4)
                .asText();

        Optional<User> userOptional = userRepository.findById(userNo);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setTravelType(travelType);
            userRepository.save(user);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("travelType", travelType);

        return result;
    }

    private String determineType(List<Integer> answers, String[] options) {

        int[] counts = new int[options.length];
        for (int answer : answers) {
            counts[answer - 1]++;
        }

        // counts 배열에서 최대값 찾기
        int maxCount = Arrays.stream(counts).max().orElse(-1);

        // 최대값의 인덱스 찾기
        int maxIndex = IntStream.range(0, counts.length)
                .filter(i -> counts[i] == maxCount)
                .findFirst()
                .orElse(0);

        return options[maxIndex]; // 최종적으로 선택된 옵션 반환
    }
}
