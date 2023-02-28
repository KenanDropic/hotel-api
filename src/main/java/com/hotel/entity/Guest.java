package com.hotel.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "guest")
public class Guest {
    public Guest(List<Reservation> reservations, String firstName, String lastName, String email,
                 String phone, String gender) {
        this.reservations = reservations;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
    }

    public Guest(String firstName, String lastName, String email,
                 String phone, String gender, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    @Id
    @SequenceGenerator(name = "guest_id", sequenceName = "guest_id", allocationSize = 1, initialValue = 6)
    @GeneratedValue(generator = "guest_id", strategy = SEQUENCE)
    @Column(name = "guest_id")
    private Long id;

    @JsonBackReference
    @OneToMany(mappedBy = "guest", cascade = {DETACH, MERGE, PERSIST, REFRESH})
    private List<Reservation> reservations;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @Column(unique = true)
    @NotNull
    private String email;

    @NotNull
    private String phone;

    @NotNull
    private String gender;

    @NotNull
    private LocalDate dateOfBirth;

    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }
}
