package com.bsse1401.stats_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {


    private Long id;


    private String name;


    private String email;


    private String role;
}
