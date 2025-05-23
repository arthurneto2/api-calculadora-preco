package com.calculadoraPreco.api.repository;

import com.calculadoraPreco.api.domain.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {


}
