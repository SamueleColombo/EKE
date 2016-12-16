/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import aes.AdvanceEncryptionStandard;
import client.Client;
import db.Account;
import dh.DiffieHelman;
import exception.InvalidAccountException;
import exception.WrongChallengeException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import message.eke.CryptedMessage;
import message.eke.ErrorMessage;
import message.eke.FirstMessage;
import message.eke.FourthMessage;
import message.eke.SecondMessage;
import message.eke.ThirdMessage;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
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
     * @since 0.2
     */
    private SecretKey shared;
    
    /**
     * @since 1.0
     */
    private boolean connected = false;
    
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
     * @return 
     * @throws java.security.NoSuchAlgorithmException 
     * @throws javax.crypto.NoSuchPaddingException 
     * @throws java.security.InvalidKeyException 
     * @throws javax.crypto.IllegalBlockSizeException 
     * @throws javax.crypto.BadPaddingException 
     * @throws java.security.InvalidAlgorithmParameterException 
     * @throws java.security.spec.InvalidKeySpecException 
     * @throws exception.InvalidAccountException 
     * @throws java.security.spec.InvalidParameterSpecException 
     * @since 0.12
     */
    public SecondMessage getSecondMessage(FirstMessage first, String bob) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException, InvalidAccountException, InvalidParameterSpecException
    {     
        // Get Alice
        String alice = first.getAlice();
        // Check if alice is a valid account
        if(!Account.getInstance().accountExists(alice)) throw new InvalidAccountException();
        // Get the string version of the shared key
        String stringKey = Account.getInstance().getSharedKey(alice);
        // Cast the shared key into byte
        byte [] encodedKey = Base64.decodeBase64(stringKey);
        // Save the alice's shared key
        this.shared = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        // Get the P from Alice
        BigInteger p = first.getP();
        // Send to logger the p variable
        Server.CONSOLE.log(Level.INFO, "[p] : {0}", p.toString());
        // Get the G from Alice
        BigInteger g = first.getG();
        // Send to logger the g variable
        Server.CONSOLE.log(Level.INFO, "[g] : {0}", g.toString());
        // Get the IV of the message
        BigInteger iv = first.getIV();
        // Send to logger the iv variable
        Server.CONSOLE.log(Level.INFO, "[iv] : {0}", iv.toString());
        // Decrypt the message (A) and retrive ta := g^sa mod p
        BigInteger ta = first.getT(shared);
       
        
        // Send to logger the ta variable
        Server.CONSOLE.log(Level.INFO, "[Ta] : {0}", ta.toString());
        // Generate a new token c1
        this.c1 = generateRandomChallenge();
        // Send to logger the c1 variable
        Server.CONSOLE.log(Level.INFO, "[c1] : {0}", c1.toString());
        // Initialize Diffie Helman class
        DiffieHelman dh = new DiffieHelman(p, g);
        // Get the sb
        BigInteger sb = dh.getS();
        // Send to logger the Sb variable
        Server.CONSOLE.log(Level.INFO, "[Sb] : {0}", sb.toString());
        // Calculate K := g^(sa * sb) mod p = (g^sa mod p)^sb mod p = ta ^ sb mod p
        BigInteger k = ta.modPow(sb, p);
        // Send to logger the K variable
        Server.CONSOLE.log(Level.INFO, "[K] : {0}", k.toString());
        // Concatenate g^sb mod p with c1
        BigInteger tbc1 = new BigInteger(dh.getT().toString() + c1.toString());
        // Send to logger the tbc1 variable
        Server.CONSOLE.log(Level.INFO, "[tbc1] : {0}", k.toString());
        // Encrypt tbc1
        CryptedMessage b = AdvanceEncryptionStandard.encrypt(tbc1, shared);
        // Send to logger the b variable
        Server.CONSOLE.log(Level.INFO, "[b] : {0}", b.getContent().toString());
        // Send the new message
        return new SecondMessage(bob, b.getContent(), b.getIV());

    }
    
    /**
     * 
     * @param third
     * @return 
     * @throws java.security.NoSuchAlgorithmException 
     * @throws javax.crypto.NoSuchPaddingException 
     * @throws java.security.InvalidKeyException 
     * @throws javax.crypto.IllegalBlockSizeException 
     * @throws javax.crypto.BadPaddingException 
     * @throws java.security.InvalidAlgorithmParameterException 
     * @throws java.security.spec.InvalidKeySpecException 
     * @throws java.security.spec.InvalidParameterSpecException 
     * @throws exception.WrongChallengeException 
     * @since 0.12
     */
    public FourthMessage getFourthMessage(ThirdMessage third) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException, WrongChallengeException, InvalidParameterSpecException
    {
        BigInteger c1c2 = AdvanceEncryptionStandard.decrypt(third.getEk(), third.getIV(), shared);
        Server.CONSOLE.log(Level.INFO, "[c1c2] : {0}", c1c2.toString());
        // Get C1 from Alice
        BigInteger c1Alice = third.getC1(shared);
        // Send to logger the c1 variable
        Server.CONSOLE.log(Level.INFO, "[c1] : {0}", c1Alice.toString());
        // If the challenge doesn't match with the correct c1 throws a new exception
        if(!c1.equals(c1Alice)) throw new WrongChallengeException();
        // Get C2 from Alice
        BigInteger c2 = third.getC2(shared);
        // Send to logger the c2 variable
        Server.CONSOLE.log(Level.INFO, "[c2] : {0}", c2.toString());
        // Encrypt the new message
        CryptedMessage message = AdvanceEncryptionStandard.encrypt(c2, shared);
        // Send to logger the message variable
        Server.CONSOLE.log(Level.INFO, "[Ek(c2)] : {0}", message.getContent().toString());
        // Create the new message
        return new FourthMessage(message.getContent(), message.getIV());
    }
    
    /**
     * 
     * @return 
     * @since 0.12
     */
    private BigInteger generateRandomChallenge()
    {
        // Initialize the SecureRandom class
        SecureRandom random = new SecureRandom();
        // Generate a random number
        BigInteger c = new BigInteger(512, random);
        
        
        try 
        {   // Truncate the random number
            return new BigInteger(c.toString().substring(0, 154));
        }
        catch(StringIndexOutOfBoundsException ex)
        {   
            // Sometimes the secure random could be very massive.
            return generateRandomChallenge();
        }
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
            Object firstObject = input.readObject();
            // Check if the first message is an error one
            if(firstObject instanceof ErrorMessage)
            {
                Server.CONSOLE.info(((ErrorMessage) firstObject).getMessage());
                interrupt();
            }
            
            // Cast the first message
            FirstMessage first = (FirstMessage) firstObject;
            // Send to logger that the first message is received
            Server.CONSOLE.info("First message is received");
            
            // Compute the second message
            SecondMessage second = getSecondMessage(first, "Bob");
            // Send the second message
            output.writeObject(second);
            // Flush the second message
            output.flush();
            // Send to logger that the second message is sent
            Server.CONSOLE.info("Second message is sent");
            
            
            // Receive the first message
            Object thirdObject = input.readObject();
            // Check if the first message is an error one
            if(thirdObject instanceof ErrorMessage)
            {
                Server.CONSOLE.info(((ErrorMessage) thirdObject).getMessage());
                interrupt();
            }
            // Receive the third message
            ThirdMessage third = (ThirdMessage) thirdObject;
            // Send to logger that the third message is received
            Server.CONSOLE.info("Third message is received");
            
            // Compute the fourth message
            FourthMessage fourth = getFourthMessage(third);
            // Send the fourth message
            output.writeObject(fourth);
            // Flush the fourth message
            output.flush();
            // Send to logger that the fourth message is sent
            Server.CONSOLE.info("Fourth message is sent");
            
            // The client is now authenticated
            System.out.println("The client is authenticated");
            this.connected = true;
            
            
        }  
        catch (IOException | ClassNotFoundException | WrongChallengeException | InvalidAccountException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException | InvalidKeySpecException | InvalidParameterSpecException ex) 
        {
            try 
            {
                System.err.println(ex.getMessage());
                
                output.writeObject(new ErrorMessage(ex.getMessage()));
                output.close();
                
            } 
            catch (IOException ex1)
            {
                Logger.getLogger(ServerCipher.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
            //Logger.getLogger(ServerCipher.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally
        {
            // Interrupt this thread
            interrupt();
        }
    }
    
    /**
     * @since 1.0
     * @return 
     */
    public boolean isConnected() 
    {
        return connected;
    }
    
}
