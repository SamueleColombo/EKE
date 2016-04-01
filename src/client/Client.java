/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import aes.AdvanceEncryptionStandard;
import db.Account;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import javax.crypto.SecretKey;
import message.BaseMessage;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Samuele Colombo
 * @since 0.1
 */
public class Client 
{
    /**
     * @since 0.12
     */
    public static String id;
   
    /**
     * @since 0.12
     */
    public static String password;
    
    /**
     * @since 0.1
     */
    public static String host;
    
    /**
     * @since 0.1
     */
    public static int port;
    
    /**
     * @since 0.12
     */
    public static final Logger CONSOLE = Logger.getLogger(Client.class.getName());
    
    
    
    /**
     * @param args the command line arguments
     * @since 0.1
     */
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) 
    {
        // Check if the number of arguments is right
        if(args.length != 4) throw new IllegalArgumentException();

        // Get and parse the host
        host = args[0];
        
        // Retrieve the port from args
        port = server.Server.parsePort(args[1]);
        
        // Set the current client id
        id = parseAccount(args[2]);
        
        // Check and set the password
        password = parsePassword(id, args[3]);     
        
        // Set the right handler
        CONSOLE.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
        
        try
        {
            // Create the socket
            Socket socket = new Socket(host, port);
            // Initialize the output stream
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            // Clean the buffer
            output.flush();
            // Initialize the input stream
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            // Enstabilish a secure connection
            ClientCipher cipher = new ClientCipher(input, output);
            // Start the secure connection
            cipher.start();
            
            while (!cipher.isInterrupted());
            
            ClientSender sender = new ClientSender(output);
            sender.setDaemon(true);
            sender.start();
            
            BaseMessage message;
            
            while((message = (BaseMessage) input.readObject()) != null)
            {
                System.out.println(message.getMessage());               
            }
            
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * 
     * @param id
     * @param arg
     * @return 
     * @since 0.12
     */
    public static String parsePassword(String id, String arg)
    {
        // Check if the password is null or empty
        if(arg == null || arg.isEmpty()) throw new IllegalArgumentException();
        // Check if the password is great than 8 character
        if(arg.length() < 8) throw new IllegalArgumentException();
        // Generate the secretKey with AES
        SecretKey secretKey = AdvanceEncryptionStandard.generateKey(arg);
        // Encode the secretKey to String
        String encodedKey = Base64.encodeBase64String(secretKey.getEncoded());
        // Get the shared key inside the database
        String sharedKey = Account.getInstance().getSharedKey(id);
        // Check if the encoded and the shared key are equals 
        if(!encodedKey.equals(sharedKey)) throw new IllegalArgumentException();
        
        return arg;
    }
    
    /**
     * 
     * @param arg
     * @return 
     * @since 0.2
     */
    private static String parseAccount(String arg)
    {
        if(!Account.getInstance().accountExists(arg)) throw new IllegalArgumentException();
        
        return arg;
    }
    
}
