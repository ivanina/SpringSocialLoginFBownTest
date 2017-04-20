package social_light;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.social.config.annotation.EnableSocial;



@EnableSocial
@EnableWebSecurity
//@EnableAutoConfiguration
@EnableJpaRepositories
//@EnableTransactionManagement
/*@EnableNeo4jRepositories(basePackages = {
        "social.entity.graph",
        "social.repository.graph"
})*/
//@SpringBootApplication(exclude = {FacebookAutoConfiguration.class, TwitterAutoConfiguration.class})
@SpringBootApplication
public class ApplicationSocialLight extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApplicationSocialLight.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationSocialLight.class, args); //NOSONAR
    }
}