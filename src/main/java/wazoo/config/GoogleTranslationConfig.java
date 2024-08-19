package wazoo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleTranslationConfig {
    @Value("${google.credentials.path}")
    private String credentialsPath;

    @Bean
    public Translate translate() throws IOException {
        InputStream fileInputStream;
        try {
            fileInputStream = new ClassPathResource("classpath:google-credentials.json").getInputStream();
        } catch (FileNotFoundException e) {
            fileInputStream = new FileInputStream(credentialsPath);
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(fileInputStream)
                .createScoped("https://www.googleapis.com/auth/cloud-translation");
        TranslateOptions translateOptions = TranslateOptions.newBuilder()
                .setCredentials(credentials)
                .build();
        return translateOptions.getService();
    }
}
