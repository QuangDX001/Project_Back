package com.example.backend.payload.dto;

import com.example.backend.security.config.AppConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordAdminDTO {
    @NotBlank(message = "Please input new password")
//    @Pattern(regexp = AppConstants.PASSWORD_REGEX, message = "Password must be 6 - 20  characters long " +
//            "and combination of 1 uppercase letter, 1 lowercase letter, 1 number, 1 special character.")
    private String newPassword;

}
