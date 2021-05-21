package ru.fenix2k.Chatter.server.EntityView;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserView implements Serializable {
    private long id;
    @NotEmpty
    @Size(min = 2, max = 100)
    private String login;
    @NotEmpty
    @Email
    private String email;
    private LocalDateTime dtRegister;
    private LocalDateTime dtLastLogin;
    @NotNull
    private Boolean isActive = false;
    @NotNull
    private Boolean isVisible = false;

}
