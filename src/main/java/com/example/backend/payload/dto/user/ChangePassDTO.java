package com.example.backend.payload.dto.user;

import com.example.backend.security.config.AppConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Pattern;
import lombok.*;



/**
 * Created by Admin on 10/25/2023
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassDTO {
    @NotBlank(message = "Please input current password")
    private String password;

    @NotBlank(message = "Please input new password")
    @Pattern(regexp = AppConstants.PASSWORD_REGEX, message = "Password must be 6 - 20  characters long " +
            "and combination of 1 uppercase letter, 1 lowercase letter, 1 number, 1 special character.")
    private String newPassword;

    @NotBlank(message = "Please confirm new password")
    private String confirmNewPassword;

}
