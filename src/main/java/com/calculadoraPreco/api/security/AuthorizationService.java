package com.calculadoraPreco.api.security;

import com.calculadoraPreco.api.domain.Usuario;
import com.calculadoraPreco.api.dto.UserDTO;
import com.calculadoraPreco.api.repository.UsuarioRepository;
import com.calculadoraPreco.api.security.dto.AuthorizationDTO;
import com.calculadoraPreco.api.security.dto.TokenInfo;
import com.calculadoraPreco.api.security.jwt.JwtAuthenticationService;
import com.calculadoraPreco.api.util.DateTimeFormarter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final JwtAuthenticationService jwtAuthenticationService;


    @Override
    public Usuario loadUserByUsername(String login) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(login).orElseThrow( () -> new UsernameNotFoundException("Não foi possível encontrar o usuario com login: " + login));
    }

    @PostConstruct
    private void init(){
        Usuario user = usuarioRepository.findByEmail("admin@labprog.com").orElse(null);
        if(user == null){
            Usuario admin = new Usuario();
            admin.setName("Arthur");
            admin.setEmail("admin@labprog.com");
            admin.setPassword(PasswordEncoderService.encode("admin123"));
            usuarioRepository.save(admin);
        }

    }

    public AuthorizationDTO generateAuthorization(Authentication authentication){
        Usuario user = (Usuario) authentication.getPrincipal();
        TokenInfo tokenInfo = jwtAuthenticationService.generateToken(user);
        return new AuthorizationDTO(
                new UserDTO(user),
                tokenInfo.token(),
                DateTimeFormarter.toIso8601(tokenInfo.expirationDate()),
                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );
    }

    public UsernamePasswordAuthenticationToken authenticate(UserDetails userDetails) {
        // Cria o token de autenticação sem usar o hash da senha
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        null, // Ignoramos a senha no caso de autenticação programática
                        userDetails.getAuthorities()
                );

        // Define o token no contexto de segurança do Spring
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // Retorna o token de autenticação gerado
        return authenticationToken;
    }

}