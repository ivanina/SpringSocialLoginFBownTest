package social.service.discoveryapi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;

import social.model.EventMetadata;
import social.model.RecommendedEvent;
import social.service.recommendation.RecommendationServiceProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.ticketmaster.api.discovery.DiscoveryApi;
import com.ticketmaster.api.discovery.operation.SearchEventsOperation;
import com.ticketmaster.api.discovery.response.PagedResponse;
import com.ticketmaster.discovery.model.Event;
import com.ticketmaster.discovery.model.Events;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Named
public class DiscoveryServiceImpl implements DiscoveryService {

    private final static ObjectMapper JSON_MAPPER = (new ObjectMapper()).registerModule(new JodaModule()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(MapperFeature.USE_GETTERS_AS_SETTERS, false);

    @Inject
    private DiscoveryApi discoveryApi;

    @Inject
    private RecommendationServiceProperties properties; //NOSONAR

    @Value("${discovery.cache.enabled}")
    Boolean enableCache;

    private Map<String, List<Event>> cache = new HashMap<>();

    @Override
    public List<Event> searchEvents(String keyword) {
        List<Event> result = tryFromCache(keyword);
        if (result == null) {
            result = requestEvents(keyword);
            writeToCache(keyword, result);
        }
        return result;
    }

    private List<Event> requestEvents(String keyword) {
        log.info("Requesting events for [" + keyword + "] from ticketmaster");
        return requestEvents(keyword, null);
    }

    private List<Event> requestEvents(String keyword, String city) {
        return requestEvents(getSearchEventsOperation(keyword, city));
    }

    @Override
    public List<Event> requestEvents(SearchEventsOperation operation) {
        List<PagedResponse<Events>> events = new ArrayList<>();
        
        try {
            PagedResponse<Events> response = discoveryApi.searchEvents(operation);
            
            if(response.getContent() != null) {
                events.add(response);
            }
            
            while((response = discoveryApi.nextPage(response)) != null) {
                if(response.getContent() != null) {
                        events.add(response);
                } else {
                    break;
                }
                
            }
            
            return getEvents(events);
        } catch (Exception e) {
            log.warn("Failed with request for Discovery API, will retry", e);
        }
        
        log.warn("No events found. Return empty result");
        return getEvents(events);
        
    }

    @Override
    public List<RecommendedEvent> fillAmount(List<RecommendedEvent> events) {
        
        //events can't be null
        if (events == null) {
            throw new IllegalStateException("Events parameter can't be null");
        }
        
        //check if there's already enough events amount
        if (properties.getMin() - events.size() <= 0) {
            return events;
        }
        
        try {
            //search first events page
            PagedResponse<Events> response = discoveryApi.searchEvents(new SearchEventsOperation());

            Events firstPage = response.getContent();
            
            //add events into general collection
            if (firstPage != null) {
                events.addAll(firstPage.getEvents().stream()
                        .map(event -> new RecommendedEvent(event, new EventMetadata())).collect(Collectors.toList()));
            }
            
            //again check if there's already enough events amount
            if (properties.getMin() - events.size() <= 0) {
                return events;
            }
            
            //seek and collect events from pages
            //one by one until it would be enough
            while ((response = discoveryApi.nextPage(response)) != null) {

                Events content = response.getContent();

                if (content == null) {
                    break;
                }

                log.info("Found " + events.size() + " events, trying to add more...");

                Collection<RecommendedEvent> fallbackEvents = content.getEvents().stream()
                        .map(event -> new RecommendedEvent(event, new EventMetadata())).collect(Collectors.toList());

                if (fallbackEvents.isEmpty()) {
                    log.info("Found " + events.size() + " events, can't find any more...");
                    break;
                }

                events.addAll(fallbackEvents);

                if (properties.getMin() - events.size() <= 0) {
                    break;
                }
            }
            
        } catch (Exception e) {
            log.error("Error during events retrieving");
        }
        
        return events;
        
    }

    private List<Event> getEvents(List<PagedResponse<Events>> events) {
        return events.stream()
        .map(PagedResponse::getContent)
        .map(Events::getEvents)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
    }

    private SearchEventsOperation getSearchEventsOperation(String keyword, String city) {
        SearchEventsOperation eventsOperation = new SearchEventsOperation();
        eventsOperation.keyword(keyword);
        if (city != null && !city.isEmpty()) {
            eventsOperation.city(city);
        }
        return eventsOperation;
    }

    private List<Event> tryFromCache(String keyword) {
        if (!enableCache) {
            return null; //NOSONAR
        }

        if (cache.containsKey(keyword)) {
            log.info("Events for [" + keyword + "] loaded from hot cache");
            return cache.get(keyword);
        }

        if (!cacheFile(keyword).exists()) {
            return null; //NOSONAR
        }

        try {
            List<Event> result = readFromFile(keyword);
            cache.put(keyword, result);
            log.info("Events for [" + keyword + "] loaded from cold cache");

            return result;
        } catch (IOException e) {
            log.warn("Can't open file " + cacheFile(keyword) + " for reading", e);
            return null; //NOSONAR
        }
    }

    private List<Event> readFromFile(String keyword) throws IOException {
        return FileUtils.readLines(cacheFile(keyword)).stream().limit(properties.getLimit())
                .map(this::readLineFromCacheFile)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Event readLineFromCacheFile(String line) { //NOSONAR
        try {
            return JSON_MAPPER.readValue(line, Event.class);
        } catch (IOException e) {
            log.warn("Error while deserializing line [" + line + "] because of " + e.getMessage(), e);
            return null; //NOSONAR
        }
    }

    private void writeToCache(String keyword, List<Event> result) {
        if (!enableCache) {
            return;
        }

        try {
            cache.put(keyword, result);
            FileUtils.writeLines(cacheFile(keyword), result.stream().map(this::toCacheFileLine).filter(Objects::nonNull).collect(Collectors.toList()));
            log.info("Events for [" + keyword + "] saved to cache");
        } catch (IOException e) {
            log.warn("Failed to create cache file " + cacheFile(keyword) + " because of " + e.getMessage(), e);
        }
    }

    private String toCacheFileLine(Event event) { //NOSONAR
        try {
            return JSON_MAPPER.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.warn("Can't write event " + event.toString() + " to file", e);
            return null; //NOSONAR
        }
    }

    private File cacheFile(String text) {
        return new File(".cache/discovery", DigestUtils.md5Hex(text));
    }
}