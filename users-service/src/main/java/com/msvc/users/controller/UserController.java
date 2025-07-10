package com.msvc.users.controller;

import com.msvc.users.dto.request.UserCreationRequestDTO;
import com.msvc.users.dto.request.UserEditRequestDTO;
import com.msvc.users.dto.response.UserResponseDTO;
import com.msvc.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/find-all")
    public List<UserResponseDTO> getAllUsers(){
        return userService.findAll();
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserCreationRequestDTO userCreationRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userCreationRequestDTO));
    }

    @PutMapping("/edit/username")
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
