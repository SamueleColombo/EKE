/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.Socket;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Samuele Colombo
 * @since 0.1
 */
public class ServerDispatcher implements Runnable
{
    /**
     * @since 0.1
     */
    private Queue <String> messages;
    
    /**
     * @since 0.1
     */
    private Set <ServerConnection> connections;
    
    /**
     * @since 0.1
     */
    public ServerDispatcher()
    {
        messages = new ConcurrentLinkedQueue <> ();
        connections = new HashSet <> ();
    }
    
    /**
     * @param connection
     * @param message
     * @since 0.1
     */
    public synchronized void dispatchMessage(ServerConnection connection, String message)
    {
        if(connections.contains(connection))
        {
            messages.add(message);
            notify();
        }
    }
    
    /**
     * 
     * @return
     * @throws InterruptedException 
     * @since 0.1
     */
    public synchronized String getNextMessage() throws InterruptedException
    {
        // Wait until the queue is empty
        while(messages.isEmpty()) wait();
        // Get the next message and remove it from the queue
        String message = messages.remove();
        // Return the message
        return message;
    }
    
    /**
     * 
     * @param user
     * @since 0.1
     */
    public synchronized void addClient(ServerConnection user)
    {
        connections.add(user);
    }
    
    /**
     * 
     * @param user 
     * @since 0.1
     */
    public synchronized void removeClient(ServerConnection user)
    {
        connections.remove(user);
    }
    /*
    public synchronized String getNextMessage()
    {
        
    }*/
    
    /**
     * @since 0.1
     */
    @Override
    public void run() 
    {

    }
}
