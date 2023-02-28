package com.hotel.utils.dto.Invoice;

import com.hotel.entity.Reservation;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CreateInvoiceDto {
    @NotNull(message = "Field cannot be null")
    @NotEmpty(message = "Field cannot be empty")
    @PositiveOrZero
    private Double invoiceAmount;

    @NotNull(message = "Field cannot be null")
    @NotEmpty(message = "Field cannot be empty")
    @FutureOrPresent
    private LocalDateTime invoiceDate;

    @NotNull(message = "Field cannot be null")
    @NotEmpty(message = "Field cannot be empty")
    private Reservation reservation;
}
