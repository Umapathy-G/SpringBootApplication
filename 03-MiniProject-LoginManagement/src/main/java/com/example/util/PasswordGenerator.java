package com.example.util;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int PASSWORD_LENGTH = 10;

	public static String generateTemporaryPassword() {
		Random random = new SecureRandom();
		StringBuilder password = new StringBuilder();

		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			int randomIndex = random.nextInt(CHARACTERS.length());
			password.append(CHARACTERS.charAt(randomIndex));
		}

		return password.toString();
	}
}
