package com.calculadoraPreco.api.repository;

import com.calculadoraPreco.api.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findAllByUsuarioId(Long id);
}
