/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.util.UUID;

/**
 *
 * @author Samuele Colombo
 * @param <T>
 * @since 0.11
 */
public abstract class BaseMessage<T>
{
    /**
     * @since 0.11
     */
    private final String id;
        
    /**
     * @since 0.11
     */
    public BaseMessage()
    {
        this.id = UUID.randomUUID().toString();
    }
    
    /**
     * 
     * @return
     * @since 0.11
     */
    public abstract T getMessage();
    
    /**
     * 
     * @param t 
     * @since 0.11
     */
    public abstract void setMessage(T t);
}
