package com.arthur.api.dto;

import com.arthur.api.domain.Insumo;
import com.arthur.api.domain.Produto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
public class ProductDto {

    private Long id;

    private String nome;

    private BigDecimal precoVenda;

    private BigDecimal custoTotal;

    private BigDecimal margemDeLucro;

    private Set<InsumoDto> insumoDtoSet;

    public ProductDto(Produto produto){
        id = produto.getId();
        nome = produto.getNome();
        precoVenda = produto.getPrecoVenda();
        custoTotal = produto.getCustoTotal();
        margemDeLucro = produto.getMargemDeLucro();


        if(produto.getInsumo() != null){
            insumoDtoSet = new HashSet<>();
            for (Insumo i : produto.getInsumo()){
                InsumoDto insumoDto = new InsumoDto(i);
                insumoDtoSet.add(insumoDto);
            }
        }
    }

    public ProductDto(){
    }


}
