/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 * @since 0.1
 */
public class ServerConnection 
{    
    /**
     * @since 0.1
     */
    private Socket socket;
    
    /**
     * @since 0.1
     */
    private ServerListener listener;
    
    /**
     * @since 0.11
     */
    private ObjectOutputStream output;
    
    /**
     * @since 0.1
     */
    private ServerSender sender;
    
    /**
     * @since 0.11
     */
    private ObjectInputStream input;
    
    /**
     * 
     * @param socket
     * @throws java.io.IOException
     * @since 0.1
     */
    public ServerConnection(Socket socket) throws IOException
    {
        this.socket = socket;
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
        this.listener = new ServerListener(socket, output, input, this);
        this.sender = new ServerSender(socket, output, this);
    }
    
    /**
     * 
     * @param socket
     * @param listener
     * @param sender
     * @since 0.1
     * @deprecated 
     */
    public ServerConnection(Socket socket, ServerListener listener, ServerSender sender)
    {
        this.socket = socket;
        this.listener = listener;
        this.sender = sender;
    }
    
    /**
     * @since 0.1
     */
    public void start()
    {
        this.listener.start();
        this.sender.start();
    }

    /**
     * 
     * @param listener 
     * @since 0.1
     */
    public void setListener(ServerListener listener)
    {
        this.listener = listener;
    }

    /**
     * 
     * @return
     * @since 0.1
     */
    public ServerListener getListener()
    {
        return listener;
    }
    
    /**
     * 
     * @param sender 
     * @since 0.1
     */
    public void setSender(ServerSender sender) 
    {
        this.sender = sender;
    }

    /**
     * 
     * @return
     * @since 0.1
     */
    public ServerSender getSender()
    {
        return sender;
    }

    /**
     * 
     * @return 
     * @since 0.12
     */
    public ObjectInputStream getInputStream() 
    {
        return input;
    }

    /**
     * 
     * @return 
     * @since 0.12
     */
    public ObjectOutputStream getOutputStream() 
    {
        return output;
    }

}
