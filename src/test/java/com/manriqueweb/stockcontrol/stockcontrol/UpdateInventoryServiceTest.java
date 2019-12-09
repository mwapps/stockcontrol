package com.manriqueweb.stockcontrol.stockcontrol;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.manriqueweb.stockcontrol.stockcontrol.dto.ConceptType;
import com.manriqueweb.stockcontrol.stockcontrol.dto.RequestResponse;
import com.manriqueweb.stockcontrol.stockcontrol.dto.ResponseCode;
import com.manriqueweb.stockcontrol.stockcontrol.entity.Movement;
import com.manriqueweb.stockcontrol.stockcontrol.entity.Product;
import com.manriqueweb.stockcontrol.stockcontrol.repository.MovementRepository;
import com.manriqueweb.stockcontrol.stockcontrol.repository.ProductRepository;
import com.manriqueweb.stockcontrol.stockcontrol.service.UpdateInventoryService;

@SpringBootTest(classes = {UpdateInventoryService.class, ProductRepository.class, MovementRepository.class})
public class UpdateInventoryServiceTest {
	
	@MockBean
	private ProductRepository productRepository;

	@MockBean
	private MovementRepository movementRepository;
	
	@Autowired
	private UpdateInventoryService updateInventoryService;
	
	private Product mProduct = new Product();
	
    @BeforeEach
    void setMockOutput() {
    	mProduct.setId(2L);
    	mProduct.setDescription("Test product");
    	mProduct.setStock(0);
    	
    	Mockito.when(productRepository.existsById(1L)).thenReturn(false);
    	Mockito.when(productRepository.existsById(2L)).thenReturn(true);
    	Mockito.when(productRepository.findById(2L)).thenReturn(Optional.of(mProduct));
    	Mockito.when(productRepository.saveAndFlush(Mockito.any(Product.class))).thenReturn(mProduct);
    	Mockito.when(movementRepository.saveAndFlush(Mockito.any(Movement.class))).thenReturn(new Movement());
    }
	
	@Test
	public void test01() {
		RequestResponse mRequestResponse = new RequestResponse(ResponseCode.PRODUCT_KO.getResponseCode());
		
        // Product not exist on database
        assertThat(updateInventoryService.updateInventory(
        		1L,
        		LocalDateTime.now(),
        		1,
        		"ADD").getResponse())
        .isEqualTo(mRequestResponse.getResponse());

	}

	@Test
	public void test02() {
		RequestResponse mRequestResponse = new RequestResponse(ResponseCode.DATE_KO.getResponseCode());
		
        // The date is invalid
        assertThat(updateInventoryService.updateInventory(
        		2L,
        		null,
        		1,
        		ConceptType.ADD.getConcept()).getResponse())
        .isEqualTo(mRequestResponse.getResponse());

	}

	@Test
	public void test03() {
		RequestResponse mRequestResponse = new RequestResponse(ResponseCode.QTY_KO.getResponseCode());
		
        // Quantity is invalid
        assertThat(updateInventoryService.updateInventory(
        		2L,
        		LocalDateTime.now(),
        		null,
        		ConceptType.ADD.getConcept()).getResponse())
        .isEqualTo(mRequestResponse.getResponse());

	}

	@Test
	public void test04() {
		RequestResponse mRequestResponse = new RequestResponse(ResponseCode.QTY_KO.getResponseCode());
		
        // Quantity is invalid
        assertThat(updateInventoryService.updateInventory(
        		2L,
        		LocalDateTime.now(),
        		-1,
        		ConceptType.ADD.getConcept()).getResponse())
        .isEqualTo(mRequestResponse.getResponse());

	}

	@Test
	public void test05() {
		RequestResponse mRequestResponse = new RequestResponse(ResponseCode.CONCEPT_TYPE_KO.getResponseCode());
		
        // ConceptType is invalid
        assertThat(updateInventoryService.updateInventory(
        		2L,
        		LocalDateTime.now(),
        		1,
        		null).getResponse())
        .isEqualTo(mRequestResponse.getResponse());
	}

	@Test
	public void test06() {
		RequestResponse mRequestResponse = new RequestResponse(ResponseCode.OK.getResponseCode());
		
        // Add stock to an existing product
        assertThat(updateInventoryService.updateInventory(
        		2L,
        		LocalDateTime.now(),
        		1,
        		ConceptType.ADD.getConcept()).getResponse())
        .isEqualTo(mRequestResponse.getResponse());
        
        
        assertEquals(mProduct.getStock(), 1);
	}

	@Test
	public void test07() {
		RequestResponse mRequestResponse = new RequestResponse(ResponseCode.OK.getResponseCode());
		
        // Subtract stock to an existing product
        assertThat(updateInventoryService.updateInventory(
        		2L,
        		LocalDateTime.now(),
        		1,
        		ConceptType.SUBTRACT.getConcept()).getResponse())
        .isEqualTo(mRequestResponse.getResponse());
        
        
        assertEquals(mProduct.getStock(), -1);
	}

	@Test
	public void test08() {
		RequestResponse mRequestResponse = new RequestResponse(ResponseCode.OK.getResponseCode());
		
        // Replace the stock of an existing product
        assertThat(updateInventoryService.updateInventory(
        		2L,
        		LocalDateTime.now(),
        		13,
        		ConceptType.REPLACE.getConcept()).getResponse())
        .isEqualTo(mRequestResponse.getResponse());
        
        
        assertEquals(mProduct.getStock(), 13);
	}

}
