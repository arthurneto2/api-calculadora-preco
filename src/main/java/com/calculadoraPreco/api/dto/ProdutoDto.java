package com.calculadoraPreco.api.dto;

import com.calculadoraPreco.api.domain.ComponenteProduto;
import com.calculadoraPreco.api.domain.Produto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
public class ProdutoDto {

    private Long id;

    private String nome;

    private BigDecimal precoVenda;

    private BigDecimal custoTotal;

    private BigDecimal margemDeLucro;

    private Set<ComponenteProdutoDto> componenteProdutoDtoSet;

    public ProdutoDto(Produto produto){
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

    public ProdutoDto(){
    }


}
