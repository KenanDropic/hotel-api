package com.hotel.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "reservation")
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

    public Reservation(LocalDateTime checkIn, LocalDateTime checkOut, Integer discount,
                       Guest guest, List<Room> rooms, Invoice invoice) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.discount_percent = discount;
        this.guest = guest;
        this.rooms = rooms;
        this.invoice = invoice;
    }

    @Id
    @SequenceGenerator(name = "reservation_id", sequenceName = "reservation_id", allocationSize = 1, initialValue = 11)
    @GeneratedValue(generator = "reservation_id", strategy = SEQUENCE)
    @Column(name = "reservation_id")
    private Long id;

    @NotNull
    private LocalDateTime checkIn;

    @NotNull
    private LocalDateTime checkOut;

    @NotNull
    private Integer discount_percent;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @JsonManagedReference
    @ManyToMany(mappedBy = "reservations", fetch = EAGER, cascade = {PERSIST, MERGE, REFRESH, DETACH})
    private List<Room> rooms;

    @JsonManagedReference
    @OneToOne(targetEntity = Invoice.class,
            fetch = EAGER,
            cascade = ALL)
    @JoinColumn(nullable = false, name = "invoice_id",
            foreignKey = @ForeignKey(name = "FK_RESERVATION_INVOICE"))
    private Invoice invoice;

    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }

}
