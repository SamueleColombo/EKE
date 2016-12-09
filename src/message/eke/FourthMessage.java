/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.eke;

import aes.AdvanceEncryptionStandard;
import java.math.BigInteger;
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
     */
    public BigInteger getC2(String password)
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
