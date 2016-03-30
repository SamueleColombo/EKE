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
        // Send to logger the p variable
        Server.CONSOLE.log(Level.INFO, "[p] : " + p.toString());
        // Get the G from Alice
        BigInteger g = first.getG();
        // Send to logger the g variable
        Server.CONSOLE.log(Level.INFO, "[g] : " + g.toString());
        // Get the IV of the message
        BigInteger iv = first.getIV();
        // Send to logger the iv variable
        Server.CONSOLE.log(Level.INFO, "[iv] : " + iv.toString());
        // Decrypt the message (A) and retrive ta := g^sa mod p
        BigInteger ta = first.getT(password);
        // Send to logger the ta variable
        Server.CONSOLE.log(Level.INFO, "[Ta] : " + ta.toString());
        // Initialize secure random class
        SecureRandom random = new SecureRandom();
        // Generate a new token c1
        this.c1 = new BigInteger(512, random);
        // Send to logger the c1 variable
        Server.CONSOLE.log(Level.INFO, "[c1] : " + c1.toString());
        // Initialize Diffie Helman class
        DiffieHelman dh = new DiffieHelman(p, g);
        // Get the sb
        BigInteger sb = dh.getS();
        // Send to logger the Sb variable
        Server.CONSOLE.log(Level.INFO, "[Sb] : " + sb.toString());
        // Calculate K := g^(sa * sb) mod p = (g^sa mod p)^sb mod p = ta ^ sb mod p
        BigInteger k = ta.modPow(sb, p);
        // Send to logger the K variable
        Server.CONSOLE.log(Level.INFO, "[K] : " + k.toString());
        // Concatenate g^sb mod p with c1
        BigInteger tbc1 = new BigInteger(dh.getT().toString() + c1.toString());
        // Send to logger the tbc1 variable
        Server.CONSOLE.log(Level.INFO, "[tbc1] : " + k.toString());
        // Encrypt tbc1
        CryptedMessage b = AdvanceEncryptionStandard.encrypt(tbc1, password);
        // Send to logger the b variable
        Server.CONSOLE.log(Level.INFO, "[b] : " + b.getContent().toString());
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
        // Send to logger the c1 variable
        Server.CONSOLE.log(Level.INFO, "[c1] : " + c1Alice.toString());
        // If the challenge doesn't match with the correct c1 throws a new exception
        if(!c1.equals(c1Alice)) throw new WrongChallengeException();
        // Get C2 from Alice
        BigInteger c2 = third.getC2(password);
        // Send to logger the c2 variable
        Server.CONSOLE.log(Level.INFO, "[c2] : " + c2.toString());
        // Encrypt the new message
        CryptedMessage message = AdvanceEncryptionStandard.encrypt(c2, password);
        // Send to logger the message variable
        Server.CONSOLE.log(Level.INFO, "[Ek(c2)] : " + message.getContent().toString());
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
            // Send to logger that the first message is received
            Server.CONSOLE.info("First message is received");
            // Compute the second message
            SecondMessage second = getSecondMessage(first, Server.id, Server.password);
            // Send the second message
            output.writeObject(second);
            // Flush the second message
            output.flush();
            // Send to logger that the second message is sent
            Server.CONSOLE.info("Second message is sent");
            // Receive the third message
            ThirdMessage third = (ThirdMessage) input.readObject();
            // Send to logger that the third message is received
            Server.CONSOLE.info("Third message is received");
            // Compute the fourth message
            FourthMessage fourth = getFourthMessage(third, Server.password);
            // Send the fourth message
            output.writeObject(fourth);
            // Flush the fourth message
            output.flush();
            // Send to logger that the fourth message is sent
            Server.CONSOLE.info("Fourth message is sent");
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
