package com.generation.farmacia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.generation.farmacia.model.Categoria;
import com.generation.farmacia.model.Produto;
import com.generation.farmacia.repository.CategoriaInterface;

@Service
public class ProdutoService {
	
	@Autowired
	public CategoriaInterface categoriaInterface;
	
	
	public Produto calcularDesconto(Produto produto) {
		String desconto = "popular";
		
		Categoria categoria = categoriaInterface.findById(produto
				.getCategoria().getId())
				.orElse(null);
		
		if(categoria != null) {
			if(desconto.equalsIgnoreCase(categoria.getDescricao())){
				Long precoAtual = produto.getPreco();
				Long calcularDesconto = (precoAtual * 10)/100;
				
				//produto.setPreco((produto.getPreco() - ((produto.getPreco() * 10) / 100)));
				produto.setPreco(precoAtual - calcularDesconto);
			}
		}
		
		return produto;
	}
}
