package com.example.backend.payload.response;


import com.example.backend.payload.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private UserDTO userDto;
  private List<String> roles;

  public JwtResponse(String accessToken, UserDTO userDto, List<String> roles) {
    this.token = accessToken;
    this.userDto = userDto;
    this.roles = roles;
  }

}
