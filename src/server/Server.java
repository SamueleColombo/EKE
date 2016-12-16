/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 * @since 0.1
 */
public class Server 
{   
    /**
     * @since 0.21
     */
    private static final String USAGE_COMMAND = "Usage: java -cp EKE.jar <port>\n\t-DmyProp.logLevel=FINE for more information\n";
    
    /**
     * @since 0.21
     */
    private static final String EMPTY_PORT = " - No port selected\n";
    
    /**
     * @since 0.21
     */
    private static final String NAN_PORT = " - The port needs to be an integer number\n";
    
    /**
     * @since 0.21
     */
    private static final String PORT_ERROR = " - The selected port value (%d) is not between %d and %d\n";
    
    /**
     * @since 0.21
     */
    private static final String SERVER_RUNNING = "The server is running";
    
    /**
     * @since 0.21
     */
    private static final String CLIENT_DISCONNECTED = "The client is disconnected";
    
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
        try
        {
            // Check the number of arguments
            if(args.length != 1) throw new IllegalArgumentException(USAGE_COMMAND);
            // Set the port to listen
            port = parsePort(args[0]);
            // Server is running
            System.out.println(SERVER_RUNNING);
            
        }
        catch(NumberFormatException ex)
        {
            System.err.println(ex.getMessage());
            System.exit(0);
        }
        catch(IllegalArgumentException ex)
        {
            System.err.println(ex.getMessage());
            System.exit(0);
        }
        
                
        // Set the right handler
        CONSOLE.setLevel(Level.FINE);
        CONSOLE.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
        
        // Initialize the pool of clients
        Set <ServerConnection> pool = new HashSet <> ();

        try
        {
            // Lanch the server socket
            ServerSocket serverSocket = new ServerSocket(port);

            while(true)
            {
                try
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
                catch(Exception ex)
                {
                    if(ex instanceof SocketException)
                    {
                        System.err.println(CLIENT_DISCONNECTED);
                    }
                    else
                    {
                        //
                    }
                }
            }
        } 
        catch(IllegalArgumentException ex)
        {
            System.err.println(ex.getMessage());
            System.exit(0);
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
        // Check if the port is null or empty
        if(arg == null || arg.trim().isEmpty()) throw new IllegalArgumentException(USAGE_COMMAND + EMPTY_PORT);
        
        try
        {
            // Check if the parse operation is possible
            Integer.parseUnsignedInt(arg);
        }
        catch(NumberFormatException ex)
        {
            throw new NumberFormatException(USAGE_COMMAND + NAN_PORT);
        }
        
        // Parse the port into unsigned int
        int p = Integer.parseUnsignedInt(arg);
        // Check if the port is contained between 0 and 65536
        if(p < 0 || p > 65536) throw new NumberFormatException(USAGE_COMMAND + String.format(PORT_ERROR, p, 0, 65536));
        
        return p;
    }
    
}
