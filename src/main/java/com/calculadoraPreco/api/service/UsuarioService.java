package com.calculadoraPreco.api.service;

import com.calculadoraPreco.api.domain.Usuario;
import com.calculadoraPreco.api.dto.RegisterRequestDto;
import com.calculadoraPreco.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    public void createUsuario(RegisterRequestDto dto){
        Usuario newUser = new Usuario();
        newUser.setName(dto.getName());
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuarioRepository.save(newUser);
    }

}
