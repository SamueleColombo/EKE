/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message.eke;

import message.StringMessage;

/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 * @since 0.12
 */
public class ErrorMessage extends StringMessage
{
    /**
     * 
     * @param content 
     * @since 0.12
     */
    public ErrorMessage(String content) 
    {
        super(content);
    }
    
}
