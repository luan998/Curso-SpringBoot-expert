package org.luan.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CLIENTE")
public class Cliente {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "NOME")
  @NotEmpty(message = "{campo.nome.obrigatorio}")
  private String nome;

  @Column(name = "CPF", length = 11)
  @NotEmpty(message = "{campo.cpf.obrigatorio}")
  @CPF(message = "{campo.cpf.invalido}")
  private String cpf;

  @JsonIgnore
  @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
  private Set<Pedido> pedidos;

  public Cliente(String nome) {
    this.nome = nome;
  }

  public Cliente(Integer id, String nome) {
    this.id = id;
    this.nome = nome;
  }
}
