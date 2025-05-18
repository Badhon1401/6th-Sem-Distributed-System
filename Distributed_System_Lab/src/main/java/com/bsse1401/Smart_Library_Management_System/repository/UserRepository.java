package com.bsse1401.Smart_Library_Management_System.repository;

import com.bsse1401.Smart_Library_Management_System.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}

