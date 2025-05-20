package com.arthur.api.service;

import com.arthur.api.domain.Insumo;
import com.arthur.api.domain.Produto;
import com.arthur.api.domain.Usuario;
import com.arthur.api.dto.ProductDto;
import com.arthur.api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {


    private final ProdutoRepository produtoRepository;


    public Produto create(ProductDto productDto, Usuario usuario){

        Produto produto = new Produto();
        produto.setNome(productDto.getNome());
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

    public void relacionarIngredientes(Long id, Set<Insumo> insumos) {
        Produto produto = findById(id);

        produto.setInsumo(insumos);
        produtoRepository.save(produto);


    }

    public BigDecimal calculaCustoTotal(Produto produto, BigDecimal quantidade) {
        BigDecimal custoTotal = BigDecimal.ZERO;
        for (Insumo i : produto.getInsumo()) {
            custoTotal = custoTotal.add(i.getCustoUn().multiply(quantidade));
            if (custoTotal.compareTo(BigDecimal.ZERO) < 0) {
                custoTotal = BigDecimal.ZERO;
            }
            produto.setCustoTotal(custoTotal);
            produtoRepository.save(produto);
        }
        return custoTotal;
    }

    public BigDecimal calculaPrecoVenda(Produto produto) {
        BigDecimal precoVenda = BigDecimal.ZERO;

        precoVenda = precoVenda.add(produto.getMargemDeLucro().multiply(produto.getCustoTotal()));

        return precoVenda;
    }
    public ProductDto calcularPreco(Long id, BigDecimal quantidade) {
        Produto produto = findById(id);

        produto.setCustoTotal(calculaCustoTotal(produto, quantidade));
        produto.setPrecoVenda(calculaPrecoVenda(produto));


        produtoRepository.save(produto);
        return new ProductDto(produto);
    }
}
