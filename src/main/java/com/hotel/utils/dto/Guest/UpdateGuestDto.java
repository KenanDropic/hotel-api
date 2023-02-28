package com.hotel.utils.dto.Guest;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UpdateGuestDto {

    @Nullable
    private String firstName;

    @Nullable
    private String lastName;

    @Nullable
    @Pattern(regexp = "^[+][0-9]{3}[\s-.]?6[1-5][\s-.]?[0-9]{3}[\s-.]?[0-9]{3,4}$",
            message = "Allowed number format examples: 1) +38761653789 2) +386 62 653 789 3) +384.64.653.7891 4) +387-63-653-789")
    private String phone;

    @Nullable
    @Past
    private LocalDate dateOfBirth;
}
