package com.alcanl.app.configuration;

import com.alcanl.app.service.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Accessors(prefix = "m_")
@Configuration
public class CurrentUserConfig {

    private UserDTO m_user;
    @Bean
    public CurrentUserConfig getCurrentUser()
    {
        return this;
    }
}
