package social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.social.FacebookAutoConfiguration;
import org.springframework.boot.autoconfigure.social.TwitterAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableSocial
@EnableWebSecurity
@EnableJpaRepositories
@EnableTransactionManagement
@EnableNeo4jRepositories(basePackages = {
        "social.entity.graph",
        "social.repository.graph"
})
@SpringBootApplication(exclude = {FacebookAutoConfiguration.class, TwitterAutoConfiguration.class})
public class ApplicationSocial extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApplicationSocial.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationSocial.class, args); //NOSONAR
    }
}