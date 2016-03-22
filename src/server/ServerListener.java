/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private PrintWriter output;
    
    /**
     * @since 0.1
     */
    private BufferedReader input;
    
    /**
     * @since 0.1
     */
    private ServerConnection connection;

    /**
     * @param socket
     * @param connection
     * @throws java.io.IOException
     * @since 0.1
     */
    public ServerListener(Socket socket, ServerConnection connection) throws IOException 
    {
        this.socket = socket;
        this.output = new PrintWriter(socket.getOutputStream());
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
                String message = this.input.readLine();
                // Break the loop if the string is null
                if(message == null) break;
                // Add the message at the queue
                connection.getSender().addMessage(message);
            }
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
