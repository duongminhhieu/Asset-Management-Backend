package com.nashtech.rookie.asset_management_0701.entities;

import java.time.LocalDate;

import com.nashtech.rookie.asset_management_0701.enums.EGender;
import com.nashtech.rookie.asset_management_0701.enums.ERole;
import com.nashtech.rookie.asset_management_0701.enums.EUserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends AuditEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String username;

    private String hashPassword;

    private LocalDate dob;

    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    private ERole role;

    @Column(unique = true)
    private String staffCode;

    @Enumerated(EnumType.STRING)
    private EUserStatus status;

    @Enumerated(EnumType.STRING)
    private EGender gender;

    @ManyToOne(fetch = FetchType.EAGER)
    private Location location;

    public void generateStaffCode () {
        this.setStaffCode(String.format("SD%04d", getId()));
    }
}
