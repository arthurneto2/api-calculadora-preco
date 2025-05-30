package com.calculadoraPreco.api.service;

import com.calculadoraPreco.api.domain.ComponenteProduto;
import com.calculadoraPreco.api.domain.Insumo;
import com.calculadoraPreco.api.domain.Produto;
import com.calculadoraPreco.api.domain.Usuario;
import com.calculadoraPreco.api.dto.ProdutoDto;
import com.calculadoraPreco.api.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;


    @Test
    void create_DeveSalvarProdutoComSucesso() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setName("Arthur");
        usuario.setEmail("<EMAIL>");
        usuario.setPassword("<PASSWORD>");

        ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setNome("Notebook");
        produtoDto.setMargemDeLucro(BigDecimal.valueOf(30));

        Produto produtoSalvo = new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Notebook");
        produtoSalvo.setMargemDeLucro(BigDecimal.valueOf(30));
        produtoSalvo.setUsuario(usuario);

        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoSalvo);

        // Act
        Produto resultado = produtoService.create(produtoDto, usuario);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Notebook", resultado.getNome());
        assertEquals(BigDecimal.valueOf(30), resultado.getMargemDeLucro());
        assertEquals(usuario, resultado.getUsuario());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void create_DeveLancarExcecaoQuandoDadosInvalidos() {
        // Arrange
        Usuario usuario = new Usuario();
        ProdutoDto produtoDto = new ProdutoDto(); // Sem nome e margem

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            produtoService.create(produtoDto, usuario);
        });
    }

    @Test
    void findById_DeveRetornarProdutoQuandoExistir() {
        // Arrange
        Long id = 1L;
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome("Notebook");

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));

        // Act
        Produto resultado = produtoService.findById(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Notebook", resultado.getNome());
    }

    @Test
    void findById_DeveLancarExcecaoQuandoNaoEncontrar() {
        // Arrange
        Long id = 999L;

        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            produtoService.findById(id);
        }, "Produto não encontrado!");
    }

    @Test
    void delete_DeveDeletarProdutoQuandoExistir() {
        // Arrange
        Long id = 1L;

        when(produtoRepository.existsById(id)).thenReturn(true);

        // Act
        produtoService.delete(id);

        // Assert
        verify(produtoRepository, times(1)).deleteById(id);
    }

    @Test
    void delete_DeveLancarExcecaoQuandoProdutoNaoExistir() {
        // Arrange
        Long id = 999L;

        when(produtoRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            produtoService.delete(id);
        }, "Produto com ID " + id + " não encontrado.");

        verify(produtoRepository, never()).deleteById(id);
    }

    @Test
    void update_DeveAtualizarProdutoComSucessoComListaDeCompoenetesVazia() {
        // Arrange
        ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setId(1L);
        produtoDto.setNome("Notebook Atualizado");
        produtoDto.setCustoTotal(BigDecimal.valueOf(0));
        produtoDto.setPrecoVenda(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        produtoDto.setMargemDeLucro(BigDecimal.valueOf(25));

        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);
        produtoExistente.setNome("Notebook");
        produtoExistente.setMargemDeLucro(BigDecimal.valueOf(30));

        when(produtoRepository.findById(produtoDto.getId())).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Produto resultado = produtoService.update(produtoDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(produtoDto.getId(), resultado.getId());
        assertEquals(produtoDto.getNome(), resultado.getNome());
        assertEquals(produtoDto.getCustoTotal(), resultado.getCustoTotal());
        assertEquals(produtoDto.getPrecoVenda(), resultado.getPrecoVenda());
        assertEquals(produtoDto.getMargemDeLucro(), resultado.getMargemDeLucro());
        verify(produtoRepository, times(1)).findById(produtoDto.getId());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    private static Produto getProduto(BigDecimal custoUn) {
        Insumo insumo = new Insumo();
        insumo.setId(1L);
        insumo.setNome("CPU");
        insumo.setUnMedida("Unidade");
        insumo.setCustoUn(custoUn);

        ComponenteProduto componenteProduto = new ComponenteProduto();
        componenteProduto.setId(1L);
        componenteProduto.setQuantidade(BigDecimal.valueOf(1));
        componenteProduto.setInsumo(insumo);

        Set<ComponenteProduto> componentes = new HashSet<>();
        componentes.add(componenteProduto);

        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);
        produtoExistente.setNome("Notebook");
        produtoExistente.setComponenteProduto(componentes);
        produtoExistente.setMargemDeLucro(BigDecimal.valueOf(100));
        return produtoExistente;
    }

    @Test
    void update_DeveAtualizarProdutoComSucessoComListaDeCompoenetesPovoadaComCustoTotalMaiorQueZero() {
        // Arrange
        ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setId(1L);
        produtoDto.setNome("Notebook Atualizado");
        produtoDto.setCustoTotal(BigDecimal.valueOf(100));
        produtoDto.setPrecoVenda(new BigDecimal(200).setScale(2, RoundingMode.HALF_UP));
        produtoDto.setMargemDeLucro(BigDecimal.valueOf(100));

        Produto produtoExistente = getProduto(BigDecimal.valueOf(100));


        when(produtoRepository.findById(produtoDto.getId())).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Produto resultado = produtoService.update(produtoDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(produtoDto.getId(), resultado.getId());
        assertEquals(produtoDto.getNome(), resultado.getNome());
        assertEquals(produtoDto.getCustoTotal(), resultado.getCustoTotal());
        assertEquals(produtoDto.getPrecoVenda(), resultado.getPrecoVenda());
        assertEquals(produtoDto.getMargemDeLucro(), resultado.getMargemDeLucro());
        verify(produtoRepository, times(1)).findById(produtoDto.getId());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void update_DeveAtualizarProdutoComSucessoComListaDeCompoenetesPovoadaComCustoTotalMenorQueZero() {
        // Arrange
        ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setId(1L);
        produtoDto.setNome("Notebook Atualizado");
        produtoDto.setCustoTotal(BigDecimal.valueOf(0));
        produtoDto.setPrecoVenda(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
        produtoDto.setMargemDeLucro(BigDecimal.valueOf(100));

        Produto produtoExistente = getProduto(BigDecimal.valueOf(-100));


        when(produtoRepository.findById(produtoDto.getId())).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Produto resultado = produtoService.update(produtoDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(produtoDto.getId(), resultado.getId());
        assertEquals(produtoDto.getNome(), resultado.getNome());
        assertEquals(produtoDto.getCustoTotal(), resultado.getCustoTotal());
        assertEquals(produtoDto.getPrecoVenda(), resultado.getPrecoVenda());
        assertEquals(produtoDto.getMargemDeLucro(), resultado.getMargemDeLucro());
        verify(produtoRepository, times(1)).findById(produtoDto.getId());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }


    @Test
    void update_DeveLancarExcecaoQuandoProdutoNaoExistir() {
        // Arrange
        ProdutoDto produtoDto = new ProdutoDto();
        produtoDto.setId(999L);

        when(produtoRepository.findById(produtoDto.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            produtoService.update(produtoDto);
        }, "Produto não encontrado!");

        verify(produtoRepository, never()).save(any(Produto.class));
    }
}