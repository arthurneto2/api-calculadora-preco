package com.calculadoraPreco.api.security.dto;

import java.time.Instant;

public record TokenInfo(String token, Instant expirationDate) {

}
