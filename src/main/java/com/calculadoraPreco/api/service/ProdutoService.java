package com.calculadoraPreco.api.service;

import com.calculadoraPreco.api.domain.ComponenteProduto;
import com.calculadoraPreco.api.domain.Insumo;
import com.calculadoraPreco.api.domain.Produto;
import com.calculadoraPreco.api.domain.Usuario;
import com.calculadoraPreco.api.dto.AdicionarInsumoDto;
import com.calculadoraPreco.api.dto.ComponenteProdutoDto;
import com.calculadoraPreco.api.dto.ProdutoDto;
import com.calculadoraPreco.api.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProdutoService {


    private final ProdutoRepository produtoRepository;

    private final InsumoService insumoService;

    public Produto create(ProdutoDto produtoDto, Usuario usuario){

        if (produtoDto.getNome() == null || produtoDto.getNome().trim().isEmpty()) {
            produtoDto.setNome("nome");
        }
        if (produtoDto.getMargemDeLucro() == null) {
            produtoDto.setMargemDeLucro(BigDecimal.ZERO);
        }

        Produto produto = new Produto();
        produto.setNome(produtoDto.getNome());
        produto.setMargemDeLucro(produtoDto.getMargemDeLucro());
        produto.setUsuario(usuario);

        return produtoRepository.save(produto);
    }

    public Produto findById(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado!"));
    }

    public List<Produto> findAll(Usuario usuario){
        return produtoRepository.findAllByUsuarioId(usuario.getId());
    }

    public void delete(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new EntityNotFoundException("Produto com ID " + id + " não encontrado.");
        }

        produtoRepository.deleteById(id);
    }


    public Produto update(ProdutoDto produtoDto) {

        Produto produto = findById(produtoDto.getId());
        produto.setNome(produtoDto.getNome());
        produto.setMargemDeLucro(produtoDto.getMargemDeLucro());

        calcularPreco(produto);

        return produto;
    }

    public void relacionarInsumos(Long id, AdicionarInsumoDto adicionarInsumoDto) {
        Produto produto = findById(id);
        Insumo insumo = insumoService.findById(adicionarInsumoDto.getInsumoId());

        ComponenteProduto componenteProduto = new ComponenteProduto();
        componenteProduto.setQuantidade(adicionarInsumoDto.getQuantidade());
        componenteProduto.setInsumo(insumo);

        produto.getComponenteProduto().add(componenteProduto);

        calcularPreco(produto);
        produtoRepository.save(produto);
    }

    private BigDecimal calculaCustoTotal(Produto produto) {
        BigDecimal custoTotal = BigDecimal.ZERO;
        if (!(produto.getComponenteProduto() == null || produto.getComponenteProduto().isEmpty())){
            for (ComponenteProduto i : produto.getComponenteProduto()) {
                custoTotal = custoTotal.add(i.getInsumo().getCustoUn().multiply(i.getQuantidade()));
                if (custoTotal.compareTo(BigDecimal.ZERO) < 0) {
                    custoTotal = BigDecimal.ZERO;
                }
            }
        }

        return custoTotal;
    }


    public void calcularPreco(Produto produto) {
        BigDecimal custoTotal = calculaCustoTotal(produto);
        produto.setCustoTotal(custoTotal);

        // Calcula a margem de lucro com arredondamento
        BigDecimal margemLucroPercentual = produto.getMargemDeLucro()
                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP); // 2 casas decimais, arredondamento padrão

        BigDecimal precoVenda = BigDecimal.ONE
                .add(margemLucroPercentual)
                .multiply(custoTotal);

        produto.setPrecoVenda(precoVenda);
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

    public Set<ComponenteProduto> listAllComponente(Long idProduto) {
        Produto produto = findById(idProduto);

        return produto.getComponenteProduto();
    }
}
