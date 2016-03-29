/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

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
    
    /*
    * @since 0.12
    */
    private BigInteger c2;
        
    /**
     * @since 0.12
     */
    public ClientCipher()
    {
        this.dh = new DiffieHelman();
    }
    
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
    }
    
    /**
     * 
     * @param password
     * @return 
     * @since 0.12
     */
    public FirstMessage getFirstMessage(String password)
    {
        // Encrypt the message A := Ew (g^Sa mod p)
        CryptedMessage aiv = AdvanceEncryptionStandard.encrypt(dh.getT(), password);
        // Alice
        String alice = "A";
        // Get the A attribute
        BigInteger a = aiv.getContent();
        // Get the g attribute
        BigInteger g = dh.getG();
        // Get the p attribute
        BigInteger p = dh.getP();
        // Get the iv from AES
        BigInteger iv = aiv.getIV();
        // Create the message
        return new FirstMessage(alice, a, g, p, iv); 
    }
    
    /**
     * 
     * @param second
     * @param password
     * @return 
     */
    public ThirdMessage getThirdMessage(SecondMessage second, String password)
    {
        // Get C1 from B
        BigInteger c1 = second.getC1(password);
        // Get Tb from B (Tb := g^Sb mod p)
        BigInteger tb = second.getT(password);
        // Initialize the SecureRandom class
        SecureRandom random = new SecureRandom();
        // Generate a random number
        this.c2 = new BigInteger(512, random);
        // Calculate K := g^(Sb * Sa) mod p = Tb ^ Sa mod p
        BigInteger k = tb.modPow(dh.getS(), dh.getP());
        // Concatenate c1 + c2
        BigInteger c1c2 = new BigInteger(c1.toString() + c2.toString());
        // Encrypt message
        CryptedMessage message = AdvanceEncryptionStandard.encrypt(c1c2, password);
        // Create the message
        return new ThirdMessage(message.getContent(), message.getIV());
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
            FirstMessage first = getFirstMessage(Client.password);
            // Send the first message
            output.writeObject(first);
            // Receive the second message
            SecondMessage second = (SecondMessage) input.readObject();
            // Compute the third message
            ThirdMessage third = getThirdMessage(second, Client.password);
            // Send the third message
            output.writeObject(third);
            // Receive the fourth message
            FourthMessage fourth = (FourthMessage) input.readObject();
            // Check if the challenge matches with the correct c2
            if(!fourth.getC2(Client.password).equals(c2)) throw new WrongChallengeException();
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
        catch (WrongChallengeException ex) 
        {
            Logger.getLogger(ClientCipher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
