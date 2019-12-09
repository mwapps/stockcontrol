package com.manriqueweb.stockcontrol.stockcontrol.dto;

public class RequestResponse {
	private int response;

	public RequestResponse() {
		super();
	}

	public RequestResponse(int response) {
		super();
		this.response = response;
	}

	public int getResponse() {
		return response;
	}

	public void setResponse(int response) {
		this.response = response;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestResponse other = (RequestResponse) obj;
		if (this.response != other.response)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RequestResponse [response=");
		builder.append(response);
		builder.append("]");
		return builder.toString();
	}

	public static class Builder {
		protected ResponseCode response;
		
		public Builder() {
			this.response = ResponseCode.NOTSET;
		}
		public Builder response(final ResponseCode response) {
			this.response = response;
			return this;
		}
		public RequestResponse build(){
			return new RequestResponse(this.response.getResponseCode());
		}
	}
	

}
