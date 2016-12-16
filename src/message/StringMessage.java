/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 * @since 0.11
 */
public class StringMessage extends BaseMessage <String>
{
    /**
     * @since 0.11
     */
    private String content;

    /**
     * 
     * @param content 
     * @since 0.11
     */
    public StringMessage(String content) 
    {
       this.content = content;
    }
    
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
