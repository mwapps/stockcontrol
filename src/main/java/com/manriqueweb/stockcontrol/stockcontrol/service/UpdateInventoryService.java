package com.manriqueweb.stockcontrol.stockcontrol.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manriqueweb.stockcontrol.stockcontrol.dto.ConceptType;
import com.manriqueweb.stockcontrol.stockcontrol.dto.RequestResponse;
import com.manriqueweb.stockcontrol.stockcontrol.dto.ResponseCode;
import com.manriqueweb.stockcontrol.stockcontrol.entity.Movement;
import com.manriqueweb.stockcontrol.stockcontrol.entity.Product;
import com.manriqueweb.stockcontrol.stockcontrol.repository.MovementRepository;
import com.manriqueweb.stockcontrol.stockcontrol.repository.ProductRepository;

@Service
public class UpdateInventoryService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private MovementRepository movementRepository;

	public RequestResponse updateInventory(
			final Long productId, 
			final LocalDateTime datemovement, 
			final Integer quantity, 
			final String concept) {
		
		final RequestResponse.Builder mRequestResponseBuilder = new RequestResponse.Builder();

        if(productId==null || productId == 0)
        	return mRequestResponseBuilder.response(ResponseCode.PRODUCT_KO).build();
        
        if(datemovement==null)
        	return mRequestResponseBuilder.response(ResponseCode.DATE_KO).build();
        
        if(quantity==null || quantity < 0)
        	return mRequestResponseBuilder.response(ResponseCode.QTY_KO).build();
        
        if(concept==null)
        	return mRequestResponseBuilder.response(ResponseCode.CONCEPT_TYPE_KO).build();
        
        if(!ConceptType.ADD.getConcept().equals(concept) &&
        		!ConceptType.SUBTRACT.getConcept().equals(concept) &&
        		!ConceptType.REPLACE.getConcept().equals(concept))
        	return mRequestResponseBuilder.response(ResponseCode.CONCEPT_TYPE_KO).build();
        
        
        ConceptType mConceptType = ConceptType.valueOf(concept);
        
    	try {
			if(productRepository.existsById(productId)){
				Optional<Product> mOptProduct = productRepository.findById(productId);
				
				if(mOptProduct.isPresent()){
			       	Product mProduct = mOptProduct.get();
					Integer mStock = mProduct.getStock();
					
			        if(ConceptType.ADD==mConceptType){
			        	mProduct.setStock(mStock + quantity);
			        }else if(ConceptType.SUBTRACT==mConceptType){
			        	mProduct.setStock(mStock - quantity);
			        }else{
			        	mProduct.setStock(quantity);
			        }
			        
			        productRepository.saveAndFlush(mProduct);

			        Movement mMovement = new Movement();
			        mMovement.setDatemovement(datemovement);
			        mMovement.setProduct(mProduct);
			        mMovement.setConcept(mConceptType);
			        mMovement.setQuantity(quantity);

			        movementRepository.saveAndFlush(mMovement);
			        
			    	return mRequestResponseBuilder.response(ResponseCode.OK).build();
				}else{
			    	return mRequestResponseBuilder.response(ResponseCode.PRODUCT_KO).build();
				}
			}else{
				return mRequestResponseBuilder.response(ResponseCode.PRODUCT_KO).build();
			}
		} catch (Exception e) {
	    	return mRequestResponseBuilder.response(ResponseCode.UNKNOWN_ERROR).build();
		}
	}

}
