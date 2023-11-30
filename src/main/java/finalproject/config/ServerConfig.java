package finalproject.config;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class ServerConfig {
    @Bean
    public Faker faker() {
        return new Faker(Locale.ITALY);
    }
}
