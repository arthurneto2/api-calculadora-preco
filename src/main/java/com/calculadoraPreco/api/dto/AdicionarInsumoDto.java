package com.calculadoraPreco.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdicionarInsumoDto {

    private Long insumoId;

    private BigDecimal quantidade;

}
