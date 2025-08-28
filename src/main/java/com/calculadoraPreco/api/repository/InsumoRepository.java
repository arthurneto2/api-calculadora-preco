package com.calculadoraPreco.api.repository;

import com.calculadoraPreco.api.domain.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {
    List<Insumo> findAllByUsuarioId(Long id);


}
