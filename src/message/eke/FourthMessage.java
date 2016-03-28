/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.eke;

import java.math.BigInteger;

/**
 *
 * @author Samuele Colombo
 * @since 0.12
 */
public class FourthMessage extends ThirdMessage
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
    
}
