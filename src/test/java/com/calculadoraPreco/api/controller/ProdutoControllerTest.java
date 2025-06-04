package com.calculadoraPreco.api.controller;

import com.calculadoraPreco.api.domain.Produto;
import com.calculadoraPreco.api.domain.Usuario;
import com.calculadoraPreco.api.dto.ProdutoDto;
import com.calculadoraPreco.api.repository.ProdutoRepository;
import com.calculadoraPreco.api.repository.UsuarioRepository;
import com.calculadoraPreco.api.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    private ProdutoDto produtoDtoRequest1;

    private ProdutoDto produtoDtoRequest2;


    @BeforeEach
    void setUp() {

        usuario = new Usuario();
        usuario.setName("UsuarioTeste");
        usuario.setEmail("teste@email.com");
        usuario.setPassword("senha123");
        usuario = usuarioRepository.save(usuario);

        var auth = new UsernamePasswordAuthenticationToken(usuario, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        produtoDtoRequest1 = new ProdutoDto();
        produtoDtoRequest1.setNome("Notebook");
        produtoDtoRequest1.setMargemDeLucro(BigDecimal.valueOf(30));

        produtoDtoRequest2 = new ProdutoDto();
        produtoDtoRequest2.setNome("Smartphone");
        produtoDtoRequest2.setMargemDeLucro(BigDecimal.valueOf(25));

    }

    @AfterEach
    void tearDown() {
        produtoRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void shouldCreateAndRetrieveProduct() throws Exception {

        // Act & Assert - Testing the entire flow
        // Faz request para criar um produto
        String responseJson = mockMvc.perform(post("/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(produtoDtoRequest1)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Converte a resposta em DTO
        ProdutoDto createdProduct = new ObjectMapper()
                .readValue(responseJson, ProdutoDto.class);

        // Verifica que o produto foi salvo e associado ao usuário correto
        Produto produtoSalvo = produtoRepository.findById(createdProduct.getId()).orElseThrow();
        assertEquals("Notebook", produtoSalvo.getNome());
        assertEquals(new BigDecimal(30).setScale(2, RoundingMode.HALF_UP), produtoSalvo.getMargemDeLucro());
        assertEquals(usuario.getId(), produtoSalvo.getUsuario().getId());

        // Busca o produto pela API
        mockMvc.perform(get("/produto/" + createdProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Notebook"))
                .andExpect(jsonPath("$.margemDeLucro").value(30));
    }

    @Test
    void shouldRetrieveAllProductsForAuthenticatedUser() throws Exception {


        // Cria os produtos via API
        mockMvc.perform(post("/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(produtoDtoRequest1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(produtoDtoRequest2)))
                .andExpect(status().isOk());

        // Act & Assert
        mockMvc.perform(get("/produto/list-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // Verifica que retornou 2 produtos
                .andExpect(jsonPath("$[0].nome").value("Notebook"))
                .andExpect(jsonPath("$[1].nome").value("Smartphone"))
                .andExpect(jsonPath("$[0].margemDeLucro").value(30))
                .andExpect(jsonPath("$[1].margemDeLucro").value(25));

    }

    @Test
    void shouldRetrieveProductById() throws Exception {
        // Cria e salva o produto manualmente associado ao usuário
        Produto produto = new Produto();
        produto.setNome("Monitor");
        produto.setMargemDeLucro(BigDecimal.valueOf(20));
        produto.setUsuario(usuario); // usuário previamente salvo no @BeforeEach
        produto = produtoRepository.save(produto);

        // Realiza a requisição GET passando o ID do produto na URL
        mockMvc.perform(get("/produto/" + produto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(produto.getId()))
                .andExpect(jsonPath("$.nome").value("Monitor"))
                .andExpect(jsonPath("$.margemDeLucro").value(20));
    }

    @Test
    void shouldReturn404WhenProductNotFound() throws Exception {
        long invalidId = 9999L;

        mockMvc.perform(get("/produto/" + invalidId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").doesNotExist());
    }

    @Test
    void shouldFindAndDeleteProduct() throws Exception {
        Produto produto = new Produto();
        produto.setNome("Monitor");
        produto.setMargemDeLucro(BigDecimal.valueOf(20));
        produto.setUsuario(usuario); // usuário previamente salvo no @BeforeEach
        produto = produtoRepository.save(produto);


        mockMvc.perform(delete("/produto/" + produto.getId()))
                .andExpect(status().isNoContent());
    }
}