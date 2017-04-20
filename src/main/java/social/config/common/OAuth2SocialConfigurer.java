package social.config.common;

import social.config.properties.Oauth2SocialProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

@RequiredArgsConstructor
public abstract class OAuth2SocialConfigurer<P extends Oauth2SocialProperties, A> extends SocialConfigurer<A> {

    private final P properties;

    @Override
    protected final OAuth2ConnectionFactory<A> createConnectionFactory() {
        OAuth2ConnectionFactory<A> connectionFactory = createConnectionFactory(properties);
        connectionFactory.setScope(properties.getScope());
        return connectionFactory;
    }

    protected abstract OAuth2ConnectionFactory<A> createConnectionFactory(P properties);

}
