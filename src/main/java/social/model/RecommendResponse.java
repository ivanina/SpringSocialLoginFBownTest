package social.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
public class RecommendResponse {

    private List<RecommendedEvent> events;

}
