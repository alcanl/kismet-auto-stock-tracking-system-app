package com.alcanl.app.runner;

import com.alcanl.app.service.UserService;
import com.alcanl.app.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Order(2)
@Component
@PropertySource(value = "classpath:values.properties", encoding = "UTF-8")
public class AdminUserRunner implements ApplicationRunner {

    private final UserService m_userService;
    private final PasswordEncoder m_passwordEncoder;

    @Value("${kismet.auto.stock.tracking.system.admin.username}")
    private String adminUserName;
    @Value("${kismet.auto.stock.tracking.system.admin.password}")
    private String adminPassword;
    @Value("${kismet.auto.stock.tracking.system.admin.email}")
    private String adminEmail;
    @Value("${kismet.auto.stock.tracking.system.admin.name}")
    private String adminFirstName;
    @Value("${kismet.auto.stock.tracking.system.admin.lastname}")
    private String adminLastName;
    @Value("${kismet.auto.stock.tracking.system.admin.description}")
    private String adminDescription;

    public AdminUserRunner(UserService userService, PasswordEncoder passwordEncoder)
    {
        m_userService = userService;
        m_passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!m_userService.isUserExist(adminUserName, adminPassword)) {
            var userDTO = new UserDTO();
            userDTO.setFirstName(adminFirstName);
            userDTO.setLastName(adminLastName);
            userDTO.setDescription(adminDescription);
            userDTO.setAdmin(true);
            userDTO.setEMail(adminEmail);
            userDTO.setPassword(m_passwordEncoder.encode(adminPassword));
            userDTO.setUsername(adminUserName);
            m_userService.saveUser(userDTO);
        }
    }
}
