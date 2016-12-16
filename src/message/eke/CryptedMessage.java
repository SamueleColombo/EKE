/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.eke;

import java.math.BigInteger;
import message.BigIntegerMessage;
import message.ComplexMessage;

/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 */
public class CryptedMessage extends ComplexMessage
{
    
    /**
     * 
     * @param ek
     * @param iv 
     */
    public CryptedMessage(byte [] ek, byte [] iv)
    {
        super(new BigInteger(ek), new BigInteger(iv));
    }
    
    /**
     * 
     * @return 
     * @since 0.12
     */
    public BigInteger getContent()
    {
         return ((BigIntegerMessage) super.getMessage().get(0)).getMessage();
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
