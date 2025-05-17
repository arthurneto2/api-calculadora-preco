package com.arthur.api.dto;

import com.arthur.api.domain.Insumo;
import lombok.Data;

import java.math.BigDecimal;

@Data

public class InsumoDto {
    private Long id;

    private String nome;

    private String unMedida;

    private BigDecimal custoUn;

    public InsumoDto(Insumo insumo){
        id = insumo.getId();
        nome = insumo.getNome();
        unMedida = insumo.getUnMedida();
        custoUn = insumo.getCustoUn();
    }

    public InsumoDto(){
    }

}
