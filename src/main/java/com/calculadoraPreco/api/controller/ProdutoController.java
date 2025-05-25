package com.calculadoraPreco.api.controller;

import com.calculadoraPreco.api.domain.Produto;
import com.calculadoraPreco.api.domain.Usuario;
import com.calculadoraPreco.api.dto.AdicionarInsumoDto;
import com.calculadoraPreco.api.dto.ComponenteProdutoDto;
import com.calculadoraPreco.api.dto.ProdutoDto;
import com.calculadoraPreco.api.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ProdutoDto> creatProduto(
            @RequestBody ProdutoDto request,
            @AuthenticationPrincipal Usuario usuario
    ){

        Produto produto = produtoService.create(request, usuario);
        ProdutoDto response = new ProdutoDto(produto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/list-all")
    public ResponseEntity<List<ProdutoDto>> findAll(){
        return ResponseEntity.ok(produtoService.findAll().stream().map(ProdutoDto::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(new ProdutoDto(produtoService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        produtoService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/update-produto")
    public ResponseEntity<ProdutoDto> updateProduto(@RequestBody ProdutoDto request){
        ProdutoDto response = new ProdutoDto(produtoService.update(request));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/componente")
    public ResponseEntity<Void> adicionarInsumos(
            @PathVariable Long id,
            @RequestBody AdicionarInsumoDto adicionarInsumoDto
    ) {
        produtoService.relacionarInsumos(id, adicionarInsumoDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/{id}/list-all/componente")
    public ResponseEntity<List<ComponenteProdutoDto>> findByIdComponente(@PathVariable Long id){
        return ResponseEntity.ok(produtoService.listAllComponente(id).stream().map(ComponenteProdutoDto::new).toList());
    }

    @PutMapping("/{id}/update-componente")
    public ResponseEntity<Void> updateQuantComponente(
            @RequestBody ComponenteProdutoDto request,
            @PathVariable Long id
    ){
        produtoService.updateQuantComponente(request, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/componente")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            @RequestBody ComponenteProdutoDto request
    ){
        produtoService.deleteComponente(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
