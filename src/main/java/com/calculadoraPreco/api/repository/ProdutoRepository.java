package com.calculadoraPreco.api.repository;

import com.calculadoraPreco.api.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {


}
