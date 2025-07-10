package com.msvc.users.service;

import com.msvc.users.dto.request.UserCreationRequestDTO;
import com.msvc.users.dto.request.UserEditRequestDTO;
import com.msvc.users.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> findAll();

    UserResponseDTO save(UserCreationRequestDTO user);

    UserResponseDTO update(UserEditRequestDTO user, String username);

    void deleteById(Long id);
}
