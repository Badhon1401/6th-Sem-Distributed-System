package com.bsse1401.user_service.utils;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "student|faculty", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Role must be student or faculty")
    private String role;
}
