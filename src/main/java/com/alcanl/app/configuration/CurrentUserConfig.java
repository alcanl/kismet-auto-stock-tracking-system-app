package com.alcanl.app.configuration;

import com.alcanl.app.service.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Setter
@Getter
@Accessors(prefix = "m_")
@Configuration
public class CurrentUserConfig {

    private UserDTO m_user;

    @Primary
    @Bean(name = "bean.user.current")
    public CurrentUserConfig getCurrentUser()
    {
        return this;
    }
}
