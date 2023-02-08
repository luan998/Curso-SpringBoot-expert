package org.luan.controller;

import java.util.List;
import javax.validation.Valid;
import org.luan.domain.entity.Produto;
import org.luan.repository.ProdutoRepository;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

  @Autowired
  private ProdutoRepository produtoRepository;

  @GetMapping("/{id}")
  public Produto getProdutoById(@PathVariable Integer id){
    return produtoRepository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Produto saveProduto(@RequestBody @Valid Produto produto){
    return produtoRepository.save(produto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Integer id) {
    produtoRepository.findById(id)
        .map(produto -> {
          produtoRepository.delete(produto);
          return produto;
        })
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Produto update(@PathVariable Integer id,
      @RequestBody @Valid Produto produto) {
    return produtoRepository
        .findById(id)
        .map(c -> {
          produto.setId(c.getId());
          produtoRepository.save(produto);
          return produto;
        }).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
  }

  @GetMapping
  public List<Produto> find(Produto filter) {

    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(StringMatcher.CONTAINING);

    Example example = Example.of(filter, matcher);

    return produtoRepository.findAll(example);
  }
}
