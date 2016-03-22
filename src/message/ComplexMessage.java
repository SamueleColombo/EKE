/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Samuele Colombo
 * @since 0.11
 */
public class ComplexMessage extends BaseMessage<List<BaseMessage>>
{
    /**
     * @since 0.11
     */
    private List <BaseMessage> content;
    
    /**
     * 
     * @param messages 
     * @since 0.11
     */
    public ComplexMessage(BaseMessage ... messages)
    {
        content.addAll(Arrays.asList(messages));
    }
    
    /**
     * 
     * @return
     * @since 0.11
     */
    @Override
    public List<BaseMessage> getMessage() 
    {
        return content;
    }

    /**
     * 
     * @param t
     * @since 0.11
     */
    @Override
    public void setMessage(List<BaseMessage> t) 
    {
        this.content = t;
    }
    
}
