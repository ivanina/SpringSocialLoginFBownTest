package social.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Named
public class NaturalLanguageUnderstandingService {

    @Inject
    private NaturalLanguageUnderstanding naturalLanguageUnderstanding;

    @Value("${natural.language.understanding.cache.enabled}")
    private Boolean enableCache;

    private Map<String, List<KeywordsResult>> cache = new HashMap<>();

    public synchronized List<KeywordsResult> getKeywords(String text) {
        List<KeywordsResult> result = tryFromCache(text);
        if (result == null) {
            result = requestKeywords(text);
        }
        return result;
    }

    public synchronized List<KeywordsResult> requestKeywords(String text) {
        log.info("Requesting keywords for [" + text + "] from nlu");
        List<KeywordsResult> keywords = null;
        try {
            KeywordsOptions keywordsOptions = new KeywordsOptions.Builder().sentiment(true).build();
            Features features = new Features.Builder().keywords(keywordsOptions).build();
            AnalyzeOptions parameters = new AnalyzeOptions.Builder().text(text).features(features)
                    .returnAnalyzedText(true).build();
            keywords = naturalLanguageUnderstanding.analyze(parameters).execute().getKeywords();
        } catch (Exception exception) {
            log.error("Error while calling nlu service", exception);
        }

        if (keywords != null && !keywords.isEmpty()) {
            writeToCache(text, keywords);
            return keywords;
        } else {
            log.warn("Unable to request keywords for '" + text + "', will just split it to individual words");
            return splitToWords(text);
        }
    }

    private List<KeywordsResult> splitToWords(String text) {
        return Stream.of(StringUtils.split(text)) //NOSONAR
                .map(token -> {
                    int index = 0;
                    while (index < token.length()) {
                        if (!Character.isAlphabetic(token.charAt(index))) {
                            token = token.replace(token.substring(index, index + 1), "");
                        } else {
                            index++;
                        }
                    }
                    return token;
                })
                .filter(StringUtils::isNoneBlank)
                .filter(word -> word.length() > 3)
                .map(word -> {
                    KeywordsResult keyword = new KeywordsResult();
                    keyword.setText(word);
                    keyword.setRelevance(0.5);
                    return keyword;
                })
                .collect(Collectors.toList());
    }

    private List<KeywordsResult> tryFromCache(String text) {
        if (!enableCache) {
            return null; //NOSONAR
        }

        if (cache.containsKey(text)) {
            log.info("Keywords for [" + text + "] loaded from hot cache");
            return cache.get(text);
        }

        if (!cacheFile(text).exists()) {
            return null; //NOSONAR
        }

        try {
            List<KeywordsResult> result = readFromCache(text);
            cache.put(text, result);
            log.info("Keywords for [" + text + "] loaded from cold cache");

            return result;
        } catch (IOException e) {
            log.warn("Can't open file " + cacheFile(text) + " for reading", e);
            return null; //NOSONAR
        }
    }

    private List<KeywordsResult> readFromCache(String text) throws IOException {
        return FileUtils.readLines(cacheFile(text)).stream()
                .map(line -> {
                    KeywordsResult keyword = new KeywordsResult();
                    if (line.contains(";")) {
                        keyword.setText(line.split(";")[1]);
                        keyword.setRelevance(Double.parseDouble(line.split(";")[0]));
                    } else {
                        log.warn("Cache file " + cacheFile(text) + " has syntax errors, line: " + line);
                        keyword.setText(line);
                        keyword.setRelevance(1.0);
                    }
                    return keyword;
                })
                .collect(Collectors.toList());
    }

    private void writeToCache(String text, List<KeywordsResult> result) {
        if (result != null && enableCache) {
            try {
                cache.put(text, result);
                FileUtils.writeLines(cacheFile(text), result.stream().filter(Objects::nonNull).map(keyword -> keyword.getRelevance() + ";" + keyword.getText()).collect(Collectors.toList()));
                log.info("Keywords for [" + text + "] saved to cache");
            } catch (IOException e) {
                log.warn("Failed to create cache file " + cacheFile(text) + " because of " + e.getMessage(), e);
            }
        }
    }

    private File cacheFile(String text) {
        return new File(".cache/nlu", DigestUtils.md5Hex(text));
    }
}
