/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import aes.AdvanceEncryptionStandard;
import dh.DiffieHelman;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.eke.CryptedMessage;
import message.eke.FirstMessage;
import message.eke.FourthMessage;
import message.eke.SecondMessage;
import message.eke.ThirdMessage;

/**
 *
 * @author Samuele Colombo
 * @since 0.12
 */
public class ClientCipher extends Thread
{
    /**
     * @since 0.12
     */
    private ObjectInputStream input;
    
    /**
     * @since 0.12
     */
    private ObjectOutputStream output;
    
    /**
     * @since 0.12
     */
    private DiffieHelman dh;
    
    /**
     * @since 0.12
     */
    private AdvanceEncryptionStandard aes;
    
    /**
     * 
     * @param input
     * @param output 
     * @since 0.12
     */
    public ClientCipher(ObjectInputStream input, ObjectOutputStream output)
    {
        this.input = input;
        this.output = output;
        this.dh = new DiffieHelman();
        this.aes = new AdvanceEncryptionStandard(Client.password);
    }
    
    /**
     * 
     * @return 
     * @since 0.12
     */
    public FirstMessage getFirstMessage()
    {
        CryptedMessage aiv = aes.encrypt(dh.getT());
        
        return new FirstMessage(
                Client.id,              // Alice
                aiv.getContent(),       // A
                dh.getG(),              // g
                dh.getP(),              // p
                aiv.getIV());           // iv
    }
    
    /**
     * 
     * @param second
     * @return 
     */
    public ThirdMessage getThirdMessage(SecondMessage second)
    {
        BigInteger b;
        BigInteger c2 = null;
        BigInteger c1 = null;
        BigInteger iv = null;
        BigInteger ek = aes.decrypt(new BigInteger(c1.toString() + c2.toString()), iv);
        return new ThirdMessage(ek, iv);
        
    }
    
    /**
     * @since 0.12
     */
    @Override
    public void run()
    {
        try 
        {
            // Create the first message
            FirstMessage first = getFirstMessage();
            // Send the first message
            output.writeObject(first);
            // Receive the second message
            SecondMessage second = (SecondMessage) input.readObject();
            // Compute the third message
            ThirdMessage third = getThirdMessage(second);
            // Send the third message
            output.writeObject(third);
            // Receive the fourth message
            FourthMessage fourth = (FourthMessage) input.readObject();
            // Interrupt this thread
            interrupt();
            
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ClientCipher.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(ClientCipher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
