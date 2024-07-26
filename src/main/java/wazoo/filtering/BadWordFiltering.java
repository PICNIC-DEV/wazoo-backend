package wazoo.filtering;


import java.util.*;
import java.util.regex.*;

public class BadWordFiltering {
    private final Set<String> badWordsSet = new HashSet<>();
    private final Map<String, Pattern> badWordPatterns = new HashMap<>();

    public void addBadWords(Collection<String> badWords) {
        badWordsSet.addAll(badWords);
        compileBadWordPatterns();
    }

    private void compileBadWordPatterns() {
        String patternText = buildPatternText();
        for (String word : badWordsSet) {
            String[] chars = word.split("");
            badWordPatterns.put(word, Pattern.compile(String.join(patternText, chars), Pattern.CASE_INSENSITIVE));
        }
    }

    private String buildPatternText() {
        StringBuilder delimiterBuilder = new StringBuilder("[");
        String[] delimiters = {" ", ",", ".", "!", "?", "@", "1"};
        for (String delimiter : delimiters) {
            delimiterBuilder.append(Pattern.quote(delimiter));
        }
        delimiterBuilder.append("]*");
        return delimiterBuilder.toString();
    }

    public boolean checkBadWord(String input) {
        for (Pattern pattern : badWordPatterns.values()) {
            if (pattern.matcher(input).find()) {
                return true;
            }
        }
        return false;
    }

    public Set<String> findBadWords(String input) {
        Set<String> foundBadWords = new HashSet<>();
        for (Pattern pattern : badWordPatterns.values()) {
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                foundBadWords.add(matcher.group());
            }
        }
        return foundBadWords;
    }
}
