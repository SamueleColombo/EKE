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
import message.StringMessage;

/**
 *
 * @author Samuele Colombo
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
     * @since 0.12
     */
    public BigInteger getT(String password)
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
