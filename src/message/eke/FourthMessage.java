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

/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 * @since 0.12
 */
public class FourthMessage extends ComplexMessage
{
    /**
     * 
     * @param ek
     * @param iv 
     * @since 0.12
     */
    public FourthMessage(BigInteger ek, BigInteger iv)
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
     * @return 
     * @throws java.security.NoSuchAlgorithmException 
     * @throws javax.crypto.NoSuchPaddingException 
     * @throws java.security.InvalidKeyException 
     * @throws javax.crypto.IllegalBlockSizeException 
     * @throws javax.crypto.BadPaddingException 
     * @throws java.security.InvalidAlgorithmParameterException 
     * @throws java.security.spec.InvalidKeySpecException 
     */
    public BigInteger getC2(String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException
    {
        // Get the decrypted challenge c2
        return AdvanceEncryptionStandard.decrypt(this.getEk(), this.getIV(), password);
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
