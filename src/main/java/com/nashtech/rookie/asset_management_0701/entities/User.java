package com.nashtech.rookie.asset_management_0701.entities;

import com.nashtech.rookie.asset_management_0701.enums.EGender;
import com.nashtech.rookie.asset_management_0701.enums.EUserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String username;

    private String hashPassword;

    private LocalDateTime dob;

    private LocalDateTime joinDate;

    @Column(unique = true)
    private String staffCode;

    @Enumerated(EnumType.STRING)
    private EUserStatus status;

    @Enumerated(EnumType.STRING)
    private EGender gender;

    @ManyToOne
    private Location location;

    @ManyToOne
    private Role role;
}
