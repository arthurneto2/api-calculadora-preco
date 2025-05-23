package com.calculadoraPreco.api.security.dto;


import com.calculadoraPreco.api.dto.UserDTO;

import java.util.List;

public record AuthorizationDTO(
        UserDTO user,
        String tokenJwt,
        String expirationDate,
        List<String> roles
) {

}
