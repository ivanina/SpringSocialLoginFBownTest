package social.config;

import social.config.common.OAuth2SocialConfigurer;
import social.config.properties.ExtendedFacebookProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

@Configuration
@EnableConfigurationProperties(ExtendedFacebookProperties.class)
public class FacebookConfiguration extends OAuth2SocialConfigurer<ExtendedFacebookProperties, Facebook> {

    public FacebookConfiguration(ExtendedFacebookProperties properties) {
        super(properties);
    }

    @Override
    public FacebookConnectionFactory createConnectionFactory(ExtendedFacebookProperties properties) {
        return new FacebookConnectionFactory(
                properties.getAppId(),
                properties.getAppSecret(),
                properties.getAppNamespace()
        );
    }

    @Override
    protected Class<Facebook> apiBindingClass() {
        return Facebook.class;
    }

}
