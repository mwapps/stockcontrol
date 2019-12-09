package com.manriqueweb.stockcontrol.stockcontrol.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.manriqueweb.stockcontrol.stockcontrol.entity.Product;

public class ProductsResponse extends RequestResponse implements Serializable {

	private static final long serialVersionUID = 391869457708451628L;
	
	private List<Product> products;
	
	public ProductsResponse(int response, List<Product> products) {
		super();
		this.setResponse(response);
		this.products = products;
	}

	public List<Product> getProduct() {
		return products;
	}

	public void setProduct(List<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		final int maxLen = 7;
		StringBuilder builder2 = new StringBuilder();
		builder2.append("ProductsResponse [products=");
		builder2.append(products != null ? products.subList(0, Math.min(products.size(), maxLen)) : null);
		builder2.append("]");
		return builder2.toString();
	}

	@Override
	public boolean equals(Object obj) {
		ProductsResponse other = (ProductsResponse) obj;
		if (products == null && other!=null && (other.getProduct()==null && this.getResponse()==other.getResponse()))
			return true;
		else if (other.products==null)
			return false;
		else if((products.size()==other.products.size()) && (this.getResponse()==other.getResponse()))
			return true;
		return false;
	}
	
	public static class Builder extends RequestResponse.Builder {
		private List<Product> products;
		
		public Builder() {
			super();
			this.products = null;
		}
		
		public Builder product(final List<Product> products) {
			this.products = products;
			return this;
		}
		
		public Builder findAllResponse(final List<Product> products) {
			this.response = ResponseCode.OK;
			if(products==null) {
				this.products = new ArrayList<Product>();
			}else{
				this.products = products;
			}
			return this;
		}
		
		@Override
		public Builder response(final ResponseCode response) {
			this.response = response;
			return this;
		}
		@Override
		public ProductsResponse build(){
			return new ProductsResponse(this.response.getResponseCode(), this.products);
		}
	}

}
