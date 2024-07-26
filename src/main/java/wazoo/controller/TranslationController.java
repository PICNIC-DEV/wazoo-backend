package wazoo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wazoo.service.TranslationService;

@RestController
public class TranslationController {

    @Autowired
    private TranslationService translationService;

    @GetMapping("/translate")
    public String translate(@RequestParam String text, @RequestParam String targetLanguage, @RequestParam String sourceLanguage) {
        return translationService.translateText(text, targetLanguage, sourceLanguage);
    }
}