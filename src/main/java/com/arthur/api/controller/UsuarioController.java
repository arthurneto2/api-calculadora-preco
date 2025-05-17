package com.arthur.api.controller;

import com.arthur.api.dto.LoginRequestDto;
import com.arthur.api.dto.RegisterRequestDto;
import com.arthur.api.repository.UsuarioRepository;
import com.arthur.api.security.AuthorizationService;
import com.arthur.api.security.dto.AuthorizationDTO;
import com.arthur.api.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UsuarioController {

    private final AuthenticationManager authenticationManager;

    private final AuthorizationService authorizationService;

    private final UsuarioRepository usuarioRepository;

    private final UsuarioService usuarioService;


    @PostMapping("/login")
    public ResponseEntity<AuthorizationDTO> auth(@RequestBody @Valid LoginRequestDto credentials){
        var usernamePassword = new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(usernamePassword);
        AuthorizationDTO responseBody = authorizationService.generateAuthorization(authentication);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDto dto) {

        // Verifica se o e-mail já está em uso
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail já está em uso!");
        }
        usuarioService.createUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
    }

    @GetMapping("/testToken")
    public ResponseEntity<String> test(){
        return new ResponseEntity<>("Você está autenticado!", HttpStatus.OK);
    }

}

