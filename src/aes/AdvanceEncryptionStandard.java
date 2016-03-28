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
 * @author Samuele Colombo
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
     * @since 0.12
     */
    private SecretKey key;
    
    
    /**
     * 
     * @param key
     * @since 0.12
     */
    public AdvanceEncryptionStandard(String key)
    {
        this.key = generateKey(key);
    }
    
    /**
     * 
     * @param key
     * @return 
     * @since 0.12
     */
    private SecretKey generateKey(String key) 
    {
        try 
        {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(FACTORY_PARAMETER);
            KeySpec keySpec = new PBEKeySpec(key.toCharArray(), generateSalatureKey(), ITERATION, KEY_LENGTH);
            SecretKey temporaryKey = secretKeyFactory.generateSecret(keySpec);
            SecretKey secretKey = new SecretKeySpec(temporaryKey.getEncoded(), AES);
            
            return secretKey;
        } 
        catch (NoSuchAlgorithmException ex) 
        {
            Logger.getLogger(AdvanceEncryptionStandard.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (InvalidKeySpecException ex) 
        {
            Logger.getLogger(AdvanceEncryptionStandard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * This function generate the salute key throught a fiexed length and a
     * secure randomize algorithm.
     * @return 
     * @since 0.12
     */
    private byte [] generateSalatureKey()
    {
        byte [] salt = new byte[SALT_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }
    
    /**
     * 
     * @param message
     * @return
     * @since 0.12
     */
    public CryptedMessage encrypt(BigInteger message) 
    {
        try 
        {
            Cipher encrypter = Cipher.getInstance(CIPHER_PARAMETER);
            encrypter.init(Cipher.ENCRYPT_MODE, key);
            AlgorithmParameters algorithmParameters = encrypter.getParameters();
            byte [] iv = algorithmParameters.getParameterSpec(IvParameterSpec.class).getIV();
            
            return new CryptedMessage(encrypter.doFinal(Base64.encodeBase64(message.toByteArray())), iv);
        } 
        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(AdvanceEncryptionStandard.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NoSuchPaddingException ex)
        {
            Logger.getLogger(AdvanceEncryptionStandard.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (InvalidKeyException ex) 
        {
            Logger.getLogger(AdvanceEncryptionStandard.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (InvalidParameterSpecException ex) 
        {
            Logger.getLogger(AdvanceEncryptionStandard.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalBlockSizeException ex)
        {
            Logger.getLogger(AdvanceEncryptionStandard.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (BadPaddingException ex)
        {
            Logger.getLogger(AdvanceEncryptionStandard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * 
     * @param message
     * @param iv
     * @return
     * @since 0.12
     */
    public BigInteger decrypt(BigInteger message, BigInteger iv) 
    {
        try 
        {
            Cipher decrypter = Cipher.getInstance(CIPHER_PARAMETER);
            decrypter.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.toByteArray()));
            
            return new BigInteger(decrypter.doFinal(Base64.decodeBase64(message.toByteArray())));
        }
        catch (InvalidKeyException ex) 
        {
            Logger.getLogger(AdvanceEncryptionStandard.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (InvalidAlgorithmParameterException ex) 
        {
            Logger.getLogger(AdvanceEncryptionStandard.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IllegalBlockSizeException ex) 
        {
            Logger.getLogger(AdvanceEncryptionStandard.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (BadPaddingException ex) 
        {
            Logger.getLogger(AdvanceEncryptionStandard.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NoSuchAlgorithmException ex) 
        {
            Logger.getLogger(AdvanceEncryptionStandard.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NoSuchPaddingException ex) {
            Logger.getLogger(AdvanceEncryptionStandard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
