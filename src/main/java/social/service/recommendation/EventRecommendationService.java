package social.service.recommendation;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.math3.util.Pair;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import social.model.EventMetadata;
import social.model.KeywordMetadata;
import social.model.RecommendedEvent;
import social.service.discoveryapi.DiscoveryService;
import com.ticketmaster.api.discovery.operation.SearchEventsOperation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Named
@EnableConfigurationProperties(RecommendationServiceProperties.class)
public class EventRecommendationService {

    @Inject
    private DiscoveryService discoveryService;

    @Inject
    private RecommendationServiceProperties properties;

    public List<RecommendedEvent> getEvents(Map<String, KeywordMetadata> keywords, SearchEventsOperation fallback) {
        log.info("Extract " + keywords.entrySet().size() + " keywords, now searching for events using them...");

        List<RecommendedEvent> events = keywords.entrySet()
                .stream()
                .map(entry -> Pair.create(entry.getKey().toLowerCase(), entry.getValue()))
                .flatMap(keywordMetadataPair -> discoveryService.searchEvents(keywordMetadataPair.getKey()).stream()
                        .limit(properties.getLimit())
                        .map(event -> Pair.create(event, keywordMetadataPair))
                )
                .collect(Collectors.groupingBy(
                        Pair::getKey,
                        HashMap::new,
                        Collectors.mapping(
                                pair -> {
                                    Pair<String, KeywordMetadata> keywordMetadataPair = pair.getValue();
                                    Double rank = keywordMetadataPair.getValue().getRank();
                                    return new EventMetadata(Collections.singleton(keywordMetadataPair), rank);
                                },
                                Collectors.reducing(
                                        new EventMetadata(),
                                        (first, second) -> {
                                            Double totalRank = first.getRank() + second.getRank();
                                            Set<Pair<String, KeywordMetadata>> totalKeywords = new HashSet<>();
                                            totalKeywords.addAll(first.getKeywords());
                                            totalKeywords.addAll(second.getKeywords());
                                            return new EventMetadata(totalKeywords, totalRank);
                                        }
                                )
                        )
                ))
                .entrySet()
                .stream()
                .sorted((first, second) -> second.getValue().getRank().compareTo(first.getValue().getRank()))
                .map(eventWithMetadata -> new RecommendedEvent(eventWithMetadata.getKey(), eventWithMetadata.getValue()))
                .distinct()
                .limit(properties.getMax())
                .collect(Collectors.toList());
        
        return discoveryService.fillAmount(events);
        
    }

}
