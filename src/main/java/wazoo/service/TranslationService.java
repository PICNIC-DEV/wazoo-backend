package wazoo.service;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TranslationService {
    @Autowired
    private Translate translate;

    public String translateText(String text, String targetLanguage, String userLanguage) {
        Translation translation = translate.translate(
                text,
                Translate.TranslateOption.targetLanguage(targetLanguage),
                Translate.TranslateOption.sourceLanguage(userLanguage) // 원본 언어
        );
        return translation.getTranslatedText();
    }

    // 언어 감지
    public String detectLanguage(String text) {
        String detectLanguage = translate.detect(text).getLanguage();
        return detectLanguage;
    }

}