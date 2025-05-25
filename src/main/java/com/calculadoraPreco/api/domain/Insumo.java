package com.calculadoraPreco.api.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class Insumo {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    private String unMedida;

    private BigDecimal custoUn;

    @ManyToOne
    private Usuario usuario;

}
