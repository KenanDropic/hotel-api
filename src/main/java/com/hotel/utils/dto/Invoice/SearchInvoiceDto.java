package com.hotel.utils.dto.Invoice;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class SearchInvoiceDto {
    @Nullable
    private Double price;

    private String comparisonOperator = "greater";
    private String field = "invoice_amount";
    private String direction = "ASC";
}
