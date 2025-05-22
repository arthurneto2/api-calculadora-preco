package com.arthur.api.service;

import com.arthur.api.domain.ComponenteProduto;
import com.arthur.api.domain.Insumo;
import com.arthur.api.domain.Produto;
import com.arthur.api.domain.Usuario;
import com.arthur.api.dto.AdicionarInsumoDto;
import com.arthur.api.dto.ComponenteProdutoDto;
import com.arthur.api.dto.ProductDto;
import com.arthur.api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {


    private final ProdutoRepository produtoRepository;

    private final InsumoService insumoService;


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

    public void relacionarInsumos(Long id, AdicionarInsumoDto adicionarInsumoDto) {
        Produto produto = findById(id);
        Insumo insumo = insumoService.findById(adicionarInsumoDto.getInsumoId());

        ComponenteProduto componenteProduto = new ComponenteProduto();
        componenteProduto.setQuantidade(adicionarInsumoDto.getQuantidade());
        componenteProduto.setInsumo(insumo);

        produto.getComponenteProduto().add(componenteProduto);
        produtoRepository.save(produto);


    }

    public BigDecimal calculaCustoTotal(Produto produto) {
        BigDecimal custoTotal = BigDecimal.ZERO;
        for (ComponenteProduto i : produto.getComponenteProduto()) {
            custoTotal = custoTotal.add(i.getInsumo().getCustoUn().multiply(i.getQuantidade()));
            if (custoTotal.compareTo(BigDecimal.ZERO) < 0) {
                custoTotal = BigDecimal.ZERO;
            }
        }
        return custoTotal;
    }


    public Produto calcularPreco(Long id) {
        Produto produto = findById(id);
        BigDecimal custoTotal = calculaCustoTotal(produto);
        produto.setCustoTotal(custoTotal);
        produto.setPrecoVenda(produto.getMargemDeLucro().multiply(custoTotal));


        produtoRepository.save(produto);
        return produto;
    }

    public void updateQuantComponente(ComponenteProdutoDto request, Long id) {
        Produto produto = findById(id);
        for (ComponenteProduto i : produto.getComponenteProduto()) {
            if (i.getInsumo().getId().equals(request.getInsumoDto().getId())) {
                i.setQuantidade(request.getQuantidade());
                break;
            }
        }
        produto = calcularPreco(produto.getId());

        produtoRepository.save(produto);
    }

    public void deleteComponente(Long id, ComponenteProdutoDto request) {
        Produto produto = findById(id);
        for (ComponenteProduto i : produto.getComponenteProduto()) {
            if (i.getInsumo().getId().equals(request.getInsumoDto().getId())) {
                produto.getComponenteProduto().remove(i);
                break;
            }
        }

        produto = calcularPreco(produto.getId());

        produtoRepository.save(produto);
    }
}
