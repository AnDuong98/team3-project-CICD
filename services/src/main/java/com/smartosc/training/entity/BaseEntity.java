package com.smartosc.training.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
<<<<<<< HEAD
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
=======
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
>>>>>>> team-3/develop
public abstract class BaseEntity {
	

	@Column(name = "createddate")
	@CreationTimestamp
	private Date createddate;
	

	@Column(name = "modifieddate")
	@UpdateTimestamp
	private Date modifieddate;

	@Column
	private int status;
<<<<<<< HEAD

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public Date getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}

	public int getStatus() {
		return status;
	}
=======
	

>>>>>>> team-3/develop

	public void setStatus(int status) {
		this.status = status;
	}
}