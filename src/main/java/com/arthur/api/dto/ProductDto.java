package com.arthur.api.dto;

import com.arthur.api.domain.ComponenteProduto;
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

    private Set<ComponenteProdutoDto> componenteProdutoDtoSet;

    public ProductDto(Produto produto){
        id = produto.getId();
        nome = produto.getNome();
        precoVenda = produto.getPrecoVenda();
        custoTotal = produto.getCustoTotal();
        margemDeLucro = produto.getMargemDeLucro();


        if(produto.getComponenteProduto() != null){
            componenteProdutoDtoSet = new HashSet<>();
            for (ComponenteProduto i : produto.getComponenteProduto()){
                ComponenteProdutoDto componenteProdutoDto = new ComponenteProdutoDto(i);
                componenteProdutoDtoSet.add(componenteProdutoDto);
            }
        }
    }

    public ProductDto(){
    }


}
