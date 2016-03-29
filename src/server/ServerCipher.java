/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import aes.AdvanceEncryptionStandard;
import dh.DiffieHelman;
import exception.WrongChallengeException;
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
     * @since 0.12
     */
    public ServerCipher() 
    {
        //
    }
    
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
     * @param first
     * @param bob
     * @param password
     * @return 
     * @since 0.12
     */
    public SecondMessage getSecondMessage(FirstMessage first, String bob, String password)
    {     
        // Get the P from Alice
        BigInteger p = first.getP();
        // Get the G from Alice
        BigInteger g = first.getG();
        // Get the IV of the message
        BigInteger iv = first.getIV();
        // Decrypt the message (A) and retrive ta := g^sa mod p
        BigInteger ta = first.getT(password);
        // Initialize secure random class
        SecureRandom random = new SecureRandom();
        // Generate a new token c1
        this.c1 = new BigInteger(512, random);
        // Initialize Diffie Helman class
        DiffieHelman dh = new DiffieHelman(p, g);
        // Get the sb
        BigInteger sb = dh.getS();
        // Calculate K := g^(sa * sb) mod p = (g^sa mod p)^sb mod p = ta ^ sb mod p
        BigInteger k = ta.modPow(sb, p);
        // Concatenate g^sb mod p with c1
        BigInteger tbc1 = new BigInteger(dh.getT().toString() + c1.toString());
        // Encrypt tbc1
        CryptedMessage b = AdvanceEncryptionStandard.encrypt(tbc1, password);
        // Send the new message
        return new SecondMessage(bob, b.getContent(), b.getIV());

    }
    
    /**
     * 
     * @param third
     * @param password
     * @return 
     * @throws exception.WrongChallengeException 
     * @since 0.12
     */
    public FourthMessage getFourthMessage(ThirdMessage third, String password) throws WrongChallengeException
    {
        // Get C1 from Alice
        BigInteger c1Alice = third.getC1(password);
        // If the challenge doesn't match with the correct c1 throws a new exception
        if(!c1.equals(c1Alice)) throw new WrongChallengeException();
        // Get C2 from Alice
        BigInteger c2 = third.getC2(password);
        // Encrypt the new message
        CryptedMessage message = AdvanceEncryptionStandard.encrypt(c2, password);
        // Create the new message
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
            FirstMessage first = (FirstMessage) input.readObject();
            // Compute the second message
            SecondMessage second = getSecondMessage(first, Server.id, Server.password);
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
        catch (WrongChallengeException ex) 
        {
            Logger.getLogger(ServerCipher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    
}
