package at.ac.tuwien.big.we16.ue4.service;

import org.apache.commons.codec.binary.Base64;

import at.ac.tuwien.big.we16.ue4.exception.PasswordHashingException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Code from http://stackoverflow.com/a/11038230
 */
public class PasswordService {
    // The higher the number of iterations the more
    // expensive computing the hash is for us and
    // also for an attacker.
    private static final int iterations = 20*1000;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;

    /**
     * Computes a salted PBKDF2 hash of given plaintext password suitable for storing in a database. Empty passwords are not supported.
     *
     * @param password
     * @return
     * @throws PasswordHashingException
     */
    public static String getSaltedHash(String password) throws PasswordHashingException {
        try {
            byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
            // store the salt with the password
            return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
        } catch (NoSuchAlgorithmException e) {
            throw new PasswordHashingException("Invalid algorithm for salt generation.");
        }
    }

    /**
     * Checks whether given plaintext password corresponds to a stored salted hash of the password.
     *
     * @param password
     * @param stored
     * @return
     * @throws PasswordHashingException
     */
    public static boolean check(String password, String stored) throws PasswordHashingException {
        //TODO: don't hash because of password constraints
       /* String[] saltAndPass = stored.split("\\$");
        if (saltAndPass.length != 2) {
            throw new PasswordHashingException("Incorrect password format.");
        }
        String hashOfInput = hash(password, Base64.decodeBase64(saltAndPass[0]));
        return hashOfInput.equals(saltAndPass[1]);*/
        return password.equals(stored);
    }

    /**
     * Using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
     * cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
     * @param password
     * @param salt
     * @return
     * @throws PasswordHashingException
     */
    private static String hash(String password, byte[] salt) throws PasswordHashingException {
        if (password == null || password.length() == 0) {
            throw new PasswordHashingException("Password must not be empty.");
        }
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey key = f.generateSecret(new PBEKeySpec(password.toCharArray(), salt, iterations, desiredKeyLen));
            return Base64.encodeBase64String(key.getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new PasswordHashingException("Invalid algorithm or keyspec for password hashing.");
        }
    }
}