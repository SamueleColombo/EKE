/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

/**
 *
 * @author Samuele Colombo
 * @since 0.11
 */
public class StringMessage extends BaseMessage <String>
{
    private String content;
    
    /**
     * 
     * @return 
     * @since 0.11
     */
    @Override
    public String getMessage() 
    {
        return content;
    }

    /**
     * 
     * @param t
     * @since 0.11
     */
    @Override
    public void setMessage(String t) 
    {
        this.content = t;
    }
    
}
