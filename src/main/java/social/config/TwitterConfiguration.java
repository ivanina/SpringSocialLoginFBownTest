package social.config;

import social.config.common.SocialConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.social.TwitterProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@Configuration
@EnableConfigurationProperties(TwitterProperties.class)
@RequiredArgsConstructor
public class TwitterConfiguration extends SocialConfigurer<Twitter> {

    private final TwitterProperties properties;

    @Override
    protected ConnectionFactory<Twitter> createConnectionFactory() {
        return new TwitterConnectionFactory(properties.getAppId(), properties.getAppSecret());
    }

    @Override
    protected Class<Twitter> apiBindingClass() {
        return Twitter.class;
    }

}
