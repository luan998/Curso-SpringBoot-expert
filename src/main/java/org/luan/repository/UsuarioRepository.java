package org.luan.repository;

import java.util.Optional;
import org.luan.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

  Optional<Usuario> findByLogin(String login);
}
