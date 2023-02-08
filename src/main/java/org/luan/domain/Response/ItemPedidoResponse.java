package org.luan.domain.Response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedidoResponse {

  private String descricaoProduto;

  private BigDecimal precoUnitario;

  private Integer quantidade;

}
