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
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import javax.crypto.SecretKey;
import message.BaseMessage;
import message.StringMessage;
import org.apache.commons.codec.binary.Base64;

/**
 * The class <code>Client</code> is used to connect to a specific server using 
 * the (command line) arguments.
 * 
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 * @since 0.1
 */
public class Client 
{
    /**
     * @since 0.21
     */
    private static final String COMMAND_USAGE = "Usage: java -cp EKE.jar client.Client <host> <port> <username> <password>\n";
    
    /**
     * @since 0.21
     */
    private static final String EMPTY_STRING = " - The %s is empty\n";
    
    /**
     * @since 0.21
     */
    private static final String SHORT_STRING = " - The %s must be greater than %d\n";
    
    /**
     * @since 0.21
     */
    private static final String WRONG_ARGUMENTS = " - Wrong number of arguments (%d found)\n";
    
    /**
     * @since 0.21
     */
    private static final String ACCOUNT_EXISTS = " - This username already exists\n";
    
    /**
     * @since 0.21
     */
    private static final String WRONG_PASSWORD = " - The password is not correct\n";
   
    /**
     * @since 0.21
     */
    private static final String SERVER_DISCONNECTED = "The server is disconnected";
    
    /**
     * @since 0.21
     */
    private static final String STREAM_ERROR = "An error occurs on server or its stream";
    
    /**
     * @since 0.21
     */
    private static final String CLASS_ERROR = "Received message is not correctly formatted";
    
    /**
     * @since 0.21
     */
    private static final String USERNAME = "username";
    
    /**
     * @since 0.21
     */
    private static final String PASSWORD = "password";
    
    /**
     * The client's username.
     * @since 0.12
     */
    public static String id;
   
    /**
     * The client's password.
     * @since 0.12
     */
    public static String password;
    
    /**
     * The client's hostname.
     * @since 0.1
     */
    public static String host;
    
    /**
     * The client's port.
     * @since 0.1
     */
    public static int port;
    
    /**
     * This Logger provides information about EKE algorithm.
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
        try
        {
            // Check if the number of arguments is right
            if(args.length != 4) throw new IllegalArgumentException(COMMAND_USAGE + String.format(WRONG_ARGUMENTS, args.length));

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
        }
        catch(IllegalArgumentException | NoSuchAlgorithmException | InvalidKeySpecException ex)
        {
            System.err.println(ex.getMessage());
            System.exit(0);
        } 
        
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
            
            if(cipher.isConnected())
            {
                ClientSender sender = new ClientSender(output);
            
                sender.setDaemon(true);
                sender.start();

                BaseMessage message;

                while((message = (BaseMessage) input.readObject()) != null)
                {
                    if(message instanceof StringMessage) System.out.println(message.getMessage());               
                }   
            }
            
        } 
        catch(SocketException ex)
        {
            System.err.println(SERVER_DISCONNECTED);
            System.exit(0);
        }
        catch (IOException ex) 
        {
            System.err.println(STREAM_ERROR);
            System.exit(0);
        } 
        catch (ClassNotFoundException ex) 
        {
            System.err.println(CLASS_ERROR);
            System.exit(0);
        }
        
    }
    
    /**
     * This method validates a password by username.
     * 
     * @param id
     * @param arg
     * @return 
     * @throws java.security.NoSuchAlgorithmException 
     * @throws java.security.spec.InvalidKeySpecException 
     * @since 0.12
     */
    public static String parsePassword(String id, String arg) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        // Check if the password is null or empty
        if(arg == null || arg.isEmpty()) throw new IllegalArgumentException(COMMAND_USAGE + String.format(EMPTY_STRING,PASSWORD));
        // Check if the password is great than 8 character
        if(arg.length() < 8) throw new IllegalArgumentException(COMMAND_USAGE + String.format(SHORT_STRING,PASSWORD, 8));
        // Generate the secretKey with AES
        SecretKey secretKey = AdvanceEncryptionStandard.generateKey(arg);
        // Encode the secretKey to String
        String encodedKey = Base64.encodeBase64String(secretKey.getEncoded());
        // Get the shared key inside the database
        //String sharedKey = Account.getInstance().getSharedKey(id);
        // Check if the encoded and the shared key are equals 
        //if(!encodedKey.equals(sharedKey)) throw new IllegalArgumentException(COMMAND_USAGE + WRONG_PASSWORD);
        
        return arg;
    }
    
    /**
     * This method validates an account by username.
     * 
     * @param arg
     * @return 
     * @since 0.2
     */
    private static String parseAccount(String arg)
    {
        // Check if the account is null or empty
        if(arg == null || arg.isEmpty()) throw new IllegalArgumentException(COMMAND_USAGE + String.format(EMPTY_STRING, USERNAME));
        // Check if the username is great than 4 character
        if(arg.length() < 4) throw new IllegalArgumentException(COMMAND_USAGE + String.format(SHORT_STRING, USERNAME, 4));        
        //if(!Account.getInstance().accountExists(arg)) throw new IllegalArgumentException(COMMAND_USAGE + String.format(ACCOUNT_EXISTS,PASSWORD));
        
        return arg;
    }
    
}
