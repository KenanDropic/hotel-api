package com.hotel.utils.dto.Guest;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class SearchGuestDto {
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;

    private String field = "guest_id";
    private String direction = "ASC";
}
