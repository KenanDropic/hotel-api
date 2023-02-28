package com.hotel.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "room")
public class Room {
    public Room(Integer roomNumber, String description, String roomType,
                Boolean allowedPet, Double current_price) {
        this.roomNumber = roomNumber;
        this.description = description;
        this.roomType = roomType;
        this.allowedPet = allowedPet;
        this.currentPrice = current_price;
    }

    @Id
    @SequenceGenerator(name = "room_id", sequenceName = "room_id", allocationSize = 1, initialValue = 11)
    @GeneratedValue(generator = "room_id", strategy = SEQUENCE)
    @Column(name = "room_id")
    private Long id;

    @NotNull
    private Integer roomNumber;

    @NotNull
    private String description;

    @NotNull
    private String roomType;

    @NotNull
    private Boolean allowedPet;

    @NotNull
    private Double currentPrice;

    @JsonBackReference
    @ManyToMany
    @JoinTable(name = "room_reservation",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "reservation_id"))
    private List<Reservation> reservations;

}
