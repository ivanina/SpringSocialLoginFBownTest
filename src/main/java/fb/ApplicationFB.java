package fb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EntityScan(basePackages = "fb.entity")
@EnableJpaRepositories(basePackages = "fb.repository")
public class ApplicationFB {
    public static void main(String[] args) {
        SpringApplication.run(fb.ApplicationFB.class, args);
    }
}