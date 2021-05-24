package ru.fenix2k.Chatter.server.Entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fenix2k.Chatter.server.View.Views;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "User")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Min.class)
    private long id;

    @JsonView(Views.Full.class)
    private LocalDateTime sys_created;
    @JsonView(Views.Full.class)
    private LocalDateTime sys_modified;
    @JsonView(Views.Full.class)
    private Boolean sys_removed = false;

    @NotEmpty
    @Size(min = 2, max = 100)
    @JsonView(Views.Min.class)
    private String login;

    @JsonView(Views.Full.class)
    private String encryptedPassword;

    @NotEmpty
    @Email
    @JsonView(Views.Min.class)
    private String email;

    @JsonView(Views.Middle.class)
    private LocalDateTime dtRegister;
    @JsonView(Views.Middle.class)
    private LocalDateTime dtLastLogin;

    @NotNull
    @JsonView(Views.Min.class)
    private Boolean isActive = false;

    @NotNull
    @JsonView(Views.Full.class)
    private Boolean isVisible = false;
    //@OneToOne
    //private Profile profile;


    public User(long id, String login, String encryptedPassword, String email, LocalDateTime dtRegister, LocalDateTime dtLastLogin, Boolean isActive, Boolean isVisible) {
        this.id = id;
        this.login = login;
        this.encryptedPassword = encryptedPassword;
        this.email = email;
        this.dtRegister = dtRegister;
        this.dtLastLogin = dtLastLogin;
        this.isActive = isActive;
        this.isVisible = isVisible;
    }
}
