package org.luan.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.luan.domain.dto.CredentialsDto;
import org.luan.domain.dto.TokenDto;
import org.luan.domain.entity.Usuario;
import org.luan.exception.SenhaInvalidaException;
import org.luan.security.JwtService;
import org.luan.service.impl.UsuarioServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

  private final UsuarioServiceImpl usuarioService;

  private final PasswordEncoder passwordEncoder;

  private final JwtService jwtService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  private Usuario salvar(@RequestBody @Valid Usuario usuario){
    String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
    usuario.setSenha(senhaCriptografada);
    return usuarioService.salvar(usuario);
  }

  @PostMapping("/auth")
  private TokenDto authentication(@RequestBody CredentialsDto credentials) {
    try {
      Usuario user = Usuario
          .builder()
          .login(credentials.getLogin())
          .senha(credentials.getSenha())
          .build();

      UserDetails authenticatedUser = usuarioService.authenticate(user);
      String token = jwtService.generateToken(user);

      return new TokenDto(user.getLogin(), token);
    } catch (UsernameNotFoundException | SenhaInvalidaException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
    }
  }
}
