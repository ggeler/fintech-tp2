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
	
	@JsonIgnore private static final String codigoPsp = "1234";
	@JsonIgnore private static final String codigoClaveVirtual = "000";

	@JsonIgnore @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true) 
	private String cvu;
	
	@JsonIgnore @OneToOne 
	private Wallet wallet;

	public Cvu() {
	}
	
	public Cvu(Wallet wallet) {
		this.wallet = wallet;
		var cvu = new String();
		var uuid = wallet.getWalletId().toString();
		String account = new String();
		for (int i=0;i<uuid.length();i++) {
			var digit = uuid.charAt(i);
			var decimal = Character.digit(digit,16);
			account += String.valueOf(decimal);
		}
		String block1 = codigoClaveVirtual+codigoPsp; 
		String block2 = account.substring(account.length()-12, account.length());
		
		cvu = block1+digitoVerificador(block1)+"0"+block2+digitoVerificador(block2);
		this.cvu = cvu;
	}
	private String digitoVerificador(String s) {
		int verificador = 0;
		int[] primos = {3,1,7,9};
		int indexPrimos = 0;
		for (int i = s.length()-1; i>=0; i--) {
			if (indexPrimos > primos.length-1)
				indexPrimos = 0;
			verificador += Character.getNumericValue(s.charAt(i))*primos[indexPrimos];
			indexPrimos++;
		}
		int lastDigit = verificador % 10;
		int diferencial = 10 - lastDigit;
		if (diferencial == 10) diferencial = 0;
		return Integer.toString(diferencial);
	}

	public static String getPspCode(String cvu) {
		return cvu.substring(0, 7);
	}
	public static boolean isInternal(String cvu) {
		var block1 = codigoClaveVirtual+codigoPsp;
		return getPspCode(cvu).equals(block1);
	}
}
