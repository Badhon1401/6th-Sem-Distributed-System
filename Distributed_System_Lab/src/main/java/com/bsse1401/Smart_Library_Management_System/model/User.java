package com.bsse1401.Smart_Library_Management_System.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "library_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Column(unique = true)
    private String name;

    @Email(message = "Email must be a valid email address")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Role cannot be blank")
    @Pattern(
            regexp = "student|faculty",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Role must be either 'student' or 'faculty'"
    )
    private String role;
}
