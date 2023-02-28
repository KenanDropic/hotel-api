package com.hotel.utils.paginationSorting;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
public class ResultLength {

    public static <T> Boolean checkIfEmpty(Page<T> items, Integer offset) {
        return items.isEmpty();
    }
}
