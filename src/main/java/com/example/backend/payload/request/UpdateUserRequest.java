package com.example.backend.payload.request;

import com.example.backend.security.config.AppConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    @NotBlank(message = "Please input!")
    private String firstName;

    @NotBlank(message = "Please input!")
    private String lastName;

    @NotBlank(message = "Please input address!")
    private String address;

    @NotBlank(message = "Please input phone number!")
    //@Length(min = 10, max = 10, message = "Phone number must have 10 digits")
    @Pattern(regexp = AppConstants.PHONE_REGEX, message = "Phone number only contain numbers")
    private String phone;

}
