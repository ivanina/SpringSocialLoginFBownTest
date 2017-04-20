package social.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Pair;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Invitation;
import org.springframework.social.facebook.api.Page;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.PagingParameters;
import org.springframework.social.facebook.api.Post;

import social.service.keyword.CompositeKeywordCollector;
import social.service.keyword.KeywordCollector;
import social.service.keyword.KeywordCollectorImpl;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Named
public class FacebookKeywordService extends SocialKeywordService<Facebook> {

    @Inject
    private Function<String, Pair<String, Double>> defaultRanker;

    @Inject
    private BiFunction<Post, KeywordsResult, Pair<String, Double>> facebookPostKeywordRanker;

    @Value("${facebook.keywords.posts.max}")
    private Integer maxPostsCount;

    @Value("${facebook.keywords.events.max}")
    private Integer maxEventsCount;

    @Value("${facebook.keywords.pages.max}")
    private Integer maxPagesCount;

    @Value("#{'${facebook.page.categoryes}'.split(',')}")
    private Set<String> catgories;

    @Override
    protected Class<Facebook> apiType() {
        return Facebook.class;
    }

    @Override
    public KeywordCollector keywordCollector(Facebook api) {
        return new CompositeKeywordCollector(pageKeywordCollector(api), postKeywordCollector(api), eventKeywordCollector(api));
    }

    private KeywordCollector pageKeywordCollector(Facebook api) {
        log.info("Collecting no more than " + maxPagesCount + " liked pages from facebook");

        Collection<Page> likedPages = new ArrayList<>();
        PagingParameters pageRequest = new PagingParameters(maxPagesCount, 0, null, null);
        while (pageRequest != null && likedPages.size() < maxPagesCount) {
            PagedList<Page> pageResponse = api.likeOperations().getPagesLiked(pageRequest);
            likedPages.addAll(pageResponse);
            pageRequest = pageResponse.getNextPage();
        }

        return KeywordCollectorImpl.<Page>builder()
                .src(likedPages.stream().filter(page -> facebookPageFilter(page)))
                .extractKeywords(page -> {
                    String keyword = page.getName().trim();
                    return Stream.of(defaultRanker.apply(keyword));
                })
                .mapSource(page -> "you've liked the page '" + page.getName() + "'")
                .build();
    }

    //TODO store users liked pages into tiketmasters profile
    private boolean facebookPageFilter(Page page) {
        if(page.getName() == null || page.getCategory() == null) {
            return false;
        }
        
        if(catgories.contains(page.getCategory())) {
            log.info("Liked facebook page will be processed " + page.getName() + " " + page.getCategory());
            return true;
        }
        
        log.info("Liked facebook page will be skipped " + page.getName() + " " + page.getCategory());
        return false;
    }

    private KeywordCollector eventKeywordCollector(Facebook api) {
        Collection<Invitation> allEvents = new ArrayList<>();

        log.info("Collecting no more than " + maxEventsCount + " attended events from facebook");
        Collection<Invitation> attendingEvents = new ArrayList<>();
        PagingParameters attendingEventsRequest = new PagingParameters(maxEventsCount, 0, null, null);
        while (attendingEventsRequest != null && allEvents.size() < maxEventsCount) {
            PagedList<Invitation> eventResponse = api.eventOperations().getAttending(attendingEventsRequest);
            attendingEvents.addAll(eventResponse);
            allEvents.addAll(eventResponse);
            attendingEventsRequest = eventResponse.getNextPage();
        }
        KeywordCollector attendingCollector = KeywordCollectorImpl.<Invitation>builder()
                .src(attendingEvents.stream().filter(event -> event.getName() != null))
                .extractKeywords(page -> {
                    String keyword = page.getName().trim();
                    return Stream.of(defaultRanker.apply(keyword));
                })
                .mapSource(event -> "you're attending event '" + event.getName() + "'")
                .build();

        log.info("Collecting no more than " + (maxEventsCount - allEvents.size()) + " maybe attended events from facebook");
        Collection<Invitation> maybeAttendingEvents = new ArrayList<>();
        PagingParameters maybeAttendingEventsRequest = new PagingParameters(maxEventsCount, 0, null, null);
        while (maybeAttendingEventsRequest != null && allEvents.size() < maxEventsCount) {
            PagedList<Invitation> eventResponse = api.eventOperations().getMaybeAttending(maybeAttendingEventsRequest);
            maybeAttendingEvents.addAll(eventResponse);
            allEvents.addAll(eventResponse);
            maybeAttendingEventsRequest = eventResponse.getNextPage();
        }
        KeywordCollector maybeAttendingCollector = KeywordCollectorImpl.<Invitation>builder()
                .src(maybeAttendingEvents.stream().filter(event -> event.getName() != null))
                .extractKeywords(page -> {
                    String keyword = page.getName().trim();
                    return Stream.of(defaultRanker.apply(keyword));
                })
                .mapSource(event -> "you're maybe attending event '" + event.getName() + "'")
                .build();

        log.info("Collecting no more than " + (maxEventsCount - allEvents.size()) + " created events from facebook");
        Collection<Invitation> createdEvents = new ArrayList<>();
        PagingParameters createdEventsRequest = new PagingParameters(maxEventsCount, 0, null, null);
        while (createdEventsRequest != null && allEvents.size() < maxEventsCount) {
            PagedList<Invitation> eventResponse = api.eventOperations().getCreated(createdEventsRequest);
            createdEvents.addAll(eventResponse);
            allEvents.addAll(eventResponse);
            createdEventsRequest = eventResponse.getNextPage();
        }
        KeywordCollector createdCollector = KeywordCollectorImpl.<Invitation>builder()
                .src(createdEvents.stream().filter(event -> event.getName() != null))
                .extractKeywords(page -> {
                    String keyword = page.getName().trim();
                    return Stream.of(defaultRanker.apply(keyword));
                })
                .mapSource(event -> "you're created event '" + event.getName() + "'")
                .build();

        return new CompositeKeywordCollector(attendingCollector, maybeAttendingCollector, createdCollector);
    }

    private KeywordCollector postKeywordCollector(Facebook api) {
        Collection<Post> posts = new ArrayList<>();
        PagingParameters postRequest = new PagingParameters(maxPostsCount, 0, null, null);
        while (postRequest != null && posts.size() < maxPostsCount) {
            PagedList<Post> postResponse = api.feedOperations().getFeed(postRequest);
            posts.addAll(postResponse);
            postRequest = postResponse.getNextPage();
        }

        log.info("Collecting no more than " + maxPostsCount + " posts from facebook");
        KeywordCollector postsCollector = KeywordCollectorImpl.<Post>builder()
                .src(posts.stream().filter(post -> post.getMessage() != null))
                .extractKeywords(post -> Stream.of(post.getMessage())
                        .flatMap(text -> nluService.getKeywords(text).stream())
                        .map(keyword -> facebookPostKeywordRanker.apply(post, keyword))
                )
                .mapSource(post -> "you've posted '" + post.getMessage() + "'")
                .build();

        log.info("Collecting reposts from facebook");
        KeywordCollector repostsCollector = KeywordCollectorImpl.<Post>builder()
                .src(posts.stream().filter(post -> post.getDescription() != null))
                .extractKeywords(post -> Stream.of(post.getName(), post.getDescription()).filter(Objects::nonNull)
                        .flatMap(text -> nluService.getKeywords(text).stream())
                        .map(keyword -> facebookPostKeywordRanker.apply(post, keyword))
                )
                .mapSource(post -> "you've reposted '" + post.getDescription() + "' by '" + post.getName())
                .build();

        log.info("Collecting shared links from facebook");
        KeywordCollector linksCollector = KeywordCollectorImpl.<Post>builder()
                .src(posts.stream().filter(post -> post.getLink() != null))
                .extractKeywords(post -> Stream.of(post.getLink())
                        .filter(link -> !link.startsWith("https://www.facebook.com"))
                        .map(link -> {
                            try {
                                log.info("Fetching title from " + link);
                                return Jsoup.connect(link).get().title();
                            } catch (IOException e) {
                                log.warn("Failed to fetch title from " + link + " because of " + e.getMessage(), e);
                                return null;
                            }
                        })
                        .filter(StringUtils::isNoneBlank)
                        .flatMap(text -> nluService.getKeywords(text).stream())
                        .map(keyword -> facebookPostKeywordRanker.apply(post, keyword))
                )
                .mapSource(post -> {
                    try {
                        return "you've shared link to '" + post.getLink() + "' entitled '" + Jsoup.connect(post.getLink()).get().title() + "'";
                    } catch (IOException e) {
                        log.warn("Failed to fetch title from " + post.getLink() + " because of " + e.getMessage(), e);
                        return "you've shared link to '" + post.getLink() + "'";
                    }
                })
                .build();

        return new CompositeKeywordCollector(postsCollector, repostsCollector, linksCollector);
    }
}
