/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Samuele Colombo
 * @since 0.1
 */
public class ServerSender extends Thread
{
    /**
     * @since 0.1
     */
    private Queue <String> queue;
    
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
    private ServerConnection connection;
    
    /**
     * @param socket
     * @param connection
     * @throws java.io.IOException
     * @since 0.1
     */
    public ServerSender(Socket socket, ServerConnection connection) throws IOException
    {
        this.queue = new ConcurrentLinkedQueue <> ();
        this.socket = socket;
        this.connection = connection;
        this.output = new PrintWriter(socket.getOutputStream());
    }
    
    /**
     * 
     * @param message 
     * @since 0.1
     */
    public synchronized void addMessage(String message)
    {
        queue.add(message);
        notify();
    }
    
    /**
     * 
     * @return
     * @throws InterruptedException 
     * @since 0.1
     */
    private synchronized String getNextMessage() throws InterruptedException
    {
        // Wait until the queue is empty
        while(queue.isEmpty()) wait();
        // Get the next message and remove it from the queue
        String message = queue.remove();
        // Return the message
        return message;
    }
    
    /**
     * 
     * @param message 
     * @since 0.1
     */
    private void sendMessage(String message)
    {
        output.println(message);
        output.flush();
    }

    /**
     * @since 0.1
     */
    @Override
    public void run()
    {
       try
       {
           while(!interrupted())
           {
               String message = getNextMessage();
               sendMessage(message);
           }
       } 
       catch (InterruptedException ex) 
       {
            Logger.getLogger(ServerSender.class.getName()).log(Level.SEVERE, null, ex);
       }
       finally
       {
           connection.getListener().interrupt();
           connection.getSender().interrupt();
       }
    }
}
