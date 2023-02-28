package com.hotel.controller;

import com.hotel.entity.Invoice;
import com.hotel.service.InvoiceService;
import com.hotel.utils.dto.Invoice.SearchInvoiceDto;
import com.hotel.utils.payload.PaginationResponse;
import com.hotel.utils.payload.ResponsePayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<PaginationResponse> getInvoices
            (@Valid SearchInvoiceDto searchParams,
             @RequestParam(required = false, defaultValue = "1") Integer page,
             @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        return this.invoiceService.findAllInvoicesWithPaginationAndSorting(searchParams, page, pageSize);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePayload<Invoice>> getInvoice
            (@PathVariable("id") final Long roomId) {
        return this.invoiceService.findInvoice(roomId);
    }
}
