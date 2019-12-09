package com.manriqueweb.stockcontrol.stockcontrol.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.manriqueweb.stockcontrol.stockcontrol.dto.ConceptType;


@Entity
@Table(name = "Movement")
public class Movement implements Serializable {

	private static final long serialVersionUID = -626055707771356230L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	@Column(name = "DateMovement", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime datemovement;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;
    
	@Enumerated(EnumType.STRING)
	@Column(name = "ConceptType", length = 8, nullable = false)
    private ConceptType concept;

	@Column(name = "Quantity")
    private Integer quantity;

	public Movement() {
		super();
		this.id = null;
		this.datemovement = null;
		this.product = null;
		this.concept = null;
		this.quantity = 0;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getDatemovement() {
		return datemovement;
	}

	public void setDatemovement(LocalDateTime datemovement) {
		this.datemovement = datemovement;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ConceptType getConcept() {
		return concept;
	}

	public void setConcept(ConceptType concept) {
		this.concept = concept;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Movement [id=");
		builder.append(id);
		builder.append(", datemovement=");
		builder.append(datemovement);
		builder.append(", productId=");
		builder.append(product);
		builder.append(", concept=");
		builder.append(concept);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append("]");
		return builder.toString();
	}

}
