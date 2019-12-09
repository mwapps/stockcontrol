package com.manriqueweb.stockcontrol.stockcontrol.dto;

import java.io.Serializable;
import java.util.Optional;

import com.manriqueweb.stockcontrol.stockcontrol.entity.Movement;

public class MovementResponse extends RequestResponse implements Serializable {

	private static final long serialVersionUID = -5516640297432211455L;
	
	private Movement movement;
	
	public MovementResponse(int response, Movement movement) {
		super();
		this.setResponse(response);
		this.movement = movement;
	}

	public Movement getMovement() {
		return movement;
	}

	public void setMovement(Movement movement) {
		this.movement = movement;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MovementResponse [movement=");
		builder.append(movement);
		builder.append(", getResponse()=");
		builder.append(getResponse());
		builder.append("]");
		return builder.toString();
	}
	
	public static class Builder extends RequestResponse.Builder {
		private Movement movement;
		
		public Builder() {
			super();
			this.movement = null;
		}
		
		public Builder movement(final Movement movement) {
			this.movement = movement;
			return this;
		}
		
		public Builder findByIdResponse(final Optional<Movement> mOptMovement) {
			if(mOptMovement.isPresent()) {
				this.response = ResponseCode.OK;
				this.movement = mOptMovement.get();
			}else{
				this.response = ResponseCode.MOVEMENT_KO;
				this.movement = null;
			}
			return this;
		}
		
		@Override
		public Builder response(final ResponseCode response) {
			this.response = response;
			return this;
		}
		@Override
		public MovementResponse build(){
			return new MovementResponse(this.response.getResponseCode(), this.movement);
		}
	}
}
