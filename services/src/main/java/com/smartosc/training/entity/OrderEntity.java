package com.smartosc.training.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "orderdb")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6640421745652042569L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name")
	private String name;

	@Column(name = "totalprice")
	private Double totalPrice;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userid")
	private UserEntity user;

	@OneToMany(mappedBy = "orderid",cascade = CascadeType.ALL)
	private List<OrderDetailEntity> details;


}