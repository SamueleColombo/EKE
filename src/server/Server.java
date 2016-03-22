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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Samuele Colombo
 * @since 0.1
 */
public class Server 
{
    /**
     * @since 0.1
     */
    private static int port;   
       
    /**
     * @param args the command line arguments
     * @since 0.1
     */
    public static void main(String[] args) 
    {
        if(args.length != 1) throw new IllegalArgumentException();
        
        // Set the port to listen
        port = parsePort(args[0]);
        
        
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
                // Create a new user
                ServerConnection user = new ServerConnection(client);     
                // Add the user at the pool of client
                pool.add(user);
                // Start the connection
                user.start();
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
