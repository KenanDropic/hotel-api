package com.hotel.utils.dto.Guest;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CreateGuestDto {
    @NotNull(message = "Field cannot be empty")
    @Length(min = 3, max = 15)
    private String firstName;

    @NotNull(message = "Field cannot be empty")
    @Length(min = 3, max = 15)
    private String lastName;

    @Email
    @NotNull(message = "Field cannot be empty")
    private String email;

    @NotNull(message = "Field cannot be empty")
    @Pattern(regexp =  "^[+][0-9]{3}[\s-.]?6[1-5][\s-.]?[0-9]{3}[\s-.]?[0-9]{3,4}$",
            message = "Allowed number format examples: 1) +38761653789 2) +386 62 653 789 3) +384.64.653.7891 4) +387-63-653-789")
    private String phone;

    @NotNull(message = "Field cannot be empty")
    private String gender;

    @NotNull(message = "Field cannot be empty")
    @Past
    private LocalDate dateOfBirth;

}
