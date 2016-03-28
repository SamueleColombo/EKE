/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

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
public class ServerCipher extends Thread
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
    //private DiffieHelman dh;
    
    /**
     * @since 0.12
     */
    //private AdvanceEncryptionStandard aes;
    
    /**
     * @since 0.12
     */
    private BigInteger c1;
        
    /**
     * 
     * @param connection
     * @since 0.12
     */
    public ServerCipher(ServerConnection connection)
    {
        this.input = connection.getInputStream();
        this.output = connection.getOutputStream();
    }
    
    /**
     * 
     * @param bob
     * @param password
     * @param first
     * @return 
     * @since 0.12
     */
    public SecondMessage getSecondMessage(String bob, String password, FirstMessage first)
    {
        SecureRandom random = new SecureRandom();
        this.c1 = new BigInteger(64, random);
        AdvanceEncryptionStandard aes = new AdvanceEncryptionStandard(password);
        DiffieHelman dh = new DiffieHelman(first.getP(), first.getG());
        BigInteger sb = dh.getS();
        BigInteger k = first.getG().modPow(sb, first.getP());
        CryptedMessage b = aes.encrypt(new BigInteger(first.getG().modPow(sb, first.getP()).toString() + c1.toString()));
        return new SecondMessage(bob, b.getContent(), b.getIV());
    }
    
    /**
     * 
     * @param third
     * @param password
     * @return 
     * @since 0.12
     */
    public FourthMessage getFourthMessage(ThirdMessage third, String password)
    {
        AdvanceEncryptionStandard aes = new AdvanceEncryptionStandard(password);
        BigInteger c1c2 = aes.decrypt(third.getEk(), third.getIV());
        String s1s2 = c1c2.toString();
        BigInteger c2 = new BigInteger(s1s2.substring(c1.bitCount()));
        CryptedMessage message = aes.encrypt(c2); 
        return new FourthMessage(message.getContent(), message.getIV());
        
    }
    
  
      
    /**
     * @since 0.12
     */
    @Override
    public void run()
    {
        try 
        {
            // Receive the first message
            FirstMessage fist = (FirstMessage) input.readObject();
            // Compute the second message
            SecondMessage second = getSecondMessage(Server.id, Server.password, fist);
            // Send the second message
            output.writeObject(second);
            // Receive the third message
            ThirdMessage third = (ThirdMessage) input.readObject();
            // Compute the fourth message
            FourthMessage fourth = getFourthMessage(third, Server.password);
            // Sende the fourth message
            output.writeObject(fourth);
            // Interrupt this thread
            interrupt();
            
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ServerCipher.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(ServerCipher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    
}
