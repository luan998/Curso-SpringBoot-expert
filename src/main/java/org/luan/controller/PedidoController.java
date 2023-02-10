package org.luan.controller;

import io.swagger.annotations.Api;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.luan.domain.Response.ItemPedidoResponse;
import org.luan.domain.Response.PedidoInfoResponse;
import org.luan.domain.dto.PedidoDto;
import org.luan.domain.entity.ItemPedido;
import org.luan.domain.entity.Pedido;
import org.luan.domain.enums.StatusPedido;
import org.luan.domain.request.UpdateStatusPedidoRequest;
import org.luan.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/pedidos")
@Api("Api Pedidos")
public class PedidoController {

  @Autowired
  private PedidoService pedidoService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Integer save(@RequestBody @Valid PedidoDto dto){
    Pedido pedido = pedidoService.salvar(dto);
    return pedido.getId();
  }

  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateStatus( @PathVariable Integer id,
                            @RequestBody UpdateStatusPedidoRequest request) {
    String novoStatus = request.getNovoStatus();
    pedidoService.atualizaStatus(id, StatusPedido.valueOf(novoStatus));
  }

  @GetMapping("/{id}")
  public PedidoInfoResponse getById(@PathVariable Integer id){
    return pedidoService.obterPedidoCompleto(id)
        .map(p -> converter(p))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
  }

  private PedidoInfoResponse converter(Pedido pedido){
    return PedidoInfoResponse
        .builder()
        .codigo(pedido.getId())
        .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        .cpf(pedido.getCliente().getCpf())
        .nomeCliente(pedido.getCliente().getNome())
        .total(pedido.getTotal())
        .status(pedido.getStatus().name())
        .items(converterItem(pedido.getItens()))
        .build();
  }

  private List<ItemPedidoResponse> converterItem(List<ItemPedido> itens){
    if(CollectionUtils.isEmpty(itens)){
      return Collections.emptyList();
    }

    return itens.stream().map(
        item -> ItemPedidoResponse
            .builder().descricaoProduto(item.getProduto().getDescricao())
            .precoUnitario(item.getProduto().getPreco())
            .quantidade(item.getQuantidade())
            .build()
    ).collect(Collectors.toList());
  }
}
