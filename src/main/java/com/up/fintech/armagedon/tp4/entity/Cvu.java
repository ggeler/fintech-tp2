package com.up.fintech.armagedon.tp4.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "cvus")
public class Cvu {

	@JsonIgnore @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(unique = true) 
	private String cvu;
	@JsonIgnore @OneToOne private Wallet wallet;

	public Cvu() {
	}
	
	public Cvu(Wallet wallet) {
		this.wallet = wallet;
		var cvu = new String();
		var uuid = wallet.getWalletId().toString();
		cvu = "000"+"1111"+"9"+"0"+uuid.substring(uuid.length()-12, uuid.length())+"9";
		this.cvu = cvu;
	}
}
