package social.config;

import com.ticketmaster.api.discovery.DiscoveryApi;
import com.ticketmaster.api.discovery.DiscoveryApiConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscoveryApiConfig {

    @Bean
    public DiscoveryApi discoveryApi(@Value("${discovery_api.key}") String apiKey) {
        return new DiscoveryApi(apiKey, discoveryApiConfiguration());
    }

    //@Bean
    public DiscoveryApiConfiguration discoveryApiConfiguration() {
        return DiscoveryApiConfiguration.builder()
                .socketConnectTimeout(20000)
                .socketTimeout(20000)
                .build();
    }

}
