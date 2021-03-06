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
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import message.BigIntegerMessage;
import message.ComplexMessage;
import message.StringMessage;

/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 * @since 0.12
 */
public class FirstMessage extends ComplexMessage
{
    /**
     * 
     * @param alice
     * @param ta
     * @param g
     * @param p
     * @param iv 
     * @since 0.12
     */
    public FirstMessage(String alice, BigInteger ta, BigInteger g, BigInteger p, BigInteger iv)
    {
        super(alice, ta, g, p, iv);
    }
    
    /**
     * 
     * @return 
     * @since 0.12
     */
    public String getAlice()
    {
        return ((StringMessage) super.getMessage().get(0)).getMessage();
    }
    
    /**
     * 
     * @return 
     * @since 0.12
     */
    public BigInteger getA()
    {
        return ((BigIntegerMessage) super.getMessage().get(1)).getMessage();
    }
    
    /**
     * 
     * @param password
     * @return 
     * @throws java.security.NoSuchAlgorithmException 
     * @throws javax.crypto.NoSuchPaddingException 
     * @throws java.security.InvalidKeyException 
     * @throws javax.crypto.IllegalBlockSizeException 
     * @throws javax.crypto.BadPaddingException 
     * @throws java.security.InvalidAlgorithmParameterException 
     * @since 0.12
     */
    public BigInteger getT(SecretKey password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException
    {
        // Get A
        BigInteger a = this.getA();
        // Get IV
        BigInteger iv = this.getIV();
        // Decrypt the IV
        return AdvanceEncryptionStandard.decrypt(a, iv, password);
    }
    
    /**
     * 
     * @return 
     * @since 0.12
     */
    public BigInteger getG()
    {
        return ((BigIntegerMessage) super.getMessage().get(2)).getMessage();
    }
    
    /**
     * 
     * @return 
     * @since 0.12
     */
    public BigInteger getP()
    {
        return ((BigIntegerMessage) super.getMessage().get(3)).getMessage();
    }
    
    /**
     * 
     * @return 
     * @since 0.12
     */
    public BigInteger getIV()
    {
        return ((BigIntegerMessage) super.getMessage().get(4)).getMessage();
    }
    

}
