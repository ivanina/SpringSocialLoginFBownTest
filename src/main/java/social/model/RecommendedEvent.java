package social.model;

import com.ticketmaster.discovery.model.Date;
import com.ticketmaster.discovery.model.Event;
import com.ticketmaster.discovery.model.Image;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode(of = "name")
public class RecommendedEvent {

    private String url;
    private String name;
    private Image image;
    private Date dates;

    private EventMetadata eventMetadata;

    private Map<String, KeywordMetadata> keywords = new HashMap<>();
    private Double rank;
    
    public RecommendedEvent(Event event, EventMetadata metadata) {
        this.url = event.getUrl();
        this.name = event.getName();
        this.image = event.getImages().iterator().next();
        this.dates = event.getDates();
        this.eventMetadata = metadata;
        metadata.getKeywords().forEach(eventPair -> this.keywords.put(eventPair.getKey(), eventPair.getValue()));
        this.rank = metadata.getRank();
    }
    
}
