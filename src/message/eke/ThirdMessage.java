/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.eke;

import aes.AdvanceEncryptionStandard;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import message.BigIntegerMessage;
import message.ComplexMessage;

/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 * @since 0.12
 */
public class ThirdMessage extends ComplexMessage
{
     /**
     * 
     * @param ek
     * @param iv
     * @since 0.12
     */
    public ThirdMessage(BigInteger ek, BigInteger iv)
    {
        super(ek, iv);
    }
    
    /**
     * 
     * @return 
     * @since 0.12
     */
    public BigInteger getEk()
    {
         return ((BigIntegerMessage) super.getMessage().get(0)).getMessage();
    }
    
    /**
     * 
     * @param password
     * @throws java.security.NoSuchAlgorithmException 
     * @throws javax.crypto.NoSuchPaddingException 
     * @throws java.security.InvalidKeyException 
     * @throws javax.crypto.IllegalBlockSizeException 
     * @throws javax.crypto.BadPaddingException 
     * @throws java.security.InvalidAlgorithmParameterException 
     * @throws java.security.spec.InvalidKeySpecException 
     * @return
     * @since 0.12
     */
    public BigInteger getC1(SecretKey password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException
    {
        // Get the decrypted token c1 + c2
        BigInteger c = AdvanceEncryptionStandard.decrypt(this.getEk(), this.getIV(), password);
        // Cast to string
        String s = c.toString();
        // Get the first length - 512 bytes
        return new BigInteger(s.substring(0, s.length() - 154));
    }
    
    /**
     * 
     * @param password
     * @throws java.security.NoSuchAlgorithmException 
     * @throws javax.crypto.NoSuchPaddingException 
     * @throws java.security.InvalidKeyException 
     * @throws javax.crypto.IllegalBlockSizeException 
     * @throws javax.crypto.BadPaddingException 
     * @throws java.security.InvalidAlgorithmParameterException 
     * @throws java.security.spec.InvalidKeySpecException 
     * @return 
     * @since 0.12
     */
    public BigInteger getC2(SecretKey password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException
    {
        // Get the decrypted token c1 + c2
        BigInteger c = AdvanceEncryptionStandard.decrypt(this.getEk(), this.getIV(), password);
        // Cast to string
        String s = c.toString();
        // Get the last 512 bytes
        return new BigInteger(s.substring(s.length() - 154, s.length()));
    }
    
     /**
     * 
     * @return 
     * @since 0.12
     */
    public BigInteger getIV()
    {
         return ((BigIntegerMessage) super.getMessage().get(1)).getMessage();
    }
}
