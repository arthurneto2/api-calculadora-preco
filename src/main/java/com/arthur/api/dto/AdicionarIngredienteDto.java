package com.arthur.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdicionarIngredienteDto {

    private Long insumoId;

    private BigDecimal quantidade;

}
