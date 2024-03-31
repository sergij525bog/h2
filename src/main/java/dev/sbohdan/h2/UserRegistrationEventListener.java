package dev.sbohdan.h2;

import dev.sbohdan.h2.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationEventListener implements ApplicationListener<UserRegistrationEvent> {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    @Override
    public void onApplicationEvent(UserRegistrationEvent event) {
        logger.debug("Please confirm registration by sending this code " + event.getActivationCode());
    }
}
