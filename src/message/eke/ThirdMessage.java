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
 * @author Samuele Colombo
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
     * @return 
     * @since 0.12
     */
    public BigInteger getIV()
    {
         return ((BigIntegerMessage) super.getMessage().get(1)).getMessage();
    }
}
