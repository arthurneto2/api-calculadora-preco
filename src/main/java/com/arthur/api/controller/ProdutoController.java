package com.arthur.api.controller;


import com.arthur.api.domain.Produto;
import com.arthur.api.domain.Usuario;
import com.arthur.api.dto.AdicionarIngredienteDto;
import com.arthur.api.dto.ProductDto;
import com.arthur.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProdutoController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> creatProduto(@RequestBody ProductDto request, @AuthenticationPrincipal Usuario usuario){

        Produto produto = productService.create(request, usuario);
        ProductDto response = new ProductDto(produto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<ProductDto>> findAll(){
        return ResponseEntity.ok(productService.findAll().stream().map(ProductDto::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(new ProductDto(productService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping
    public ResponseEntity<ProductDto> updateProduto(@RequestBody ProductDto request){

        Produto produto = productService.update(request);
        ProductDto response = new ProductDto(produto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/ingredientes")
    public ResponseEntity<Void> adicionarIngrediente(
            @PathVariable Long idProduto,
            @RequestBody AdicionarIngredienteDto adicionarIngredienteDto
    ) {
        productService.relacionarIngredientes(idProduto, adicionarIngredienteDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


    @GetMapping("/{id}/calcular-preco")
    public ResponseEntity<ProductDto> calcularPreco(@PathVariable Long id) {
        ProductDto response = productService.calcularPreco(id);
        return ResponseEntity.ok(response);
    }


}
