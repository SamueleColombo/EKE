/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aes;



import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import message.ComplexMessage;
import message.eke.CryptedMessage;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 * @since 0.12
 */
public class AdvanceEncryptionStandard 
{
    /**
     * @since 0.12
     */
    private static final int ITERATION = 65536;
    
    /**
     * @since 0.12
     */
    private static final int KEY_LENGTH = 256;
    
    /**
     * @since 0.12
     */
    private static final int SALT_SIZE = 8;
    
    /**
     * @since 0.12
     */
    private static final String AES = "AES";
    
    /**
     * @since 0.12
     */
    private static final String FACTORY_PARAMETER = "PBKDF2WithHmacSHA1";
    
    /**
     * @since 0.12
     */
    private static final String CIPHER_PARAMETER = "AES/CBC/PKCS5Padding";
    
    /**
     * @since 0.12
     */    
    private static final Charset STANDARD_CHARSET = StandardCharsets.UTF_8;
    
    /**
     * 
     * @param key
     * @return 
     * @throws java.security.NoSuchAlgorithmException 
     * @throws java.security.spec.InvalidKeySpecException 
     * @since 0.12
     */
    public static SecretKey generateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException 
    {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(FACTORY_PARAMETER);
        KeySpec keySpec = new PBEKeySpec(key.toCharArray(), generateSalatureKey(), ITERATION, KEY_LENGTH);
        SecretKey temporaryKey = secretKeyFactory.generateSecret(keySpec);
        SecretKey secretKey = new SecretKeySpec(temporaryKey.getEncoded(), AES);

        return secretKey;
    }
    
    
    /**
     * This function generate the salute key throught a fiexed length and a
     * secure randomize algorithm.
     * @return 
     * @since 0.12
     */
    private static byte [] generateSalatureKey()
    {
        byte [] salt = new byte[SALT_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return new byte [] {-123,-56,-66,-72,-75,124,30,72};
    }
    
    /**
     * 
     * @param message
     * @param key
     * @throws java.security.NoSuchAlgorithmException
     * @throws javax.crypto.NoSuchPaddingException
     * @throws java.security.InvalidKeyException
     * @throws java.security.spec.InvalidParameterSpecException
     * @throws javax.crypto.IllegalBlockSizeException
     * @throws javax.crypto.BadPaddingException
     * @return
     * @throws java.security.spec.InvalidKeySpecException
     * @since 0.12
     */
    public static CryptedMessage encrypt(BigInteger message, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException 
    {
        return encrypt(message, AdvanceEncryptionStandard.generateKey(key));
    }
    
    /**
     * 
     * @param message
     * @param key
     * @return
     * @throws java.security.NoSuchAlgorithmException
     * @throws javax.crypto.NoSuchPaddingException
     * @throws java.security.InvalidKeyException
     * @throws java.security.spec.InvalidParameterSpecException
     * @throws javax.crypto.IllegalBlockSizeException
     * @throws javax.crypto.BadPaddingException
     * @since 0.12
     */
    public static CryptedMessage encrypt(BigInteger message, SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException 
    {

        Cipher encrypter = Cipher.getInstance(CIPHER_PARAMETER);
        encrypter.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters algorithmParameters = encrypter.getParameters();
        byte [] iv = algorithmParameters.getParameterSpec(IvParameterSpec.class).getIV();
        byte [] encoded = Base64.encodeBase64(message.toByteArray());
        byte [] fin = encrypter.doFinal(encoded);
        return new CryptedMessage(fin, iv);

    }
    
    
    /**
     * 
     * @param message
     * @param iv
     * @param key
     * @return 
     * @throws java.security.NoSuchAlgorithmException 
     * @throws javax.crypto.NoSuchPaddingException 
     * @throws java.security.InvalidKeyException 
     * @throws javax.crypto.IllegalBlockSizeException 
     * @throws javax.crypto.BadPaddingException 
     * @throws java.security.InvalidAlgorithmParameterException 
     * @throws java.security.spec.InvalidKeySpecException 
     * @since 0.12
     */
    public static BigInteger decrypt(BigInteger message, BigInteger iv, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException
    {
        return decrypt(message, iv, AdvanceEncryptionStandard.generateKey(key));
    }
    
    /**
     * 
     * @param message
     * @param iv
     * @param key
     * @return
     * @throws java.security.NoSuchAlgorithmException
     * @throws javax.crypto.NoSuchPaddingException
     * @throws java.security.InvalidKeyException
     * @throws javax.crypto.IllegalBlockSizeException
     * @throws javax.crypto.BadPaddingException
     * @throws java.security.InvalidAlgorithmParameterException
     * @since 0.12
     */
    public static BigInteger decrypt(BigInteger message, BigInteger iv, SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException 
    {

        Cipher decrypter = Cipher.getInstance(CIPHER_PARAMETER);
        decrypter.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.toByteArray()));

        return new BigInteger(Base64.decodeBase64(decrypter.doFinal(message.toByteArray())));

    }
}
