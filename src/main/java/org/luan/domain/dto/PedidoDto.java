package org.luan.domain.dto;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.luan.validation.NotEmptyList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDto {

  @NotNull(message = "{campo.codigo-cliente.obrigatorio}")
  private Integer cliente;

  @NotNull(message = "{campo.total-pedido.obrigatorio}")
  private BigDecimal total;

  @NotEmptyList(message = "{campo.items-pedido.obrigatorio}")
  private List<ItemPedidoDto> items;

}
