package com.nocta.user.management.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class SignupRequest {
  @NotBlank
  @Size(min = 3, max = 20)
  @Schema(example = "cihan")
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  @Schema(example = "cihan@gmail.com")
  private String email;

  @Schema(example = "[\"admin\"]")
  private Set<String> role;

  @NotBlank
  @Size(min = 6, max = 40)
  @Schema(example = "cihanpass")
  private String password;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<String> getRole() {
    return this.role;
  }

  public void setRole(Set<String> role) {
    this.role = role;
  }
}
