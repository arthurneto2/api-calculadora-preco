package com.arthur.api.controller;


import com.arthur.api.domain.Insumo;
import com.arthur.api.dto.InsumoDto;
import com.arthur.api.service.InsumoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/insumo")
public class InsumoController {

    private final InsumoService insumoService;

    @PostMapping
    public ResponseEntity<InsumoDto> createInsumo(@RequestBody InsumoDto request){
        Insumo insumo = insumoService.create(request);
        InsumoDto response = new InsumoDto(insumo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<InsumoDto>> listAll(){
        return ResponseEntity.ok(insumoService.findAll().stream().map(InsumoDto::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsumoDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(new InsumoDto(insumoService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsumo(@PathVariable Long id){
        insumoService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping
    public ResponseEntity<InsumoDto> updateInsumo(@RequestBody InsumoDto request){
        Insumo insumo = insumoService.update(request);
        InsumoDto response = new InsumoDto(insumo);
        return ResponseEntity.ok(response);
    }




}
