package com.example.backend.payload.request;

import com.example.backend.payload.dto.account.AccountAddDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.example.backend.security.config.AppConstants;
import lombok.Data;

//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Pattern;
//import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignupRequest {
  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank(message = "Email is required")
  @Email(message = "Email format must be @gmail.com")
  private String email;

  private Set<String> role;

  @NotBlank
  @Size(min = 6, max = 40)
  @Pattern(regexp = AppConstants.PASSWORD_REGEX, message = "Password must be 6 - 20  characters long " +
          "and combination of 1 uppercase letter, 1 lowercase letter, 1 number, 1 special character.")
  private String password;

  @Valid
  private AccountAddDTO accountAddDTO;
}
