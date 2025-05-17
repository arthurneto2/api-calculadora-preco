package com.arthur.api.service;

import com.arthur.api.domain.Produto;
import com.arthur.api.domain.Usuario;
import com.arthur.api.dto.ProductDto;
import com.arthur.api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProdutoRepository produtoRepository;

    public Produto create(ProductDto productDto, Usuario usuario){

        Produto produto = new Produto();
        produto.setNome(productDto.getNome());
        produto.setCustoTotal(productDto.getCustoTotal());
        produto.setPrecoVenda(productDto.getPrecoVenda());
        produto.setMargemDeLucro(productDto.getMargemDeLucro());
        produto.setUsuario(usuario);

        return produtoRepository.save(produto);
    }


    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    public Produto findById(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado!"));
    }


    public void delete(Long id) {
        produtoRepository.deleteById(id);
    }

    public Produto update(ProductDto productDto) {

        Produto produto = findById(productDto.getId());
        produto.setNome(productDto.getNome());
        produto.setCustoTotal(productDto.getCustoTotal());
        produto.setPrecoVenda(productDto.getPrecoVenda());
        produto.setMargemDeLucro(productDto.getMargemDeLucro());

        return produtoRepository.save(produto);
    }
}
