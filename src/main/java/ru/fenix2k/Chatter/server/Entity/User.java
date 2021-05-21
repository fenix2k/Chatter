package ru.fenix2k.Chatter.server.Entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name = "id")
    private long id;

    @Column(name = "sys_dt_created")
    private LocalDateTime sys_created;
    @Column(name = "sys_dt_modified")
    private LocalDateTime sys_modified;
    @Column(name = "sys_removed")
    private Boolean sys_removed = false;

    @Column(name = "login")
    @NotEmpty
    @Size(min = 2, max = 100)
    private String login;

    @Column(name = "encryptedPassword")
    private String encryptedPassword;

    @Column(name = "email")
    @NotEmpty
    @Email
    private String email;

    @Column(name = "dtRegister")
    private LocalDateTime dtRegister;

    @Column(name = "dtLastLogin")
    private LocalDateTime dtLastLogin;

    @Column(name = "isActive")
    @NotNull
    private Boolean isActive = false;

    @Column(name = "isVisible")
    @NotNull
    private Boolean isVisible = false;
    //@OneToOne
    //private Profile profile;

}
