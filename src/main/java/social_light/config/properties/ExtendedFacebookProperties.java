package social_light.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("spring.social.facebook")
public class ExtendedFacebookProperties extends Oauth2SocialProperties {

    private String appNamespace;

}
