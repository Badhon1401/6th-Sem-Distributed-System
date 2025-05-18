package com.bsse1401.user_service.service;

import com.bsse1401.user_service.model.User;
import com.bsse1401.user_service.repository.UserRepository;
import com.bsse1401.user_service.utils.UserRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;

    public User createUser(UserRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        return userRepo.save(user);
    }

    public User getUser(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    public long count() {
        return userRepo.count();
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }
}

