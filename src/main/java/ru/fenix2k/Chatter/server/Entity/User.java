package ru.fenix2k.Chatter.server.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "User")
@Data
@NoArgsConstructor
@Builder
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "login")
    private String login;
    @Column(name = "encryptedPassword")
    private String encryptedPassword;
    @Column(name = "email")
    private String email;
    @Column(name = "dtRegister")
    private LocalDateTime dtRegister;
    @Column(name = "dtLastLogin")
    private LocalDateTime dtLastLogin;
    @Column(name = "isActive")
    private boolean isActive = false;
    @Column(name = "isVisible")
    private boolean isVisible = false;
    //@OneToOne
    //private Profile profile;

}
