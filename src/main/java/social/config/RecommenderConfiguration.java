package social.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.twitter.api.Tweet;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;

@Configuration
public class RecommenderConfiguration {

    private static final String DICTIONARY_SOURCE = "dictionary.txt";
    private static final Double DEFAULT_RANK = 1.0;
    private static final Double SPOTIFY_RANK = 5.0;

    @Bean
    public BiFunction<Post, KeywordsResult, Pair<String, Double>> facebookPostKeywordRanker(@Value("${recommender.watson.use_relevance}") String useRelevance) {
        return (post, keyword) -> watsonKeywordRanker(Boolean.parseBoolean(useRelevance)).apply(keyword);
    }

    @Bean
    public BiFunction<Tweet, KeywordsResult, Pair<String, Double>> tweetKeywordRanker(@Value("${recommender.watson.use_relevance}") String useRelevance) {
        return (tweet, keyword) -> watsonKeywordRanker(Boolean.parseBoolean(useRelevance)).apply(keyword);
    }

    @Bean
    public Function<String, Pair<String, Double>> spotifyKeywordRanker() {
        return keyword -> Pair.create(keyword, SPOTIFY_RANK);
    }

    @Bean
    public Function<String, Pair<String, Double>> defaultRanker() {
        return keyword -> Pair.create(keyword, DEFAULT_RANK);
    }
    
    @Bean
    public Function<String, Pair<String, Double>> twitterFollowRanker() {
        return keyword -> Pair.create(keyword, 2.0);
    }

    @Bean
    public Set<String> dictionary(ClassPathResource dictionaryResource) throws IOException {
        try (InputStream inputStream = dictionaryResource.getInputStream()){
            Reader reader = new InputStreamReader(inputStream);
            return new BufferedReader(reader).lines()
                    .map(w -> w.split(";"))
                    .flatMap(Stream::of)
                    .flatMap(w -> Stream.concat(Stream.of(w), Stream.of(w.split(" "))))
                    .filter(w -> w.length() > 1)
                    .collect(Collectors.toSet());
        }
    }

    @Bean
    public ClassPathResource dictionaryResource() {
        return new ClassPathResource(DICTIONARY_SOURCE);
    }

    private Function<KeywordsResult, Pair<String, Double>> watsonKeywordRanker(boolean useRelevance) {
        return keyword -> {
//            Double rank = keyword.getRelevance();
//            Sentiment sentiment = keyword.getSentiment();
//            if (sentiment != null) {
//                rank += sentiment.getScore();
//            }
            if (useRelevance) {
                return Pair.create(keyword.getText(), DEFAULT_RANK * keyword.getRelevance());
            } else {
                return Pair.create(keyword.getText(), DEFAULT_RANK);
            }
        };
    }
}