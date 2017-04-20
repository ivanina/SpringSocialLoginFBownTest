package social_light.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.social.SocialProperties;

@Getter
@Setter
public abstract class Oauth2SocialProperties extends SocialProperties {

    private String scope;

}
