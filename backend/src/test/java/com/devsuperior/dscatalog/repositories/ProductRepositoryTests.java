package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts = 25L;
	
	@BeforeEach
	void setup() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
	}
	
	@Test //Deveria salvar e ver se auto incrementou quando o Id for nulo
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		
		Product product = Factory.createProduct();
		
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
		
	}
	
	@Test //Nome do método + Deveria deletar o objeto quando o Id existe
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		//Primeiro deleta o Id existente
		repository.deleteById(existingId);
		
		//Depois verifica se o id foi realmente deletado
		Optional<Product> result = repository.findById(existingId);
		
		//Verifica se o resultado foi igual a False, como não existe mais o teste dá correto
		Assertions.assertFalse(result.isPresent());
		
	}
	
	
	
	@Test // Deveria lançar a exceção EmptyResultDataAccessException quando o ID não existe
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		
		//Lança esse tipo de Exceção caso o Id não existe, como o que está sendo passado não existe o teste dá correto
		Assertions.assertThrows(EmptyResultDataAccessException.class, () ->{
			repository.deleteById(nonExistingId);
		});
	}
	
	@Test //Deveria retornar um Optional não vazio quando o id existir
	public void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {
			
		Optional<Product> result = repository.findById(existingId);
		
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test //Deveria retornar um Optional vazio quando o id não existir
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists() {
			
		Optional<Product> result = repository.findById(nonExistingId);
		
		Assertions.assertTrue(result.isEmpty());
	}

}
