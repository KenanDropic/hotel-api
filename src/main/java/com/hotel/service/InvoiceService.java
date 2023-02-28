package com.hotel.service;

import com.hotel.entity.Invoice;
import com.hotel.exception.exceptions.NotFoundExc;
import com.hotel.repository.InvoiceRepository;
import com.hotel.utils.dto.Invoice.SearchInvoiceDto;
import com.hotel.utils.paginationSorting.Pagination;
import com.hotel.utils.paginationSorting.Sorting;
import com.hotel.utils.payload.PaginationResponse;
import com.hotel.utils.payload.ResponsePayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public ResponseEntity<PaginationResponse> findAllInvoicesWithPaginationAndSorting
            (SearchInvoiceDto searchParams, int page, int pageSize) {
        Sorting sorting = new Sorting();
        sorting.containsDirection(searchParams.getDirection());
        sorting.containsField(List.of("invoice_amount", "invoice_date", "invoice_id"), searchParams.getField());

        Sort sort = searchParams.getDirection().equals("ASC") ?
                Sort.by(Objects.requireNonNull(searchParams.getField()).equals("invoice_amount") ?
                        "invoice_amount" :
                        Objects.requireNonNull(searchParams.getField()).equals("invoice_id") ?
                                "invoice_id" :
                                Objects.requireNonNull(searchParams.getField().equals("invoice_date") ?
                                        "invoice_date" :
                                        "invoice_id")).ascending() :
                Sort.by(Objects.requireNonNull(searchParams.getField()).equals("invoice_amount") ?
                        "invoice_amount" :
                        Objects.requireNonNull(searchParams.getField()).equals("invoice_id") ?
                                "invoice_id" :
                                Objects.requireNonNull(searchParams.getField().equals("invoice_date") ?
                                        "invoice_date" :
                                        "invoice_id")).descending();

        Pageable paging = page == 1 ? PageRequest.of(0, pageSize, sort) :
                PageRequest.of(page - 1, pageSize);

        Page<Invoice> invoices = this.invoiceRepository.findAllInvoices(searchParams, paging);

        if (invoices.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new PaginationResponse(true, 0, invoices.getTotalPages(),
                            page, invoices.getContent()));
        }

        Pagination pagination = new Pagination();
        pagination.doesHaveNext(invoices, page);

        return ResponseEntity
                .status(200)
                .body(new PaginationResponse(true, invoices.getSize(),
                        invoices.getTotalElements(), invoices.getTotalPages(), page,
                        pagination.getPagination(), invoices.getContent()));
    }

    public ResponseEntity<ResponsePayload<Invoice>> findInvoice(Long id) {
        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(true, this.invoiceRepository
                        .findById(id)
                        .orElseThrow(() -> new NotFoundExc("Invoice " + id + " not found!"))));
    }
}
