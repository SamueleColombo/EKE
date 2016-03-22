/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.util.List;

/**
 *
 * @author Samuele Colombo
 */
public class ComplexMessage extends BaseMessage<List<BaseMessage>>
{

    @Override
    public List<BaseMessage> getMessage() 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMessage(List<BaseMessage> t) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
