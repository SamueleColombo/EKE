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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Samuele Colombo
 * @since 0.1
 */
public class ClientSender extends Thread
{
    /**
     * @since 0.1
     */
    private PrintWriter output;
    
    /**
     * 
     * @param output 
     * @since 0.1
     */
    public ClientSender(PrintWriter output)
    {
        this.output = output;
    }
    
    /**
     * @since 0.1
     */
    @Override
    public void run()
    {
        try
        {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            
            while(!isInterrupted())
            {
                String message = input.readLine();
                output.println(message);
                output.flush();
            }
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ClientSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
