package com.smartosc.training.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="orderdetail")
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class OrderDetailEntity extends BaseEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5622510985500507798L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private int quantity;
	@Column
	@NotNull
	private String price;
	
	@ManyToOne
    @JoinColumn(name = "orderid")
    private OrderEntity orderid;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
<<<<<<< HEAD
    private ProductEntity product_id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public OrderEntity getOrderid() {
		return orderid;
	}

	public void setOrder(OrderEntity orderid) {
		this.orderid = orderid;
	}

	public ProductEntity getProduct_id() {
		return product_id;
	}

	public void setProduct(ProductEntity product) {
		this.product_id = product;
	}
=======
	private ProductEntity product;
	

	
>>>>>>> team-3/develop
}