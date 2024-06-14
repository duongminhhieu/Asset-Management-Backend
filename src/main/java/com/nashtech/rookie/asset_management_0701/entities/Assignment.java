package com.nashtech.rookie.asset_management_0701.entities;

import com.nashtech.rookie.asset_management_0701.enums.EAssignmentState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assignments")
public class Assignment extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime assignedDate;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    private EAssignmentState state;

    @ManyToOne
    private User assignTo;

    @ManyToOne
    private User assignBy;

    @ManyToOne
    private Asset asset;
}
