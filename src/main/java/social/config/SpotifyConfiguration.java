package social.config;

import social.config.common.OAuth2SocialConfigurer;
import social.config.properties.SpotifyProperties;
import social.service.spotify.Spotify;
import social.service.spotify.SpotifyConnectionFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SpotifyProperties.class)
public class SpotifyConfiguration extends OAuth2SocialConfigurer<SpotifyProperties, Spotify> {

    public SpotifyConfiguration(SpotifyProperties properties) {
        super(properties);
    }

    @Override
    protected SpotifyConnectionFactory createConnectionFactory(SpotifyProperties properties) {
        return new SpotifyConnectionFactory(properties.getAppId(), properties.getAppSecret());
    }

    @Override
    protected Class<Spotify> apiBindingClass() {
        return Spotify.class;
    }

}
