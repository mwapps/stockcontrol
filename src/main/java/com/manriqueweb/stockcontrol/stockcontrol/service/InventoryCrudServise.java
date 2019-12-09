package com.manriqueweb.stockcontrol.stockcontrol.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manriqueweb.stockcontrol.stockcontrol.dto.MovementResponse;
import com.manriqueweb.stockcontrol.stockcontrol.dto.MovementsResponse;
import com.manriqueweb.stockcontrol.stockcontrol.dto.ProductResponse;
import com.manriqueweb.stockcontrol.stockcontrol.dto.ProductsResponse;
import com.manriqueweb.stockcontrol.stockcontrol.dto.ResponseCode;
import com.manriqueweb.stockcontrol.stockcontrol.entity.Product;
import com.manriqueweb.stockcontrol.stockcontrol.repository.MovementRepository;
import com.manriqueweb.stockcontrol.stockcontrol.repository.ProductRepository;

@Service
public class InventoryCrudServise {
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private MovementRepository movementRepository;

	private final ProductResponse.Builder mProductResponseBuilder = new ProductResponse.Builder();
	private final MovementResponse.Builder mMovementResponseBuilder = new MovementResponse.Builder();
	private final MovementsResponse.Builder mMovementsResponseBuilder = new MovementsResponse.Builder();
	
	//Product CRUD methods
	public ProductResponse retrieveProduct(final Long productId) {
		if(productId==null || productId<=0)
			return mProductResponseBuilder
					.response(ResponseCode.PRODUCT_KO)
					.build();
		
		try {
			return mProductResponseBuilder
					.findByIdResponse(productRepository.findById(productId))
					.build();
		} catch (Exception e) {
			return mProductResponseBuilder
					.response(ResponseCode.UNKNOWN_ERROR)
					.build();
	    }
	}
	
	public ProductsResponse retrieveByDescription(final String description) {
		final ProductsResponse.Builder mProductsResponseBuilder = new ProductsResponse.Builder();
		
		if(description.isEmpty())
			return mProductsResponseBuilder
					.response(ResponseCode.PRODUCT_DESCRIPTION_KO)
					.build();
		
		try {
			return mProductsResponseBuilder
					.findAllResponse(productRepository.retrieveByDescription(description))
					.build();
		} catch (Exception e) {
			return mProductsResponseBuilder
					.response(ResponseCode.UNKNOWN_ERROR)
					.build();
	    }
	}
	
	public ProductsResponse retrieveAllProducts() {
		final ProductsResponse.Builder mProductsResponseBuilder = new ProductsResponse.Builder();
		
		try {
			return mProductsResponseBuilder
					.findAllResponse(productRepository.findAll())
					.build();
		} catch (Exception e) {
			return mProductsResponseBuilder
					.response(ResponseCode.UNKNOWN_ERROR)
					.build();
	    }
	}
	
	public ProductResponse addNewProduct(final Product product) {
		if(product.getDescription()==null || product.getDescription().isEmpty())
			return mProductResponseBuilder
					.response(ResponseCode.PRODUCT_DESCRIPTION_KO)
					.product(null)
					.build();
		
		if(product.getId()!=null && productRepository.existsById(product.getId())) {
			return mProductResponseBuilder
					.response(ResponseCode.PRODUCT_KO)
					.product(null)
					.build();
		}else{
			return saveProduct(product);
		}
	}

	public ProductResponse updateProduct(final Product product) {
		if(product.getId()==null || product.getId()<=0)
			return mProductResponseBuilder
					.response(ResponseCode.PRODUCT_KO)
					.product(null)
					.build();

		if(product.getDescription()==null || product.getDescription().isEmpty())
			return mProductResponseBuilder
					.response(ResponseCode.PRODUCT_DESCRIPTION_KO)
					.product(null)
					.build();
		
		if(productRepository.existsById(product.getId())) {
			return saveProduct(product);
		}else{
			return mProductResponseBuilder
					.response(ResponseCode.PRODUCT_KO)
					.product(null)
					.build();
		}
	}
	
	//TODO The products with movements can not be delete
	public ProductResponse deleteProduct(final Long productId) {
		if(productId==null || productId<=0)
			return mProductResponseBuilder
					.response(ResponseCode.PRODUCT_KO)
					.build();

		if(productRepository.existsById(productId)) {
			try {
				productRepository.deleteById(productId);
				
				return mProductResponseBuilder
						.response(ResponseCode.OK)
						.product(null)
						.build();
			} catch (Exception e) {
				return mProductResponseBuilder
						.response(ResponseCode.UNKNOWN_ERROR)
						.build();
		    }
		}else{
			return mProductResponseBuilder
					.response(ResponseCode.PRODUCT_KO)
					.build();
		}
	}
	
	private ProductResponse saveProduct(final Product product) {
		try {
			return mProductResponseBuilder
					.response(ResponseCode.OK)
					.product(productRepository.saveAndFlush(product))
					.build();
		} catch (Exception e) {
			return mProductResponseBuilder
					.response(ResponseCode.UNKNOWN_ERROR)
					.build();
	    }
	}
	
	//Movement CRUD methods
	public MovementResponse retrieveMovement(final Integer movementId) {
		if(movementId==null || movementId<=0)
			return mMovementResponseBuilder
					.response(ResponseCode.MOVEMENT_KO)
					.build();
		try {
			return mMovementResponseBuilder
					.findByIdResponse(movementRepository.findById(movementId))
					.build();
		} catch (Exception e) {
			return mMovementResponseBuilder
					.response(ResponseCode.UNKNOWN_ERROR)
					.build();
	    }
	}
	
	public MovementsResponse retrieveAllMovements() {
		try {
			return mMovementsResponseBuilder
					.findAllResponse(movementRepository.findAll())
					.build();
		} catch (Exception e) {
			return mMovementsResponseBuilder
					.init()
					.response(ResponseCode.UNKNOWN_ERROR)
					.build();
	    }
	}

	public MovementsResponse retrieveBetweenDate(Date fromDate, Date toDate) {
		if(fromDate==null || toDate==null || toDate.before(fromDate))
			return mMovementsResponseBuilder
					.init()
					.response(ResponseCode.DATE_KO)
					.build();
		
		try {
			return mMovementsResponseBuilder
					.findAllResponse(movementRepository.retrieveBetweenDate(fromDate, toDate))
					.build();
		} catch (Exception e) {
			return mMovementsResponseBuilder
					.init()
					.response(ResponseCode.UNKNOWN_ERROR)
					.build();
	    }
	}
}
