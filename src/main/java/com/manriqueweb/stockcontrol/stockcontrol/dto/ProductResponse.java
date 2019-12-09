package com.manriqueweb.stockcontrol.stockcontrol.dto;

import java.io.Serializable;
import java.util.Optional;

import com.manriqueweb.stockcontrol.stockcontrol.entity.Product;

public class ProductResponse extends RequestResponse implements Serializable {

	private static final long serialVersionUID = 391869457708451628L;
	
	private Product product;
	
	public ProductResponse(int response, Product product) {
		super();
		this.setResponse(response);
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProductResponse [product=");
		builder.append(product);
		builder.append(", getResponse()=");
		builder.append(getResponse());
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		ProductResponse other = (ProductResponse) obj;
		if (product == null && other!=null && (other.getProduct()==null && this.getResponse()==other.getResponse()))
			return true;
		else if (other.product==null)
			return false;
		else if((product.getId()==other.product.getId()) && (this.getResponse()==other.getResponse()))
			return true;
		return false;
	}

	public static class Builder extends RequestResponse.Builder {
		private Product product;
		
		public Builder() {
			super();
			this.product = null;
		}
		
		public Builder product(final Product product) {
			this.product = product;
			return this;
		}
		
		public Builder findByIdResponse(final Optional<Product> mOptProduct) {
			if(mOptProduct.isPresent()) {
				this.response = ResponseCode.OK;
				this.product = mOptProduct.get();
			}else{
				this.response = ResponseCode.PRODUCT_KO;
				this.product = null;
			}
			return this;
		}
		
		@Override
		public Builder response(final ResponseCode response) {
			this.response = response;
			return this;
		}
		@Override
		public ProductResponse build(){
			return new ProductResponse(this.response.getResponseCode(), this.product);
		}
	}

}
