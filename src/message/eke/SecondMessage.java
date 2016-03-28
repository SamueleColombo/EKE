/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.eke;

import java.math.BigInteger;
import message.BigIntegerMessage;
import message.ComplexMessage;
import message.StringMessage;

/**
 *
 * @author Samuele Colombo
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
     * @return 
     * @since 0.12
     */
    public BigInteger getIV()
    {
        return ((BigIntegerMessage) super.getMessage().get(2)).getMessage();
    }
}
