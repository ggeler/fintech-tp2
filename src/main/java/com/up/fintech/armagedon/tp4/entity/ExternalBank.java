package com.up.fintech.armagedon.tp4.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "external_banks")
public class ExternalBank {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotNull @Column(unique = true) private String cvu;
	@NotNull @Column(unique = true) private String name;
	@NotNull private String address;
}
