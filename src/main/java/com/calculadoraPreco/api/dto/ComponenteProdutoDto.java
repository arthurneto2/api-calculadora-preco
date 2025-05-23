package com.calculadoraPreco.api.dto;


import com.calculadoraPreco.api.domain.ComponenteProduto;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ComponenteProdutoDto {

    private Long id;

    private BigDecimal quantidade;

    private InsumoDto insumoDto;


    public ComponenteProdutoDto(ComponenteProduto componenteProduto){
        id = componenteProduto.getId();
        quantidade = componenteProduto.getQuantidade();
        insumoDto = new InsumoDto(componenteProduto.getInsumo());
    }

    public ComponenteProdutoDto(){

    }
}
