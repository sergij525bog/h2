package dev.sbohdan.h2.event;

import dev.sbohdan.h2.entity.User;
import org.springframework.context.ApplicationEvent;

public class UserRegistrationEvent extends ApplicationEvent {
    private User user;
    private String activationCode;
    public UserRegistrationEvent(User user, String activationCode) {
        super(user);

        this.user = user;
        this.activationCode = activationCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
}
