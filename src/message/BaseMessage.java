/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 * @param <T>
 * @since 0.11
 */
public abstract class BaseMessage<T> implements Serializable
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
