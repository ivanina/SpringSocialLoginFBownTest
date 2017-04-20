package social.service.spotify;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

public class SpotifyServiceProvider extends AbstractOAuth2ServiceProvider<Spotify> {

    public SpotifyServiceProvider(String appId, String appSecret) {
        super(getOAuth2Template(appId, appSecret));
    }

    private static OAuth2Template getOAuth2Template(String appId, String appSecret) {
        OAuth2Template oAuth2Template = new OAuth2Template(appId, appSecret,
                "https://accounts.spotify.com/authorize",
                "https://accounts.spotify.com/api/token");
        oAuth2Template.setUseParametersForClientAuthentication(true);
        return oAuth2Template;
    }

    @Override
    public Spotify getApi(String accessToken) {
        return new SpotifyTemplate(accessToken);
    }

}
