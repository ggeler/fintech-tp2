package com.up.fintech.armagedon.tp4.misc.component;

import java.util.Random;

public final class RandomConfirmationCode {

	public static String generateRandomCode() {
		Random random = new Random();
		int begin = (int) 'A';
		int end = (int) 'Z';
		int lenght = 6;
		var generatedString = random.ints(begin, end+1)
				.filter(i -> (i<=57 || i>=65) && (i<=90 || i>=97))
				.limit(lenght)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
		
		return generatedString;
	}
}
