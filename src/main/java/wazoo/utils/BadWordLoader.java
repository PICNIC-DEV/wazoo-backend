package wazoo.utils;

import java.io.*;
import java.util.*;

public class BadWordLoader {

    public static List<String> loadBadWords(String fileName) throws IOException {
        List<String> badWords = new ArrayList<>();
        try (InputStream inputStream = BadWordLoader.class.getClassLoader().getResourceAsStream("badwords.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                badWords.add(line.trim());
            }
        }
        return badWords;
    }
}