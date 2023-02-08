package org.luan.repository;

import java.util.List;
import org.luan.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientesRepository extends JpaRepository<Cliente, Integer> {

  List<Cliente> findByNomeContaining(String nome);

  //Usando JPQL
  @Query(value = " select c from Cliente c where c.nome like :nome")
  List<Cliente> findByNomeLikeUsingQuery(@Param("nome") String nome);

  //Utilizando query com sql nativo
  @Query(value = " select * from cliente c where c.nome like '%:nome%' ", nativeQuery = true)
  List<Cliente> findByNomeLikeUsingSqlQuery(@Param("nome") String nome);

  //Update ou delete tem que usar o @Modifying para falar que está atualizando a tabela
  @Modifying
  void deleteByNome(String nome);

  boolean existsByNome(String nome);

  //Left join trás os clientes tendo pedidos ou não
  @Query(" select c from Cliente c left join fetch c.pedidos p where c.id =:id ")
  Cliente findClienteFetchPedidos( @Param("id") Integer id );

}
