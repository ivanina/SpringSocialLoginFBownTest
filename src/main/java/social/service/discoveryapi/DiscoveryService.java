package social.service.discoveryapi;

import social.model.RecommendedEvent;
import com.ticketmaster.api.discovery.operation.SearchEventsOperation;
import com.ticketmaster.discovery.model.Event;

import java.util.List;

public interface DiscoveryService {

    List<Event> searchEvents(String keyword);

    List<Event> requestEvents(SearchEventsOperation operation);

    List<RecommendedEvent> fillAmount(List<RecommendedEvent> events);
}
