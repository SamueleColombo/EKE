/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.math.BigInteger;

/**
 *
 * @author Samuele Colombo
 * @since 0.11
 */
public class BigIntegerMessage extends BaseMessage <BigInteger>
{
    /**
     * @since 0.11
     */
    private BigInteger content;
    
    /**
     * 
     * @return
     * @since 0.11
     */
    @Override
    public BigInteger getMessage() 
    {
        return content;
    }

    /**
     * 
     * @param t
     * @since 0.11
     */
    @Override
    public void setMessage(BigInteger t) 
    {
        this.content = t;
    }
    
}
