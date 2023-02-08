package org.luan.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.luan.domain.dto.ItemPedidoDto;
import org.luan.domain.dto.PedidoDto;
import org.luan.domain.entity.Cliente;
import org.luan.domain.entity.ItemPedido;
import org.luan.domain.entity.Pedido;
import org.luan.domain.entity.Produto;
import org.luan.domain.enums.StatusPedido;
import org.luan.exception.PedidoNotFoundException;
import org.luan.exception.RegraNegocioException;
import org.luan.repository.ClientesRepository;
import org.luan.repository.ItemPedidoRepository;
import org.luan.repository.PedidoRepository;
import org.luan.repository.ProdutoRepository;
import org.luan.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoServiceImpl implements PedidoService {

  @Autowired
  private PedidoRepository pedidoRepository;

  @Autowired
  private ClientesRepository clientesRepository;

  @Autowired
  private ProdutoRepository produtoRepository;

  @Autowired
  private ItemPedidoRepository itemPedidoRepository;

  @Override
  @Transactional
  public Pedido salvar(PedidoDto dto) {
    Cliente cliente = clientesRepository.findById(dto.getCliente())
        .orElseThrow(
        () -> new RegraNegocioException("Código de cliente inválido."));

    Pedido pedido = new Pedido();
    pedido.setTotal(dto.getTotal());
    pedido.setDataPedido(LocalDate.now());
    pedido.setCliente(cliente);
    pedido.setStatus(StatusPedido.REALIZADO);

    List<ItemPedido> itemPedidos = converterItems(pedido, dto.getItems());
    pedidoRepository.save(pedido);
    itemPedidoRepository.saveAll(itemPedidos);
    pedido.setItens(itemPedidos);
    return pedido;
  }

  private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDto> items) {
    if(items.isEmpty()){
      throw new RegraNegocioException("Não é possível realizar um pedido sem items.");
    }

    return items
        .stream()
        .map( dto -> {
          Produto produto = produtoRepository
              .findById(dto.getProduto())
              .orElseThrow(
                  () -> new RegraNegocioException("Código de Produto inválido: "+ dto.getProduto()
                  ));


          ItemPedido itemPedido = new ItemPedido();
          itemPedido.setQuantidade(dto.getQuantidade());
          itemPedido.setPedido(pedido);
          itemPedido.setProduto(produto);

          return itemPedido;
          //Método map da stream, retorna outra stream, tem que usar collect pra passar para Lista
        }).collect(Collectors.toList());
  }

  @Override
  public Optional<Pedido> obterPedidoCompleto(Integer id) {
    return pedidoRepository.findByIdFetchItens(id);
  }

  @Override
  @Transactional
  public void atualizaStatus(Integer id, StatusPedido statusPedido) {
    pedidoRepository.findById(id)
        .map( pedido -> {
          pedido.setStatus(statusPedido);
          return pedidoRepository.save(pedido);
        }).orElseThrow(() -> new PedidoNotFoundException() );
  }
}
