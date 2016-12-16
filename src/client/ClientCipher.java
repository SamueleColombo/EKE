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
import message.eke.CryptedMessage;
import message.eke.ErrorMessage;
import message.eke.FirstMessage;
import message.eke.FourthMessage;
import message.eke.SecondMessage;
import message.eke.ThirdMessage;


/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 * @since 0.12
 */
public class ClientCipher extends Thread
{
    /**
     * This message is shown when something went wrong in the authentication.
     * @since 0.2
     */
    private static final String AUTH_FAILED = "Authentication is failed";
    
    
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
     * @since 0.2
     */
    private boolean connected = false;
        
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
     * @throws java.security.NoSuchAlgorithmException 
     * @throws javax.crypto.NoSuchPaddingException 
     * @throws java.security.InvalidKeyException 
     * @throws java.security.spec.InvalidParameterSpecException 
     * @throws javax.crypto.IllegalBlockSizeException 
     * @throws javax.crypto.BadPaddingException 
     * @throws java.security.spec.InvalidKeySpecException 
     * @since 0.12
     */
    public FirstMessage getFirstMessage(String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException
    {

        // Send to logger the ta variable
        Client.CONSOLE.log(Level.INFO, "[Ta] : {0}", dh.getT());
        // Encrypt the message A := Ew (g^Sa mod p)
        CryptedMessage aiv = AdvanceEncryptionStandard.encrypt(dh.getT(), password);
        // Alice
        String alice = Client.id;
        // Get the A attribute
        BigInteger a = aiv.getContent();
        // Send to logger the a variable
        Client.CONSOLE.log(Level.INFO, "[a] : {0}", a.toString());
        // Get the g attribute
        BigInteger g = dh.getG();
        // Send to logger the g variable
        Client.CONSOLE.log(Level.INFO, "[g] : {0}", g.toString());
        // Get the p attribute
        BigInteger p = dh.getP();
        // Send to logger the p variable
        Client.CONSOLE.log(Level.INFO, "[p] : {0}", p.toString());
        // Get the iv from AES
        BigInteger iv = aiv.getIV();
        // Send to logger the a variable
        Client.CONSOLE.log(Level.INFO, "[iv] : {0}", iv.toString());
        // Create the message
        return new FirstMessage(alice, a, g, p, iv);
    }
    
    /**
     * 
     * @param second
     * @param password
     * @return 
     * @throws java.security.NoSuchAlgorithmException 
     * @throws javax.crypto.NoSuchPaddingException 
     * @throws java.security.InvalidKeyException 
     * @throws java.security.spec.InvalidParameterSpecException 
     * @throws javax.crypto.IllegalBlockSizeException 
     * @throws javax.crypto.BadPaddingException 
     * @throws java.security.spec.InvalidKeySpecException 
     * @throws java.security.InvalidAlgorithmParameterException 
     */
    public ThirdMessage getThirdMessage(SecondMessage second, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException
    {

        BigInteger b = second.getB(password);
        Client.CONSOLE.log(Level.INFO, "[V] : {0}", b.toString());
        // Get C1 from B
        BigInteger c1 = second.getC1(password);
        // Send to logger the c1 variable
        Client.CONSOLE.log(Level.INFO, "[c1] : {0}", c1.toString());
        // Get Tb from B (Tb := g^Sb mod p)
        BigInteger tb = second.getT(password);
        // Send to logger the tb variable
        Client.CONSOLE.log(Level.INFO, "[Tb] : {0}", tb.toString());

        // Generate a random number
        this.c2 = generateRandomChallenge();
        // Send to logger the c2 variable
        Client.CONSOLE.log(Level.INFO, "[c2] : {0}", c2.toString());
        // Calculate K := g^(Sb * Sa) mod p = Tb ^ Sa mod p
        BigInteger k = tb.modPow(dh.getS(), dh.getP());
        // Send to logger the K variable
        Client.CONSOLE.log(Level.INFO, "[K] : {0}", k.toString());
        // Concatenate c1 + c2
        BigInteger c1c2 = new BigInteger(c1.toString() + c2.toString());
        // Send to logger the c1c2 variable
        Client.CONSOLE.log(Level.INFO, "[c1c2] : {0}", c1c2.toString());
        // Encrypt message
        CryptedMessage message = AdvanceEncryptionStandard.encrypt(c1c2, password);
        // Send to logger the c1 variable
        Client.CONSOLE.log(Level.INFO, "[Ek(c1,c2)] : {0}", message.getContent().toString());
        // Create the message
        return new ThirdMessage(message.getContent(), message.getIV());

    }
    
    /**
     * This method generate a random <code>BigInteger</code> with 512 bytes.
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
            // Create the first message
            FirstMessage first = getFirstMessage(Client.password);
            // Send the first message
            output.writeObject(first);
            // Flush the first message
            output.flush();
            // Send to logger that the second message is sent
            Client.CONSOLE.info("First message is sent");
            
            // Receive the second message
            Object secondObject= input.readObject();
            // Check if is an error message
            if(secondObject instanceof ErrorMessage)
            {
                System.err.println(((ErrorMessage) secondObject).getMessage());
                System.exit(0);
            }
            
            
            SecondMessage second = (SecondMessage) secondObject;
            // Send to logger that the second message is received
            Client.CONSOLE.info("Second message is received");
            
            
            // Compute the third message
            ThirdMessage third = getThirdMessage(second, Client.password);
            // Send the third message
            output.writeObject(third);
            // Flush the third message
            output.flush();
            // Send to logger that the third message is sent
            Client.CONSOLE.info("Third message is sent");
            
            // Receive the fourth message
            Object fourthObject = input.readObject();
            // Check if is an error message
            if(fourthObject instanceof ErrorMessage)
            {
                System.err.println(((ErrorMessage) fourthObject).getMessage());
                System.exit(0);
            }
            
            FourthMessage fourth = (FourthMessage) fourthObject;
            // Send to logger that the fourth message is received
            Client.CONSOLE.info("Fourth message is received");
            // Check if the challenge matches with the correct c2
            if(!fourth.getC2(Client.password).equals(c2)) 
            {
                throw new WrongChallengeException();
            }
            else
            {
                setConnected(true);
                System.out.println("Authenticated!");
            }
            
            
            // Interrupt this thread
            interrupt();
            
        }  
        catch (IOException | WrongChallengeException | InvalidAlgorithmParameterException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidParameterSpecException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException ex) 
        {
            //Logger.getLogger(ClientCipher.class.getName()).log(Level.SEVERE, null, ex);
            
            try 
            {
                System.err.println(ex.getMessage());
                output.writeObject(new ErrorMessage(ex.getMessage()));
                output.close();
 
            } 
            catch (IOException ex1) 
            {
                Logger.getLogger(ClientCipher.class.getName()).log(Level.SEVERE, null, ex1);
            }
            finally
            {
                System.exit(0);
            }
            
        } 
        finally
        {
            // Interrupt this thread
            interrupt();
        }
    }

    /**
     * 
     * @return 
     * @since 0.2
     */
    public boolean isConnected() 
    {
        return connected;
    }

    /**
     * 
     * @param connected 
     * @since 0.2
     */
    public void setConnected(boolean connected) 
    {
        this.connected = connected;
    }

}
