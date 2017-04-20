package social.service.recommendation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("recommendation")
public class RecommendationServiceProperties {

    private long max;
    private long min;
    private long limit;

}
