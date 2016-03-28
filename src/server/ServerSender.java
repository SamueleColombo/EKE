/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.BaseMessage;

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
    private Queue <BaseMessage> queue;
    
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
    private ServerConnection connection;
    
    /**
     * @param socket
     * @param output
     * @param connection
     * @throws java.io.IOException
     * @since 0.1
     */
    public ServerSender(Socket socket, ObjectOutputStream output, ServerConnection connection) throws IOException
    {
        this.queue = new ConcurrentLinkedQueue <> ();
        this.socket = socket;
        this.connection = connection;
        this.output = output;
    }
    
    /**
     * 
     * @param message 
     * @since 0.1
     */
    public synchronized void addMessage(BaseMessage message)
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
    private synchronized BaseMessage getNextMessage() throws InterruptedException
    {
        // Wait until the queue is empty
        while(queue.isEmpty()) wait();
        // Get the next message and remove it from the queue
        BaseMessage message = queue.remove();
        // Return the message
        return message;
    }
    
    /**
     * 
     * @param message 
     * @since 0.1
     */
    private void sendMessage(BaseMessage message) throws IOException
    {
        output.writeObject(message);
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
               BaseMessage message = getNextMessage();
               sendMessage(message);
           }
       } 
       catch (InterruptedException ex) 
       {
            Logger.getLogger(ServerSender.class.getName()).log(Level.SEVERE, null, ex);
       } 
       catch (IOException ex) 
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
