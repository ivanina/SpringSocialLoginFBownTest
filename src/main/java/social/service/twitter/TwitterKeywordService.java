package social.service.twitter;

import static social.util.FunctionalUtils.stream;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.FriendOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;

import social.service.SocialKeywordService;
import social.service.keyword.CompositeKeywordCollector;
import social.service.keyword.KeywordCollector;
import social.service.keyword.KeywordCollectorImpl;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;

@Named
public class TwitterKeywordService extends SocialKeywordService<Twitter> {

    @Value("${tweet.limit}")
    private Integer tweetLimit;
    @Inject
    private Function<String, Pair<String, Double>> defaultRanker;
    @Inject
    private Function<String, Pair<String, Double>> twitterFollowRanker;
    @Inject
    private BiFunction<Tweet, KeywordsResult, Pair<String, Double>> tweetKeywordRanker;

    @Override
    protected Class<Twitter> apiType() {
        return Twitter.class;
    }

    @Override
    public KeywordCollector keywordCollector(Twitter api) {

        KeywordCollector tweetKeywordCollector = KeywordCollectorImpl.<Tweet>builder()
                .src(api.timelineOperations()
                        .getUserTimeline()
                        .stream()
                        .filter(tweet -> tweet.getText() != null)
                        .limit(tweetLimit)
                )
                .extractKeywords(tweet -> nluService.getKeywords(tweet.getText())
                        .stream()
                        .map(keyword -> tweetKeywordRanker.apply(tweet, keyword))
                )
                .mapSource(Tweet::getText)
                .build();

        KeywordCollector friendsKeywordCollector = KeywordCollectorImpl.<TwitterProfile>builder()
                .src(getTwitterFollowing(api)
                        .limit(tweetLimit)
                )
                .mapSource(TwitterProfile::getName)
                .extractKeywords(twitterProfile -> Stream.of(twitterFollowRanker.apply(twitterProfile.getName())))
                .build();

        return new CompositeKeywordCollector(tweetKeywordCollector, friendsKeywordCollector);
    }

    private Stream<TwitterProfile> getTwitterFollowing(Twitter api) {
        FriendOperations friendApi = api.friendOperations();
        UnaryOperator<CursoredList<TwitterProfile>> getNext = page -> {
            long reference = page.getNextCursor();
            return reference == 0 ? null : friendApi.getFriendsInCursor(reference);
        };
        return stream(friendApi.getFriends(), getNext)
                .flatMap(CursoredList::stream);
    }

}
