package ru.fenix2k.Chatter.server.EntityView;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class UserView implements Serializable {
    private long id;
    private String login;
    private String email;
    private LocalDateTime dtRegister;
    private LocalDateTime dtLastLogin;
    private boolean isActive = false;
    private boolean isVisible = false;
}
