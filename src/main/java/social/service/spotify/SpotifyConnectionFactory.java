package social.service.spotify;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;

public class SpotifyConnectionFactory extends OAuth2ConnectionFactory<Spotify> {

    public SpotifyConnectionFactory(String appId, String appSecret) {
        super("spotify", new SpotifyServiceProvider(appId, appSecret), new SpotifyApiAdapter());
    }

}
