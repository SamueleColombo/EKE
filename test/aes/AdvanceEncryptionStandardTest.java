/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aes;

import dh.DiffieHelman;
import java.math.BigInteger;
import message.eke.CryptedMessage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Samuele Colombo
 */
public class AdvanceEncryptionStandardTest {
    
    public AdvanceEncryptionStandardTest() {
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
     * Test of decrypt method, of class AdvanceEncryptionStandard.
     */
    @Test
    public void test1()
    {
        BigInteger p = new BigInteger("1150008348720760279936685026530572613417100392499438957796812687932819409472289390765704916224374941745123454838903882406194573500218647967854990262782789");
        BigInteger g = new BigInteger("8099165633552332475777118857334083268184937315013697421591598798902395792621484333438325287174730929180172105354816437952532788318979598767954413965386849");
        DiffieHelman dha = new DiffieHelman(p, g);
        BigInteger sa = dha.getS();
        BigInteger ta = dha.getT();
        
        CryptedMessage cm = AdvanceEncryptionStandard.encrypt(ta, "PASSWORD");
        
        DiffieHelman dhb = new DiffieHelman(p, g);
        BigInteger sb = dhb.getS();
        BigInteger tb = dhb.getT();
        
        BigInteger tab = AdvanceEncryptionStandard.decrypt(cm.getContent(), cm.getIV(), "PASSWORD");
        
        if(tab.equals(ta))
        {
            
        }
        
    }


    
}
