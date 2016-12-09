/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
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
     * @param objects 
     * @since 0.11
     */
    public ComplexMessage(Object ... objects)
    {
        content = new ArrayList <> ();
        
        for(Object obj : objects)
        {
            if(obj instanceof String)
            {
                content.add(new StringMessage((String) obj));
            }
            else if(obj instanceof BigInteger)
            {
                content.add(new BigIntegerMessage((BigInteger) obj));
            }
            else
            {
                throw new IllegalArgumentException();
            }
        }
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
