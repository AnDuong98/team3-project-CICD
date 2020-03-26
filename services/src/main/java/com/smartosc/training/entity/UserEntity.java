package com.smartosc.training.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(name = "USER_UK", columnNames = "username") })
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEntity extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1493326026017769274L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userid", nullable = false)
	private Long id;

	@Column(name = "fullname", length = 45)
	@NotBlank(message = "Full name is mandatory")
	private String fullName;

	@Column(name = "email", length = 125)
	private String email;

	@Column(name = "username", length = 36, nullable = false)
	private String username;
	
	@NotBlank(message = "Password is mandatory")
	@Column(name = "password")
	private String password;

	@OneToOne
	@JoinColumn(name="address_id")
	private AddressEntity address;

	@OneToMany(mappedBy = "user")
	private List<OrderEntity> orders;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name="user_role",joinColumns = @JoinColumn(name="user_id"),
	inverseJoinColumns = @JoinColumn(name="role_id"))
	private List<RoleEntity> roles;

	public UserEntity(String fullname, String email, String username, String password) {
		this.fullName = fullname;
		this.email = email;
		this.username = username;
		this.password = password;
	}
}