package com.hotel.repository;

import com.hotel.entity.Invoice;
import com.hotel.utils.dto.Invoice.SearchInvoiceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query(value = """
            SELECT * FROM invoice i
            WHERE :#{#params.price} is null or (
            CASE
            WHEN cast(:#{#params.comparisonOperator} as character varying) = 'greater'
            OR :#{#params.comparisonOperator} is null
            THEN cast(invoice_amount as numeric) >= cast(cast(:#{#params.price} as text) as numeric)
            WHEN cast(:#{#params.comparisonOperator} as character varying) = 'less'
            THEN cast(invoice_amount as numeric) <= cast(cast(:#{#params.price} as text) as numeric)
            WHEN cast(:#{#params.comparisonOperator} as character varying) = 'equals'
            THEN cast(invoice_amount as numeric) = cast(cast(:#{#params.price} as text) as numeric) END)
            """, nativeQuery = true)
    Page<Invoice> findAllInvoices(SearchInvoiceDto params, Pageable pageable);
}
