package com.manriqueweb.stockcontrol.stockcontrol;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.manriqueweb.stockcontrol.stockcontrol.dto.ConceptType;
import com.manriqueweb.stockcontrol.stockcontrol.dto.MovementResponse;
import com.manriqueweb.stockcontrol.stockcontrol.dto.MovementsResponse;
import com.manriqueweb.stockcontrol.stockcontrol.dto.ProductResponse;
import com.manriqueweb.stockcontrol.stockcontrol.dto.ProductsResponse;
import com.manriqueweb.stockcontrol.stockcontrol.dto.ResponseCode;
import com.manriqueweb.stockcontrol.stockcontrol.entity.Movement;
import com.manriqueweb.stockcontrol.stockcontrol.entity.Product;
import com.manriqueweb.stockcontrol.stockcontrol.repository.MovementRepository;
import com.manriqueweb.stockcontrol.stockcontrol.repository.ProductRepository;
import com.manriqueweb.stockcontrol.stockcontrol.service.InventoryCrudServise;

@SpringBootTest(classes = {InventoryCrudServise.class, ProductRepository.class, MovementRepository.class})
public class InventoryCrudServiceTest {
	
	@MockBean
	private ProductRepository productRepository;

	@MockBean
	private MovementRepository movementRepository;
	
	@Autowired
	private InventoryCrudServise inventoryCrudServise;
	
	private Product mProduct = new Product();
	private List<Product> mProducts = new ArrayList<Product>();
	
	private Movement mMovement = new Movement();
	private List<Movement> mMovements = new ArrayList<Movement>();
	
    @BeforeEach
    void setMockOutput() {
    	mProduct.setId(2L);
    	mProduct.setDescription("Test product");
    	mProduct.setStock(0);
    	
    	mProducts.add(mProduct);
    	
    	Mockito.when(productRepository.existsById(1L)).thenReturn(false);
    	Mockito.when(productRepository.existsById(2L)).thenReturn(true);
    	Mockito.when(productRepository.findById(2L)).thenReturn(Optional.of(mProduct));
    	Mockito.when(productRepository.retrieveByDescription(mProduct.getDescription())).thenReturn(mProducts);
    	Mockito.when(productRepository.retrieveByDescription("none")).thenReturn(new ArrayList<Product>());
    	Mockito.when(productRepository.findAll()).thenReturn(mProducts);
    	Mockito.when(productRepository.saveAndFlush(Mockito.any(Product.class))).thenReturn(mProduct);
    	Mockito.doNothing().when(productRepository).delete(Mockito.any(Product.class));
    	
    	mMovement.setId(2);
    	mMovement.setDatemovement(LocalDateTime.now());
    	mMovement.setConcept(ConceptType.ADD);
    	mMovement.setProduct(mProduct);
    	mMovement.setQuantity(2);
    	
    	mMovements.add(mMovement);
    	
    	Mockito.when(movementRepository.findById(1)).thenReturn(Optional.of(mMovement));
    	Mockito.when(movementRepository.findAll()).thenReturn(mMovements);
    	Mockito.when(movementRepository.retrieveBetweenDate(Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(mMovements);
    }
    
    /*
     * 
     * Test all Product services methods and their uses cases
     * 
     */
	
	@Test
	public void testProduct01() {
		final ProductResponse.Builder mProductResponseBuilder = new ProductResponse.Builder();
		ProductResponse mProductResponse = mProductResponseBuilder
												.response(ResponseCode.PRODUCT_KO)
												.build();

        // The Product not exist on database
        assertThat(inventoryCrudServise.retrieveProduct(1L))
        .isEqualTo(mProductResponse);
	}

	@Test
	public void testProduct02() {
		final ProductResponse.Builder mProductResponseBuilder = new ProductResponse.Builder();
		ProductResponse mProductResponse = mProductResponseBuilder
												.response(ResponseCode.PRODUCT_KO)
												.build();

        // The Product ID is not must to be null
        assertThat(inventoryCrudServise.retrieveProduct(null))
        .isEqualTo(mProductResponse);
	}

	@Test
	public void testProduct03() {
		final ProductResponse.Builder mProductResponseBuilder = new ProductResponse.Builder();
		ProductResponse mProductResponse = mProductResponseBuilder
												.response(ResponseCode.OK)
												.product(mProduct)
												.build();

        // The Product exist on database
        assertThat(inventoryCrudServise.retrieveProduct(2L))
        .isEqualTo(mProductResponse);
	}
	
	@Test
	public void testProduct031() {
		final ProductResponse.Builder mProductResponseBuilder = new ProductResponse.Builder();
		ProductResponse mProductResponse = mProductResponseBuilder
												.response(ResponseCode.UNKNOWN_ERROR)
												.product(null)
												.build();

		Mockito.when(productRepository.findById(2L)).thenThrow(PersistenceException.class);

        // The Product exist on database but occur a persistence exception
        assertThat(inventoryCrudServise.retrieveProduct(2L))
        .isEqualTo(mProductResponse);
	}

	@Test
	public void testProduct04() {
		final ProductsResponse.Builder mProductsResponseBuilder = new ProductsResponse.Builder();
		ProductsResponse mProductsResponse = mProductsResponseBuilder
												.response(ResponseCode.OK)
												.product(mProducts)
												.build();

        // The Product is looking for by name and it is found
        assertThat(inventoryCrudServise.retrieveByDescription(mProduct.getDescription()))
        .isEqualTo(mProductsResponse);
	}

	@Test
	public void testProduct05() {
		final ProductsResponse.Builder mProductsResponseBuilder = new ProductsResponse.Builder();
		ProductsResponse mProductsResponse = mProductsResponseBuilder
												.response(ResponseCode.OK)
												.product(new ArrayList<Product>())
												.build();

        // The Product is looking for by name and it is not found
        assertThat(inventoryCrudServise.retrieveByDescription("none"))
        .isEqualTo(mProductsResponse);
	}

	@Test
	public void testProduct06() {
		final ProductsResponse.Builder mProductsResponseBuilder = new ProductsResponse.Builder();
		ProductsResponse mProductsResponse = mProductsResponseBuilder
												.response(ResponseCode.OK)
												.product(mProducts)
												.build();

        // Retrieve all products
        assertThat(inventoryCrudServise.retrieveAllProducts())
        .isEqualTo(mProductsResponse);
	}

	@Test
	public void testProduct07() {
		final ProductResponse.Builder mProductResponseBuilder = new ProductResponse.Builder();
		ProductResponse mProductResponse = mProductResponseBuilder
												.response(ResponseCode.OK)
												.product(mProduct)
												.build();
		
		Product newProduct = new Product();
		newProduct.setId(null);
		newProduct.setDescription(mProduct.getDescription());
		newProduct.setStock(mProduct.getStock());

        // Add a new product using good data
        assertThat(inventoryCrudServise.addNewProduct(newProduct))
        .isEqualTo(mProductResponse);
	}

	@Test
	public void testProduct071() {
		final ProductResponse.Builder mProductResponseBuilder = new ProductResponse.Builder();
		ProductResponse mProductResponse = mProductResponseBuilder
												.response(ResponseCode.UNKNOWN_ERROR)
												.product(null)
												.build();
		
		Product newProduct = new Product();
		newProduct.setId(null);
		newProduct.setDescription(mProduct.getDescription());
		newProduct.setStock(mProduct.getStock());
		
		Mockito.when(productRepository.saveAndFlush(newProduct)).thenThrow(EntityExistsException.class);

        // Add a new product using good data but occurs a persistence exception
        assertThat(inventoryCrudServise.addNewProduct(newProduct))
        .isEqualTo(mProductResponse);
	}

	@Test
	public void testProduct08() {
		final ProductResponse.Builder mProductResponseBuilder = new ProductResponse.Builder();
		ProductResponse mProductResponse = mProductResponseBuilder
												.response(ResponseCode.PRODUCT_DESCRIPTION_KO)
												.product(null)
												.build();
		
		Product newProduct = new Product();
		newProduct.setId(null);
		newProduct.setDescription(null);
		newProduct.setStock(mProduct.getStock());

        // Add a new product using incorrect data
        assertThat(inventoryCrudServise.addNewProduct(newProduct))
        .isEqualTo(mProductResponse);
	}

	@Test
	public void testProduct09() {
		final ProductResponse.Builder mProductResponseBuilder = new ProductResponse.Builder();
		ProductResponse mProductResponse = mProductResponseBuilder
												.response(ResponseCode.OK)
												.product(mProduct)
												.build();
		
		Product newProduct = new Product();
		newProduct.setId(mProduct.getId());
		newProduct.setDescription(mProduct.getDescription());
		newProduct.setStock(mProduct.getStock());

        // Update a product using good data
        assertThat(inventoryCrudServise.updateProduct(newProduct))
        .isEqualTo(mProductResponse);
	}

	@Test
	public void testProduct10() {
		final ProductResponse.Builder mProductResponseBuilder = new ProductResponse.Builder();
		ProductResponse mProductResponse = mProductResponseBuilder
												.response(ResponseCode.PRODUCT_KO)
												.product(null)
												.build();
		
		Product newProduct = new Product();
		newProduct.setId(1L);
		newProduct.setDescription(mProduct.getDescription());
		newProduct.setStock(mProduct.getStock());

        // Update a product that it is no exist
        assertThat(inventoryCrudServise.updateProduct(newProduct))
        .isEqualTo(mProductResponse);
	}

	@Test
	public void testProduct11() {
		final ProductResponse.Builder mProductResponseBuilder = new ProductResponse.Builder();
		ProductResponse mProductResponse = mProductResponseBuilder
												.response(ResponseCode.PRODUCT_KO)
												.product(null)
												.build();
		
		Product newProduct = new Product();
		newProduct.setId(null);
		newProduct.setDescription(mProduct.getDescription());
		newProduct.setStock(mProduct.getStock());

        // Update a product using incorrect product Id value
        assertThat(inventoryCrudServise.updateProduct(newProduct))
        .isEqualTo(mProductResponse);
	}

	@Test
	public void testProduct12() {
		final ProductResponse.Builder mProductResponseBuilder = new ProductResponse.Builder();
		ProductResponse mProductResponse = mProductResponseBuilder
												.response(ResponseCode.PRODUCT_DESCRIPTION_KO)
												.product(null)
												.build();
		
		Product newProduct = new Product();
		newProduct.setId(mProduct.getId());
		newProduct.setDescription(null);
		newProduct.setStock(mProduct.getStock());

        // Update a product using incorrect description data
        assertThat(inventoryCrudServise.updateProduct(newProduct))
        .isEqualTo(mProductResponse);
	}

	@Test
	public void testProduct13() {
		final ProductResponse.Builder mProductResponseBuilder = new ProductResponse.Builder();
		ProductResponse mProductResponse = mProductResponseBuilder
												.response(ResponseCode.OK)
												.product(null)
												.build();
		
        // Delete a product using incorrect correct data
        assertThat(inventoryCrudServise.deleteProduct(mProduct.getId()))
        .isEqualTo(mProductResponse);
	}

	@Test
	public void testProduct14() {
		final ProductResponse.Builder mProductResponseBuilder = new ProductResponse.Builder();
		ProductResponse mProductResponse = mProductResponseBuilder
												.response(ResponseCode.PRODUCT_KO)
												.product(null)
												.build();
		
		Product newProduct = new Product();
		newProduct.setId(1L);
		newProduct.setDescription(mProduct.getDescription());
		newProduct.setStock(mProduct.getStock());

        // Delete a product that it is no exist
        assertThat(inventoryCrudServise.deleteProduct(newProduct.getId()))
        .isEqualTo(mProductResponse);
	}

	@Test
	public void testProduct15() {
		final ProductResponse.Builder mProductResponseBuilder = new ProductResponse.Builder();
		ProductResponse mProductResponse = mProductResponseBuilder
												.response(ResponseCode.PRODUCT_KO)
												.product(null)
												.build();
		
		Product newProduct = new Product();
		newProduct.setId(null);
		newProduct.setDescription(mProduct.getDescription());
		newProduct.setStock(mProduct.getStock());

        // Delete a product using incorrect product Id
        assertThat(inventoryCrudServise.deleteProduct(newProduct.getId()))
        .isEqualTo(mProductResponse);
	}

    /*
     * 
     * Test all Movements services methods and their uses cases
     * 
     */

	@Test
	public void testMovement01() {
		final MovementResponse.Builder mMovementResponseBuilder = new MovementResponse.Builder();
		MovementResponse mMovementResponse = mMovementResponseBuilder
												.response(ResponseCode.MOVEMENT_KO)
												.movement(null)
												.build();

        // The Movement not exist on database
        assertThat(inventoryCrudServise.retrieveMovement(2))
        .isEqualTo(mMovementResponse);
	}

	@Test
	public void testMovement02() {
		final MovementResponse.Builder mMovementResponseBuilder = new MovementResponse.Builder();
		MovementResponse mMovementResponse = mMovementResponseBuilder
												.response(ResponseCode.MOVEMENT_KO)
												.movement(null)
												.build();

        // The Movement ID is not must to be null
        assertThat(inventoryCrudServise.retrieveMovement(2))
        .isEqualTo(mMovementResponse);
	}

	@Test
	public void testMovement03() {
		final MovementResponse.Builder mMovementResponseBuilder = new MovementResponse.Builder();
		MovementResponse mMovementResponse = mMovementResponseBuilder
												.response(ResponseCode.OK)
												.movement(mMovement)
												.build();

        // The Movement exist on database
        assertThat(inventoryCrudServise.retrieveMovement(1))
        .isEqualTo(mMovementResponse);
	}

	@Test
	public void testMovement04() {
		final MovementsResponse.Builder mMovementsResponseBuilder = new MovementsResponse.Builder();
		MovementsResponse mMovementResponse = mMovementsResponseBuilder
												.response(ResponseCode.OK)
												.movement(mMovements)
												.build();

        // Retrieve all Movements
        assertThat(inventoryCrudServise.retrieveAllMovements())
        .isEqualTo(mMovementResponse);
	}

	@Test
	public void testMovement05() {
		final MovementsResponse.Builder mMovementsResponseBuilder = new MovementsResponse.Builder();
		MovementsResponse mMovementResponse = mMovementsResponseBuilder
												.response(ResponseCode.OK)
												.movement(mMovements)
												.build();
		
		Date fromDate = new Date();
		Date toDate = new Date(fromDate.getTime() + TimeUnit.DAYS.toMillis( 1 ));
		
        // Retrieve Movements between two correct dates
        assertThat(inventoryCrudServise.retrieveBetweenDate(fromDate, toDate))
        .isEqualTo(mMovementResponse);
	}

	@Test
	public void testMovement06() {
		final MovementsResponse.Builder mMovementsResponseBuilder = new MovementsResponse.Builder();
		MovementsResponse mMovementResponse = mMovementsResponseBuilder
												.response(ResponseCode.DATE_KO)
												.movement(null)
												.build();
		
		Date fromDate = new Date();
		Date toDate = new Date(fromDate.getTime() - TimeUnit.DAYS.toMillis( 1 ));
		
        // Retrieve Movements between incorrect dates range
        assertThat(inventoryCrudServise.retrieveBetweenDate(fromDate, toDate))
        .isEqualTo(mMovementResponse);
	}

	@Test
	public void testMovement07() {
		final MovementsResponse.Builder mMovementsResponseBuilder = new MovementsResponse.Builder();
		MovementsResponse mMovementResponse = mMovementsResponseBuilder
												.response(ResponseCode.DATE_KO)
												.movement(null)
												.build();
		
		Date fromDate = new Date();
		Date toDate = null;
		
        // Retrieve Movements between incorrect dates range
        assertThat(inventoryCrudServise.retrieveBetweenDate(fromDate, toDate))
        .isEqualTo(mMovementResponse);
	}

	@Test
	public void testMovement08() {
		final MovementsResponse.Builder mMovementsResponseBuilder = new MovementsResponse.Builder();
		MovementsResponse mMovementResponse = mMovementsResponseBuilder
												.response(ResponseCode.DATE_KO)
												.movement(null)
												.build();
		
		Date fromDate = null;
		Date toDate = new Date();
		
        // Retrieve Movements between incorrect dates range
        assertThat(inventoryCrudServise.retrieveBetweenDate(fromDate, toDate))
        .isEqualTo(mMovementResponse);
	}

	@Test
	public void testMovement09() {
		final MovementsResponse.Builder mMovementsResponseBuilder = new MovementsResponse.Builder();
		MovementsResponse mMovementResponse = mMovementsResponseBuilder
												.response(ResponseCode.UNKNOWN_ERROR)
												.movement(null)
												.build();
		
		Date fromDate = new Date();
		Date toDate = new Date(fromDate.getTime() + TimeUnit.DAYS.toMillis( 1 ));
		
    	Mockito.when(movementRepository.retrieveBetweenDate(fromDate, toDate)).thenThrow(PersistenceException.class);

        // When we retrieves Movements between two dates range and occurs a Persistence Exception
        assertThat(inventoryCrudServise.retrieveBetweenDate(fromDate, toDate))
        .isEqualTo(mMovementResponse);
	}

}
