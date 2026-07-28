package org.example.tennisscoreboard.config;

import org.example.tennisscoreboard.domain.service.OngoingMatchesDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("org.example.tennisscoreboard")
public class SpringConfig implements WebMvcConfigurer {

    // В Spring обычно @Bean-методы объявляются как public
    // Вместо ручного создания бина можно использовать аннотацию @Component или @Service над OngoingMatchesDomainService
    @Bean
    OngoingMatchesDomainService createOngoingMatchesDomainService() {
        return new OngoingMatchesDomainService();
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
