package org.luan.service;

import java.util.Optional;
import org.luan.domain.dto.PedidoDto;
import org.luan.domain.entity.Pedido;
import org.luan.domain.enums.StatusPedido;

public interface PedidoService {

  Pedido salvar(PedidoDto dto);

  Optional<Pedido> obterPedidoCompleto(Integer id);

  void atualizaStatus(Integer id, StatusPedido statusPedido);
}
