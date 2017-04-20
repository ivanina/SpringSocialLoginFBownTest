package social_light.config.common;

import social_light.util.ConnectionProvider;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.*;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionRepository;

import java.util.Optional;

public abstract class SocialConfigurer<A> extends SocialAutoConfigurerAdapter {
//
//    @Bean
//    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
//    public ConnectionProvider<A> socialConnection(ConnectionRepository repository) {
//        Connection<A> connection = repository.findPrimaryConnection(apiBindingClass());
//        return () -> Optional.ofNullable(connection);
//    }

    @Override
    protected abstract ConnectionFactory<A> createConnectionFactory();

    protected abstract Class<A> apiBindingClass();

}
