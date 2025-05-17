package com.arthur.api.security.dto;


import com.arthur.api.dto.UserDTO;

import java.util.List;

public record AuthorizationDTO(
        UserDTO user,
        String tokenJwt,
        String expirationDate,
        List<String> roles
) {

}
