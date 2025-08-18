package com.msvc.users.service;

import com.msvc.users.dto.request.UserCreationRequestDTO;
import com.msvc.users.dto.request.UserEditRequestDTO;
import com.msvc.users.dto.response.UserResponseDTO;
import com.msvc.users.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponseDTO> findAll();

    Optional<User> findByUsername(String username);

    UserResponseDTO save(UserCreationRequestDTO user);

    UserResponseDTO update(UserEditRequestDTO user, String username);

    void deleteById(Long id);
}
