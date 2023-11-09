package com.example.backend.payload.dto.account;

import com.example.backend.model.User;
import com.example.backend.security.config.AppConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by Admin on 11/8/2023
 */
@Data
public class AccountAddDTO {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "Phone is required")
    @Length(min = 10,max = 10,message = "Phone must have at least 10 numbers")
    @Pattern(regexp = AppConstants.PHONE_REGEX,message = "Phone only contain numbers")
    private String phone;


}
