package pl8;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
/**
 * This class is used to generate and compare password
 * hashes by the Login and Signup classes.
 */
public class Password {
	/* return the hashed version of the plaintext password using PBKDF2*/
	public static String getHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
		/* Using 1000 iterations */
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();
         
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }
     /* Produce a byte array to be used to salt the hash */
	protected static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

	protected static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }
        else
        {
            return hex;
        }
    }
	/**
	 * Method for password validation, used by Login class.
	 * @param prospect - the supplied password to check
	 * @param stored - the password on record
	 */
	public static boolean validate(String prospect, String stored) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String[] parts = stored.split(":");
        /* Iterations is stored at the beginning of the String */
        int iterations = Integer.parseInt(parts[0]);
        
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);
         
        PBEKeySpec spec = new PBEKeySpec(prospect.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();
         
        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }
	/*
	 * @param hex - hex representation to produce byte array
	 */
    protected static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
