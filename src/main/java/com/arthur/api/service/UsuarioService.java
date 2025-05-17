package com.arthur.api.service;

import com.arthur.api.domain.Usuario;
import com.arthur.api.dto.RegisterRequestDto;
import com.arthur.api.repository.UsuarioRepository;
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
