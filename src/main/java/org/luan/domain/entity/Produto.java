package org.luan.domain.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRODUTO")
@Data //Equivale a Getter, Setter, HashEquals e ToString do Lombok
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "DESCRICAO")
  @NotEmpty(message = "{campo.descricao.obrigatorio}")
  private String descricao;

  @Column(name = "PRECO_UNITARIO")
  @NotNull(message = "{campo.preco.obrigatorio}")
  private BigDecimal preco;

}
