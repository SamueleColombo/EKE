/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import message.eke.FirstMessage;
import message.eke.SecondMessage;
import message.eke.ThirdMessage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import server.ServerCipher;

/**
 *
 * @author Samuele Colombo
 */
public class EncryptedKeyExchangeTest 
{
    private ClientCipher cc;
    private ServerCipher sc;
    
    
    public EncryptedKeyExchangeTest() 
    {
        this.cc = new ClientCipher();
        this.sc = new ServerCipher();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getFirstMessage method, of class ClientCipher.
     */
    @Test
    public void testGetFirstMessage()
    {
        FirstMessage first = cc.getFirstMessage("PASS");
    }
    
    @Test
    public void testGetSecondMessage()
    {
        FirstMessage first = cc.getFirstMessage("PASS");
        SecondMessage second = sc.getSecondMessage(first, "BOB", "PASS");
    }

    /**
     * Test of getThirdMessage method, of class ClientCipher.
     */
    @Test
    public void testGetThirdMessage()
    {
        FirstMessage first = cc.getFirstMessage("PASS");
        SecondMessage second = sc.getSecondMessage(first, "BOB", "PASS");
        ThirdMessage third = cc.getThirdMessage(second, "PASS");
        
    }

    /**
     * Test of run method, of class ClientCipher.
     */
    @Test
    public void testRun() {
    }
    
}
