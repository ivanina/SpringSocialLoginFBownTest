package social.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.social.spotify")
public class SpotifyProperties extends Oauth2SocialProperties {

}
