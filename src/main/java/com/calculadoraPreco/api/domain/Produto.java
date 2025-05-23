package com.calculadoraPreco.api.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private BigDecimal precoVenda;

    private BigDecimal custoTotal;

    private BigDecimal margemDeLucro;

    @ManyToOne
    private Usuario usuario;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ComponenteProduto> componenteProduto;

}
