package com.generation.farmacia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.farmacia.model.Produto;

public interface ProdutoInterface extends JpaRepository<Produto, Long>{
	public List<Produto> findByNomeContainingIgnoreCase(@Param("nome") String Nome);
	
	public List<Produto> findByPrecoBetween(@Param("minPreco") Long min, @Param("maxPreco") Long max);
}
