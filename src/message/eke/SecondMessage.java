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
import message.BigIntegerMessage;
import message.ComplexMessage;
import message.StringMessage;

/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 * @since 0.12
 */
public class SecondMessage extends ComplexMessage
{
    /**
     * 
     * @param bob
     * @param b
     * @param iv
     * @since 0.12
     */
    public SecondMessage(String bob, BigInteger b, BigInteger iv)
    {
        super(bob, b, iv);
    }
    
    /**
     * 
     * @return 
     * @since 0.12
     */
    public String getBob()
    {
        return ((StringMessage) super.getMessage().get(0)).getMessage();
    }
    
    /**
     * 
     * @return 
     * @since 0.12
     */
    public BigInteger getB()
    {
        return ((BigIntegerMessage) super.getMessage().get(1)).getMessage();
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
    public BigInteger getB(String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException
    {
        // Get B
        BigInteger b = this.getB();
        // Get IV
        BigInteger iv = this.getIV();
        // Decrypt the IV
        return AdvanceEncryptionStandard.decrypt(b, iv, password);
    }
    
    /**
     * 
     * @param t
     * @param sa
     * @param p
     * @return 
     * @since 0.12
     */
    public BigInteger getK(BigInteger t, BigInteger sa, BigInteger p)
    {        
        return t.modPow(sa, p);
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
     */
    public BigInteger getT(String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException
    {
        // Return g^sb mod p + c1
        BigInteger b = this.getB(password);
        // Cast to string
        String stringB = b.toString();
        // Split and return C1
        return new BigInteger(stringB.substring(0, stringB.length() - 154));
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
     */
    public BigInteger getC1(String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException
    {
        // Return g^sb mod p + c1
        BigInteger b = this.getB(password);
        // Cast to string
        String stringB = b.toString();
        // Split and return C1
        return new BigInteger(stringB.substring(stringB.length() - 154));
    }
    
    /**
     * 
     * @return 
     * @since 0.12
     */
    public BigInteger getIV()
    {
        return ((BigIntegerMessage) super.getMessage().get(2)).getMessage();
    }
}
