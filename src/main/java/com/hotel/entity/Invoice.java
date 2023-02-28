package com.hotel.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.CascadeType.*;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class Invoice {

    public Invoice(Double invoiceAmount, LocalDateTime invoiceDate) {
        this.invoiceAmount = invoiceAmount;
        this.invoiceDate = invoiceDate;
    }

    @Id
    @SequenceGenerator(name = "invoice_id", sequenceName = "invoice_id", allocationSize = 1, initialValue = 11)
    @GeneratedValue(generator = "invoice_id", strategy = SEQUENCE)
    @Column(name = "invoice_id")
    private Long id;

    private Double invoiceAmount;
    private LocalDateTime invoiceDate;

    @JsonBackReference
    @OneToOne(mappedBy = "invoice",cascade = ALL)
    private Reservation reservation;

    @PrePersist
    protected void prePersist() {
        if (this.invoiceDate == null) this.invoiceDate = LocalDateTime.now();
    }
}
