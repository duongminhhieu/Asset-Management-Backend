package com.nashtech.rookie.asset_management_0701.entities;
import java.time.LocalDate;

import com.nashtech.rookie.asset_management_0701.enums.EAssignmentReturnState;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "returning_requests")
public class ReturningRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Assignment assignment;

    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    private EAssignmentReturnState state;

    @ManyToOne
    private User requestedBy;

    @ManyToOne
    private User acceptedBy;
}
