package com.calculadoraPreco.api.service;

import com.calculadoraPreco.api.domain.ComponenteProduto;
import com.calculadoraPreco.api.domain.Insumo;
import com.calculadoraPreco.api.domain.Produto;
import com.calculadoraPreco.api.domain.Usuario;
import com.calculadoraPreco.api.dto.ProdutoDto;
import com.calculadoraPreco.api.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

    private static Usuario usuario;
    private ProdutoDto produtoDto;
    private Produto produtoSalvo;
    private  Insumo insumo;
    private  ComponenteProduto componenteProduto;
    private Set<ComponenteProduto> componentes;


    @BeforeAll
    static void setUpAll(){
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setName("Arthur");
        usuario.setEmail("<EMAIL>");
        usuario.setPassword("<PASSWORD>");
    }

    @BeforeEach
    void setUpEach(){
        //Arrange
        produtoDto = new ProdutoDto();
        produtoDto.setNome("Notebook");
        produtoDto.setMargemDeLucro(BigDecimal.valueOf(30));

        produtoSalvo = new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome("Notebook");
        produtoSalvo.setMargemDeLucro(BigDecimal.valueOf(30));
        produtoSalvo.setUsuario(usuario);

        insumo = new Insumo();
        insumo.setId(1L);
        insumo.setNome("CPU");
        insumo.setUnMedida("Unidade");

        componenteProduto = new ComponenteProduto();
        componenteProduto.setId(1L);
        componenteProduto.setQuantidade(BigDecimal.valueOf(1));

        componentes = new HashSet<>();

    }


    @Test
    void create_DeveSalvarProdutoComSucesso() {
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
    void create_DeveSalvarProdutoComValorPadraoCasoCamposEstejamVazios() {
        // Arrange
        // Sem nome e margem
        produtoDto.setNome(null);
        produtoDto.setMargemDeLucro(null);

        produtoSalvo.setId(1L);
        produtoSalvo.setNome("nome");
        produtoSalvo.setMargemDeLucro(BigDecimal.ZERO);

        // Act

        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoSalvo);

        Produto resultado = produtoService.create(produtoDto, usuario);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("nome", resultado.getNome());
        assertEquals(BigDecimal.ZERO, resultado.getMargemDeLucro());
        assertEquals(usuario, resultado.getUsuario());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void findById_DeveRetornarProdutoQuandoExistir() {
        // Arrange
        Long id = 1L;


        when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoSalvo));

        // Act
        Produto resultado = produtoService.findById(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Notebook", resultado.getNome());
        verify(produtoRepository, times(1)).findById(any(Long.class));
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
        produtoDto.setId(1L);
        produtoDto.setNome("Notebook Atualizado");
        produtoDto.setCustoTotal(BigDecimal.valueOf(0));
        produtoDto.setPrecoVenda(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        produtoDto.setMargemDeLucro(BigDecimal.valueOf(25));

        when(produtoRepository.findById(produtoDto.getId())).thenReturn(Optional.of(produtoSalvo));
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
    void update_DeveAtualizarProdutoComSucessoComListaDeCompoenetesPovoadaComCustoTotalMaiorQueZero() {
        // Arrange

        produtoDto.setId(1L);
        produtoDto.setNome("Notebook Atualizado");
        produtoDto.setMargemDeLucro(BigDecimal.valueOf(100));

        insumo.setCustoUn(BigDecimal.valueOf(100));
        componenteProduto.setInsumo(insumo);
        componentes.add(componenteProduto);
        produtoSalvo.setComponenteProduto(componentes);

        when(produtoRepository.findById(produtoDto.getId())).thenReturn(Optional.of(produtoSalvo));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Produto resultado = produtoService.update(produtoDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(produtoDto.getId(), resultado.getId());
        assertEquals(produtoDto.getNome(), resultado.getNome());
        assertEquals(BigDecimal.valueOf(100), resultado.getCustoTotal());
        assertEquals(new BigDecimal(200).setScale(2, RoundingMode.HALF_UP), resultado.getPrecoVenda());
        assertEquals(produtoDto.getMargemDeLucro(), resultado.getMargemDeLucro());
        verify(produtoRepository, times(1)).findById(produtoDto.getId());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void update_DeveAtualizarProdutoComSucessoComListaDeCompoenetesPovoadaComCustoTotalMenorQueZero() {
        // Arrange

        produtoDto.setId(1L);
        produtoDto.setNome("Notebook Atualizado");
        produtoDto.setMargemDeLucro(BigDecimal.valueOf(100));

        insumo.setCustoUn(BigDecimal.valueOf(-100));
        componenteProduto.setInsumo(insumo);
        componentes.add(componenteProduto);
        produtoSalvo.setComponenteProduto(componentes);

        when(produtoRepository.findById(produtoDto.getId())).thenReturn(Optional.of(produtoSalvo));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Produto resultado = produtoService.update(produtoDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(produtoDto.getId(), resultado.getId());
        assertEquals(produtoDto.getNome(), resultado.getNome());
        assertEquals(BigDecimal.ZERO, resultado.getCustoTotal());
        assertEquals(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP), resultado.getPrecoVenda());
        assertEquals(produtoDto.getMargemDeLucro(), resultado.getMargemDeLucro());
        verify(produtoRepository, times(1)).findById(produtoDto.getId());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }


    @Test
    void update_DeveLancarExcecaoQuandoProdutoNaoExistir() {
        // Arrange
        produtoDto.setId(999L);

        when(produtoRepository.findById(produtoDto.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            produtoService.update(produtoDto);
        }, "Produto não encontrado!");

        verify(produtoRepository, never()).save(any(Produto.class));
    }
}