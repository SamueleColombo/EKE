/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package token;

import java.math.BigInteger;
import java.util.Arrays;
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
public class TokenTest {
    
    public TokenTest() {
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

    @Test
    public void token1()
    {
        BigInteger k = new BigInteger("6131762828498162435671090448347241115677869069998905394029694178927984960720478256062687112896429305060294482937989344728615325534716025498613620930755417");
        BigInteger c = new BigInteger("4383699974273736262259779001846676945356035331666526651865759176033944079436753830630696184046508256075430164528983863029044802684570920686731127655601408");
        BigInteger t = new BigInteger("61317628284981624356710904483472411156778690699989053940296941789279849607204782560626871128964293050602944829379893447286153255347160254986136209307554174383699974273736262259779001846676945356035331666526651865759176033944079436753830630696184046508256075430164528983863029044802684570920686731127655601408");
        String sc = t.toString().substring(t.toString().length() - 154);
        String sk = t.toString().substring(0, t.toString().length() - 154);
        BigInteger cn = new BigInteger(sc);
        BigInteger kn = new BigInteger(sk);
        assertEquals(c, cn);
        assertEquals(k, kn);
        
    }
    
    public void token2()
    {
        BigInteger c = new BigInteger("1279838184047896764062193464556545787227529895987831062463551342924685996320296050130800258614468425611694664594071081084938657825868919878719047591393350");
        BigInteger t = new BigInteger("50786500028844870757230774604803654818131608415783003527120144681356342085982572899416728380497661455649624162263683473501174627590418736775348790719367191279838184047896764062193464556545787227529895987831062463551342924685996320296050130800258614468425611694664594071081084938657825868919878719047591393350");
        String s = t.toString().substring(t.toString().length() - 154);
        BigInteger n = new BigInteger(s);
        assertEquals(c, n);
    }
}
