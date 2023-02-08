package org.luan.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.luan.VendasApplication;
import org.luan.domain.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${security.jwt.expiracao}")
  private String expiracao;

  @Value("${security.jwt.chave-assinatura}")
  private String chaveAssinatura;

  public String generateToken (Usuario usuario){
    long expString = Long.valueOf(expiracao);
    LocalDateTime dateAndHourExpiration = LocalDateTime.now().plusMinutes(expString);
    Instant instant = dateAndHourExpiration.atZone(ZoneId.systemDefault()).toInstant();
    Date date = Date.from(instant);

    //Como gerar token JWT
    return Jwts
        .builder()
        .setSubject(usuario.getLogin())
        .setExpiration(date)
        .signWith(SignatureAlgorithm.HS512, chaveAssinatura)
        .compact();
  }

  public Claims getClaims( String token ) throws ExpiredJwtException {
    //parser decodifica o token, getbody retorna os claims do token
    return Jwts
        .parser()
        .setSigningKey(chaveAssinatura)
        .parseClaimsJws(token)
        .getBody();
  }

  public boolean tokenValidation(String token) {
    try{
      Claims claims = getClaims(token);
      Date dateExpiration = claims.getExpiration();
      LocalDateTime date = dateExpiration.toInstant()
          .atZone(ZoneId.systemDefault()).toLocalDateTime();
      return !LocalDateTime.now().isAfter(date);
    } catch (Exception e){
      return false;
    }
  }

  public String getUserLogin(String token) throws ExpiredJwtException {
    return (String) getClaims(token).getSubject();
  }

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(VendasApplication.class);
    JwtService service = context.getBean(JwtService.class);
    Usuario usuario = Usuario.builder().login("fulano").build();
    String token = service.generateToken(usuario);
    System.out.println(token);

    boolean isTokenValid =  service.tokenValidation(token);
    System.out.println("O token está válido?" + isTokenValid);

    System.out.println(service.getUserLogin(token));
  }
}
