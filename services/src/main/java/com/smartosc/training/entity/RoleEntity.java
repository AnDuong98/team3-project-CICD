package com.smartosc.training.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "role",
	uniqueConstraints = { @UniqueConstraint(name = "ROLE_UK", columnNames = "role_name") })
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleEntity extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1715861737731007746L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "roleid", nullable = false)
	private Long id;
	
	@Column(name = "role_name", length = 45, nullable = false)
	private String name;
	
	
	@ManyToMany(mappedBy = "roles")
	private List<UserEntity> users;

}