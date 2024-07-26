package wazoo.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import wazoo.dto.WhisperTranscriptionRequest;
import wazoo.dto.WhisperTranscriptionResponse;

@FeignClient(
        name = "openai-service",
        url = "${openai-service.urls.base-url}",
        configuration = OpenAIClientConfig.class
)
public interface OpenAIClient {

    @PostMapping(value = "${openai-service.urls.create-transcription-url}", headers = {"Content-Type=multipart/form-data"})
    WhisperTranscriptionResponse createTranscription(@ModelAttribute WhisperTranscriptionRequest whisperTranscriptionRequest);
}