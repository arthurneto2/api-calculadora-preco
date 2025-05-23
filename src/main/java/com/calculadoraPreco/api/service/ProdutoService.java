package com.calculadoraPreco.api.service;

import com.calculadoraPreco.api.domain.ComponenteProduto;
import com.calculadoraPreco.api.domain.Insumo;
import com.calculadoraPreco.api.domain.Produto;
import com.calculadoraPreco.api.domain.Usuario;
import com.calculadoraPreco.api.dto.AdicionarInsumoDto;
import com.calculadoraPreco.api.dto.ComponenteProdutoDto;
import com.calculadoraPreco.api.dto.ProdutoDto;
import com.calculadoraPreco.api.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {


    private final ProdutoRepository produtoRepository;

    private final InsumoService insumoService;


    public Produto create(ProdutoDto produtoDto, Usuario usuario){

        Produto produto = new Produto();
        produto.setNome(produtoDto.getNome());
        produto.setMargemDeLucro(produtoDto.getMargemDeLucro());
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

    public Produto update(ProdutoDto produtoDto) {

        Produto produto = findById(produtoDto.getId());
        produto.setNome(produtoDto.getNome());
        produto.setCustoTotal(produtoDto.getCustoTotal());
        produto.setPrecoVenda(produtoDto.getPrecoVenda());
        produto.setMargemDeLucro(produtoDto.getMargemDeLucro());

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


    public void calcularPreco(Produto produto) {
        BigDecimal custoTotal = calculaCustoTotal(produto);
        produto.setCustoTotal(custoTotal);
        produto.setPrecoVenda(produto.getMargemDeLucro().multiply(custoTotal));


        produtoRepository.save(produto);
    }

    public void updateQuantComponente(ComponenteProdutoDto request, Long id) {
        Produto produto = findById(id);
        for (ComponenteProduto i : produto.getComponenteProduto()) {
            if (i.getInsumo().getId().equals(request.getInsumoDto().getId())) {
                i.setQuantidade(request.getQuantidade());
                break;
            }
        }
        calcularPreco(produto);
    }

    public void deleteComponente(Long id, ComponenteProdutoDto request) {
        Produto produto = findById(id);
        for (ComponenteProduto i : produto.getComponenteProduto()) {
            if (i.getInsumo().getId().equals(request.getInsumoDto().getId())) {
                produto.getComponenteProduto().remove(i);
                break;
            }
        }
        calcularPreco(produto);
    }
}
