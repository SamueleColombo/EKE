/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Samuele Colombo
 * @since 0.1
 */
public class Client 
{
    /**
     * @since 0.1
     */
    private static String host;
    
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
        // Check if the number of arguments is right
        if(args.length != 2) throw new IllegalArgumentException();
        
        // Retrieve the port from args
        port = server.Server.parsePort(args[1]);
        
        
        try
        {
            Socket socket = new Socket(args[0], port);
            
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream());
            
            ClientSender sender = new ClientSender(output);
            sender.setDaemon(true);
            sender.start();
            
            String message;
            
            while((message = input.readLine()) != null)
            {
                System.out.println(message);
            }
            
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
