package com.msvc.users.dto.request;

import lombok.Data;

@Data
public class UserCreationRequestDTO {

    private String username;

    private String password;

    private String email;
}
