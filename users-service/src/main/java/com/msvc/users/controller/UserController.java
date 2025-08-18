package com.msvc.users.controller;

import com.msvc.users.dto.mapper.UserMapper;
import com.msvc.users.dto.request.LoginRequestDTO;
import com.msvc.users.dto.request.UserCreationRequestDTO;
import com.msvc.users.dto.request.UserEditRequestDTO;
import com.msvc.users.dto.response.UserResponseDTO;
import com.msvc.users.entity.User;
import com.msvc.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/find-all")
    public List<UserResponseDTO> getAllUsers(){
        return userService.findAll();
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserCreationRequestDTO userCreationRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userCreationRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        String username = loginRequestDTO.getUsername();

        Optional<User> user = userService.findByUsername(username);

        if (user.isPresent()) {
            return ResponseEntity.ok(userMapper.toDto(user.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "User not found"));
    }

    @PutMapping("/edit/{username}")
    public ResponseEntity<UserResponseDTO> editUser(@RequestBody UserEditRequestDTO userEdited, @PathVariable String username){
        UserResponseDTO user = userService.update(userEdited, username);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping
    public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable Long id){
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
