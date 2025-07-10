package com.msvc.users.service.imp;

import com.msvc.users.dto.mapper.UserMapper;
import com.msvc.users.dto.request.UserCreationRequestDTO;
import com.msvc.users.dto.request.UserEditRequestDTO;
import com.msvc.users.dto.response.UserResponseDTO;
import com.msvc.users.entity.User;
import com.msvc.users.repository.UserRepository;
import com.msvc.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream().map(user -> userMapper.toDto(user)).toList();
    }

    @Override
    @Transactional
    public UserResponseDTO save(UserCreationRequestDTO userDTO) {
        User user = userMapper.toEntity(userDTO);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDTO update(UserEditRequestDTO user, String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User userDB = userOptional.get();
            userMapper.toEntity(user, userDB);

            return userMapper.toDto(userRepository.save(userDB));
        }
        throw new RuntimeException("User not found");
    }

    @Override
    public void deleteById(Long id) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found"));

            userRepository.delete(user);
    }
}
