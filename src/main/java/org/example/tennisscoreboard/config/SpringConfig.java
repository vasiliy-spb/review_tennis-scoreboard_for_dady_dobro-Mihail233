package org.example.tennisscoreboard.config;

import org.example.tennisscoreboard.domain.service.OngoingMatchesDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("org.example.tennisscoreboard")
public class SpringConfig {

    @Bean
    OngoingMatchesDomainService createOngoingMatchesDomainService() {
        return new OngoingMatchesDomainService();
    }
}
