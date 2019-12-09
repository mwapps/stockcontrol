package com.manriqueweb.stockcontrol.stockcontrol.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.manriqueweb.stockcontrol.stockcontrol.entity.Movement;

public class MovementsResponse extends RequestResponse implements Serializable {

	private static final long serialVersionUID = 4427027119835232174L;
	
	private List<Movement> movements;
	
	public MovementsResponse(int response, List<Movement> movements) {
		super();
		this.setResponse(response);
		this.movements = movements;
	}

	public List<Movement> getMovement() {
		return movements;
	}

	public void setMovement(List<Movement> movements) {
		this.movements = movements;
	}

	
	@Override
	public String toString() {
		final int maxLen = 7;
		StringBuilder builder2 = new StringBuilder();
		builder2.append("MovementsResponse [movements=");
		builder2.append(movements != null ? movements.subList(0, Math.min(movements.size(), maxLen)) : null);
		builder2.append("]");
		return builder2.toString();
	}


	public static class Builder extends RequestResponse.Builder {
		private List<Movement> movements;
		
		public Builder() {
			super();
			this.movements = null;
		}
		
		public Builder init() {
			this.response = ResponseCode.NOTSET;
			this.movements = null;
			return this;
		}
		
		public Builder movement(final List<Movement> movements) {
			this.movements = movements;
			return this;
		}
		
		public Builder findAllResponse(final List<Movement> mMovements) {
			this.response = ResponseCode.OK;
			if(mMovements==null) {
				this.movements = new ArrayList<Movement>();
			}else{
				this.movements = mMovements;
			}
			return this;
		}
		
		@Override
		public Builder response(final ResponseCode response) {
			this.response = response;
			return this;
		}
		@Override
		public MovementsResponse build(){
			return new MovementsResponse(this.response.getResponseCode(), this.movements);
		}
	}
}
