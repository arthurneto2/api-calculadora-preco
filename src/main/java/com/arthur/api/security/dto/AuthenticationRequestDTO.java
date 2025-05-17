package com.arthur.api.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties
public record AuthenticationRequestDTO(

        @Email(message = "Formato do login inválido!")
        String login,

        @NotBlank(message = "É obrigatório enviar uma senha!")
        String password

){ }
