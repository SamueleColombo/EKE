/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 *
 * @author Samuele Colombo
 * @since 0.1
 */
public class Server 
{
    /*
    * @since 0.12
    */
    public static String id;
    
    /**
     * @since 0.1
     */
    private static int port;   
    
    /**
     * Used for testing purpose, it will be removed.
     * @since 0.12
     */
    public static String password;
    
    /**
     * @since 0.12
     */
    public static final Logger CONSOLE = Logger.getLogger(Server.class.getName());
               
    /**
     * @param args the command line arguments
     * @since 0.1
     */
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) 
    {
        if(args.length != 2) throw new IllegalArgumentException();
        
        // Set the current id
        id = UUID.randomUUID().toString();
        
        // Set the port to listen
        port = parsePort(args[0]);
        
        // Set the debug password
        password = args[1];
        
        // Set the right handler
        CONSOLE.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
        
        try
        {
            // Lanch the server socket
            ServerSocket serverSocket = new ServerSocket(port);
            
            // Initialize the pool of client
            Set <ServerConnection> pool = new HashSet <> ();
            
            while(true)
            {
                // Accept a new connection
                Socket client = serverSocket.accept();
                // Create a new connection
                ServerConnection connection = new ServerConnection(client);     
                // Add the user at the pool of client
                pool.add(connection);
                // Create a secure connection
                ServerCipher cipher = new ServerCipher(connection);
                // Start the EKE algorithm
                cipher.start();
                // Wait until the connection is built up
                while(!cipher.isInterrupted());
                // Start the connection
                connection.start();
            }
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * 
     * @param arg
     * @return 
     * @since 0.1
     */
    public static int parsePort(String arg)
    {
        int p = Integer.parseUnsignedInt(arg);
        
        if(p < 0 || p > 65536) throw new NumberFormatException();
        
        return p;
    }
    
}
