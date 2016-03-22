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
public class ByteMessage extends BaseMessage <Byte>
{
    /**
     * @since 0.11
     */
    private Byte content;
    
    /**
     * 
     * @param content 
     * @since 0.11
     */
    public ByteMessage(Byte content)
    {
        this.content = content;
    }
    
    /**
     * 
     * @param content 
     * @since 0.11
     */
    public ByteMessage(byte content)
    {
        this.content = content;
    }
    
    /**
     * 
     * @return 
     * @since 0.11
     */
    @Override
    public Byte getMessage() 
    {
        return content;
    }

    /**
     * 
     * @param t 
     * @since 0.11
     */
    @Override
    public void setMessage(Byte t) 
    {
        this.content = t;
    }


}
