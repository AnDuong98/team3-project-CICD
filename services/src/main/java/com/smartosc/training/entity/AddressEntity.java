package com.smartosc.training.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class AddressEntity extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1300214721335294925L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String address;
	@Column
	private String phone;
	@Column
	private Boolean gender;

	@OneToOne(mappedBy = "address",cascade = CascadeType.ALL)
	private UserEntity user;

	public AddressEntity(String address, String phone, Boolean gender, int status, Date createdDate, Date modifiedDate) {

	}

	@Override
	public String toString() {
		return "AddressEntity{" +
				"id=" + id +
				", address='" + address + '\'' +
				", phone='" + phone + '\'' +
				", gender=" + gender +
				", user=" + user +
				'}';
	}
}
