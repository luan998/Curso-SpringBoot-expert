package org.luan.controller;

import java.util.List;
import javax.validation.Valid;
import org.luan.domain.entity.Cliente;
import org.luan.repository.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

  @Autowired
  private ClientesRepository clientesRepository;

  @RequestMapping(
      value = {"/hello/{nome}", "/clientes/hello"},
      method = RequestMethod.GET)
  public String helloClientes(@PathVariable("nome") String nomeCliente) {
    return String.format("Hello %s", nomeCliente);
  }

  @GetMapping("/{id}")
  public Cliente getClienteById(@PathVariable Integer id) {
    return clientesRepository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Cliente saveCliente(@RequestBody @Valid Cliente cliente) {
    return clientesRepository.save(cliente);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Integer id) {
    clientesRepository.findById(id)
        .map(cliente -> {
          clientesRepository.delete(cliente);
          return cliente;
        })
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Cliente update(@PathVariable Integer id,
      @RequestBody @Valid Cliente cliente) {
    return clientesRepository
        .findById(id)
        .map(c -> {
          cliente.setId(c.getId());
          clientesRepository.save(cliente);
          return cliente;
        }).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Cliente não encontrado"));
  }

  @GetMapping
  public List<Cliente> find(Cliente filter) {
    //Objeto que permite configurar para encontrar os clientes através das propriedades
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(StringMatcher.CONTAINING);
    //Pega as propriedades populadas e cria um exemplo
    Example example = Example.of(filter, matcher);

    return clientesRepository.findAll(example);
  }
}
