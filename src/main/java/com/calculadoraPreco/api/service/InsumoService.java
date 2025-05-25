package com.calculadoraPreco.api.service;

import com.calculadoraPreco.api.domain.Insumo;
import com.calculadoraPreco.api.domain.Usuario;
import com.calculadoraPreco.api.dto.InsumoDto;
import com.calculadoraPreco.api.repository.InsumoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsumoService {

    private final InsumoRepository insumoRepository;

    public Insumo create(InsumoDto requestDto, Usuario usuario){
        Insumo novoInsumo = new Insumo();
        novoInsumo.setNome(requestDto.getNome());
        novoInsumo.setUnMedida(requestDto.getUnMedida());
        novoInsumo.setCustoUn(requestDto.getCustoUn());
        novoInsumo.setUsuario(usuario);


        return insumoRepository.save(novoInsumo);
    };

    public List<Insumo> findAll(Usuario usuario){
        return insumoRepository.findAllByUsuarioId(usuario.getId());
    }

    public Insumo findById(Long id){
        return insumoRepository.findById(id).orElseThrow(() -> new RuntimeException("Insumo não encontrado!"));
    }

    public void delete(Long id){
        insumoRepository.deleteById(id);
    }

    public Insumo update(InsumoDto requestDto){
        Insumo insumo = findById(requestDto.getId());
        insumo.setNome(requestDto.getNome());
        insumo.setUnMedida(requestDto.getUnMedida());
        insumo.setCustoUn(requestDto.getCustoUn());

        return insumoRepository.save(insumo);
    }

    public BigDecimal custoInsumo(Long id, BigDecimal quantidade){
        Insumo insumo = findById(id);

        return insumo.getCustoUn().multiply(quantidade);
    }

}
