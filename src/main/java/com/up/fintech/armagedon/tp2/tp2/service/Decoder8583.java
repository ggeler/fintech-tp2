package com.up.fintech.armagedon.tp2.tp2.service;

import org.springframework.stereotype.Service;

import com.up.fintech.armagedon.tp2.tp2.dto.Iso8583;

@Service
public class Decoder8583 {
	private String[] memo = new String[16];
	
	private Iso8583 converterHelper(String msg, int value, Iso8583 str) {
		
		if (msg==null || msg.isEmpty())
			return null;
		
		char c = Character.toUpperCase(msg.charAt(0));
		
		if ( ! ((c>='0' && c<='9') || (c>='A' && c<='F') )) 
			return null;
		
		int n = Character.getNumericValue(c);
		
		if (memo[n]==null) {
			String binary = String.format("%4s",Integer.toBinaryString(n)).replace(" ", "0");
			memo[n] = binary;
		}
		var functions = str.getFunctions()!=null ? str.getFunctions()+bitDecoder(memo[n],(value*4)+1):bitDecoder(memo[n],(value*4)+1);
		var bits = str.getBits()!=null ? str.getBits()+" "+memo[n]: memo[n];
		
		str.setFunctions(functions);
		str.setBits(bits);
		
		converterHelper(msg.substring(1), ++value, str);
		
		return str;
			
	}
	private String bitDecoder(String nibble, int value) {
		String str = "";
		if (nibble.length()==0)
			return "";
		int b = Character.getNumericValue(nibble.charAt(0));
		if (b==1) {
			str = "F"+value+" - ";
		}
		str += bitDecoder(nibble.substring(1), value+1);
		return str;
	}

	public Iso8583 converter(String s) {
		
		if (s.length()!=16 && s.length()!=23)
			throw new IllegalArgumentException("Msg debes tener Long = 16 || = 23");
		
		String tmp = s.replaceAll("\\s", "");
		
		return converterHelper(tmp, 0, new Iso8583());
	}

}
