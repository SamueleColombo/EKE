/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.BaseMessage;

/**
 *
 * @author Samuele Colombo
 * @since 0.1
 */
public class ServerListener extends Thread
{
    /**
     * @since 0.1
     */
    private Socket socket;
        
    /**
     * @since 0.1
     */
    private ObjectOutputStream output;
    
    /**
     * @since 0.1
     */
    private ObjectInputStream input;
    
    /**
     * @since 0.1
     */
    private ServerConnection connection;

    /**
     * @param socket
     * @param output
     * @param input
     * @param connection
     * @throws java.io.IOException
     * @since 0.1
     */
    public ServerListener(Socket socket, ObjectOutputStream output, ObjectInputStream input, ServerConnection connection) throws IOException 
    {
        this.socket = socket;
        this.output = output;
        this.input = input;
        this.connection = connection;
    }
    
    /**
     * @since 0.1
     */
    @Override
    @SuppressWarnings("empty-statement")
    public void run()
    {
        try
        {
            while(!isInterrupted())
            {
                // Read the current line
                BaseMessage message = (BaseMessage) input.readObject();
                // Break the loop if the string is null
                if(message == null) break;
                // Add the message at the queue
                connection.getSender().addMessage(message);
            }
        } 
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SocketException ex)
        {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
            connection.getListener().interrupt();
            connection.getSender().interrupt();
        }
         catch (IOException ex)
        {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally
        {
            connection.getListener().interrupt();
            connection.getSender().interrupt();
        }
    
    }
    
}
