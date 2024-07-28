package wazoo.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wazoo.config.OpenAIClient;
import wazoo.config.OpenAIClientConfig;
import wazoo.dto.TranscriptionRequest;
import wazoo.dto.WhisperTranscriptionRequest;
import wazoo.dto.WhisperTranscriptionResponse;

@RequiredArgsConstructor
@Service
public class OpenAIClientService {

    private final OpenAIClient openAIClient;
    private final OpenAIClientConfig openAIClientConfig;

    public WhisperTranscriptionResponse createTranscription(TranscriptionRequest transcriptionRequest){
        WhisperTranscriptionRequest whisperTranscriptionRequest = WhisperTranscriptionRequest.builder()
                .model(openAIClientConfig.getAudioModel())
                .file(transcriptionRequest.getFile())
                .build();
        return openAIClient.createTranscription(whisperTranscriptionRequest);
    }
}
