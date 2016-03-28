/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.BaseMessage;

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
     * @param args the command line arguments
     * @since 0.1
     */
    public static void main(String[] args) 
    {
        // Check if the number of arguments is right
        if(args.length != 3) throw new IllegalArgumentException();
        
        // Set the current client id
        id = UUID.randomUUID().toString();
        
        // Get and parse the host
        host = args[0];
        
        // Retrieve the port from args
        port = server.Server.parsePort(args[1]);
        
        // Check and set the password
        password = parsePassword(args[2]);
        
        try
        {
            Socket socket = new Socket(host, port);
            
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            
            ClientCipher cipher = new ClientCipher(input, output);
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
     * @param arg
     * @return 
     * @since 0.12
     */
    public static String parsePassword(String arg)
    {
        if(arg == null || arg.isEmpty()) throw new IllegalArgumentException();
        
        if(arg.length() > 4) throw new IllegalArgumentException();
        
        return arg;
    }
    
}
