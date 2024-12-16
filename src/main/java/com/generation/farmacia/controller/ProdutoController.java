package com.generation.farmacia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.generation.farmacia.model.Produto;
import com.generation.farmacia.repository.CategoriaInterface;
import com.generation.farmacia.repository.ProdutoInterface;
import com.generation.farmacia.service.ProdutoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produto")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {
	
	@Autowired
	public ProdutoInterface produtoInterface;
	
	@Autowired
	public ProdutoService produtoService;
	
	@Autowired
	public CategoriaInterface categoriaInterface;
	
	@GetMapping
	public ResponseEntity<List<Produto>> getAll(){
		return ResponseEntity.ok(produtoInterface.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id){
		return produtoInterface.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Produto>> getByNome(@PathVariable String nome){
		return ResponseEntity.ok(produtoInterface.findByNomeContainingIgnoreCase(nome));
	}
	
	@GetMapping("/preco/{precoMin}/{precoMax}")
	public ResponseEntity<List<Produto>> getByPreco(@PathVariable Long precoMin, @PathVariable Long precoMax){
		return ResponseEntity.ok(produtoInterface.findByPrecoBetween(precoMin, precoMax));
	}
	
	@PostMapping
	public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto){
		
		if(categoriaInterface.existsById(produto.getCategoria().getId())) {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(produtoInterface.save(produtoService.calcularDesconto(produto)));
		}
		
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Categoria não Existe", null);
	}
	
	@PutMapping
	public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto){
		
		if(produtoInterface.existsById(produto.getId())) {
			if(categoriaInterface.existsById(produto.getCategoria().getId())) {
				
				return ResponseEntity.status(HttpStatus.OK)
						.body(produtoInterface.save(produto));
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Categoria não Existe", null);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Produto> produto = produtoInterface.findById(id);
		
		if(produto.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		produtoInterface.deleteById(id);
	}
}
