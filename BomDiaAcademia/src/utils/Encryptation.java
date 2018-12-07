package utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Encryptation {

	/**
	 * RANDOM a random to auxiliate in the encryption method
	 */
	private static final Random RANDOM = new SecureRandom();
	/**
	 * ALPHABET defines the valida key chars to the encryptions
	 */
	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	/**
	 * ITERATIONS the number of times that the encryption key will iterate
	 */
	private static final int ITERATIONS = 10000;
	/**
	 * KEY_LENGTH length of the encryptation key
	 */
	private static final int KEY_LENGTH = 256;

	/**
	 * Salt is a random encryptation value
	 * 
	 * @param length Salt length
	 * @return String of the Salt value
	 */
	protected String getSalt(int length) {
		StringBuilder returnValue = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}

		return new String(returnValue);
	}

	/**
	 * 
	 * @param password Password who is being encrypted
	 * @param salt     Salt key value
	 * @return Hash password
	 */
	protected byte[] hash(char[] password, byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
		Arrays.fill(password, Character.MIN_VALUE);
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
		} finally {
			spec.clearPassword();
		}
	}

	/**
	 * 
	 * @param password Password who is being encrypted
	 * @param salt     Salt key value
	 * @return Encrypted password value
	 */
	protected String generateSecurePassword(String password, String salt) {
		String returnValue = null;

		byte[] securePassword = hash(password.toCharArray(), salt.getBytes());

		returnValue = Base64.getEncoder().encodeToString(securePassword);

		return returnValue;
	}

	/**
	 * 
	 * @param providedPassword Non Encrypted password
	 * @param securedPassword  Encrypted password
	 * @param salt             Salt key value
	 * @return true if keys match, false if not
	 */
	protected boolean verifyUserPassword(String providedPassword, String securedPassword, String salt) {
		boolean returnValue = false;

		// Generate New secure password with the same salt
		String newSecurePassword = generateSecurePassword(providedPassword, salt);

		// Check if two passwords are equal
		returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

		return returnValue;
	}

}
