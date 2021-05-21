package ru.fenix2k.Chatter.server.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Profile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
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

    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "birthdate")
    private String birthdate;

}
