package org.luan.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.luan.service.impl.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthFilter extends OncePerRequestFilter {

  private JwtService jwtService;

  private UsuarioServiceImpl usuarioService;

  public JwtAuthFilter( JwtService jwtService, UsuarioServiceImpl usuarioService ) {
    this.jwtService = jwtService;
    this.usuarioService = usuarioService;
  }

  /*Estou interceptando uma requisição, obtendo as informações e adicionando um
   usuário autenticado caso o token esteja válido, dentro da sessão */

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, FilterChain filterChain)
      throws ServletException, IOException {

    String authorization = httpServletRequest.getHeader("Authorization");

    if(authorization != null && authorization.startsWith("Bearer")) {
      //Pega toda a string corta depois do espaço e pega o que vem depois
      String token = authorization.split(" ")[1];
      boolean isValid = jwtService.tokenValidation(token);

      if(isValid){
        String loginUser = jwtService.getUserLogin(token);
        UserDetails usuario = usuarioService.loadUserByUsername(loginUser);
        UsernamePasswordAuthenticationToken user = new
            UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

        //Precisa fazer isso para dizer que a autenticação é uma autenticação web
        user.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

        SecurityContextHolder.getContext().setAuthentication(user);
      }
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
