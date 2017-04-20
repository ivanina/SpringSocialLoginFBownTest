package social.controller;

import social.model.KeywordMetadata;
import social.model.RecommendedEvent;
import social.service.recommendation.EventRecommendationService;
import social.service.FacebookKeywordService;
import social.service.SpotifyKeywordService;
import social.service.ipaddr.IPAddressInfo;
import social.service.ipaddr.IPAddressService;
import social.service.twitter.TwitterKeywordService;
import social.model.RecommendResponse;
import social.service.keyword.CompositeKeywordCollector;
import social.service.keyword.KeywordCollector;
import com.ticketmaster.api.discovery.operation.SearchEventsOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("api/recommendations")
public class RecommendationResource {

    private final Logger logger = LoggerFactory.getLogger(RecommendationResource.class);

    private final FacebookKeywordService facebookKeywordService;
    private final TwitterKeywordService twitterKeywordService;
    private final SpotifyKeywordService spotifyKeywordService;
    private final EventRecommendationService eventRecommendationService;
    private final IPAddressService ipAddressService;

    @Inject
    public RecommendationResource(FacebookKeywordService facebookKeywordService,
                                  TwitterKeywordService twitterKeywordService,
                                  SpotifyKeywordService spotifyKeywordService,
                                  EventRecommendationService eventRecommendationService,
                                  IPAddressService ipAddressService) {
        this.facebookKeywordService = facebookKeywordService;
        this.twitterKeywordService = twitterKeywordService;
        this.spotifyKeywordService = spotifyKeywordService;
        this.eventRecommendationService = eventRecommendationService;
        this.ipAddressService = ipAddressService;
    }

    @GetMapping
    public RecommendResponse getRecommendations(HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();
        logger.info("Receive request to /api/recommendations, assigned id = " + requestId);

        KeywordCollector facebookKeywordCollector = facebookKeywordService.keywordCollector();
        KeywordCollector twitterKeywordCollector = twitterKeywordService.keywordCollector();
        KeywordCollector spotifyKeywordCollector = spotifyKeywordService.keywordCollector();
        KeywordCollector allKeywordCollector = new CompositeKeywordCollector(
                facebookKeywordCollector,
                twitterKeywordCollector,
                spotifyKeywordCollector
        );
        Map<String, KeywordMetadata> keywords = allKeywordCollector.collect();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        SearchEventsOperation fallbackQuery = new SearchEventsOperation();
        if (!request.getLocalAddr().equals(ipAddress) && ipAddressService.getInfo(ipAddress).getCity() != null
                && !ipAddressService.getInfo(ipAddress).getCity().isEmpty()) {
            IPAddressInfo ipAddressInfo = ipAddressService.getInfo(ipAddress);
            fallbackQuery
                    .latlong(ipAddressInfo.getLatitude().toString(), ipAddressInfo.getLongitude().toString())
                    .countryCode(ipAddressInfo.getCountryCode())
                    .city(ipAddressInfo.getCity());
        }
        //List<RecommendedEvent> recommendedEvents = eventRecommendationService.getEvents(keywords, fallbackQuery);
        List<RecommendedEvent> recommendedEvents = new ArrayList<>();

        long endTime = System.currentTimeMillis();
        logger.info("Finished request to /api/recommendations with id = " + requestId + " in " + (endTime - startTime) / 1000 + "sec");

        return new RecommendResponse(recommendedEvents);
    }

}
