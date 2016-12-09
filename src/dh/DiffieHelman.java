/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dh;

import java.math.BigInteger;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidParameterSpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.spec.DHParameterSpec;

/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 * @since 0.12
 */
public class DiffieHelman 
{
    /**
     * @since 0.12
     */
    private DHParameterSpec parameters;
    
    /**
     * @since 0.12
     */
    private BigInteger s;
    
    /**
     * @since 0.12
     */
    private BigInteger t;
    
    /**
     * @since 0.12
     */
    public DiffieHelman()
    {
        try 
        {
            // Initialize the secure random object
            AlgorithmParameterGenerator algorithmParameterGenerator = AlgorithmParameterGenerator.getInstance("DH");
            algorithmParameterGenerator.init(512);
            AlgorithmParameters algorithmParameters = algorithmParameterGenerator.generateParameters();
            this.parameters = algorithmParameters.getParameterSpec(DHParameterSpec.class);
            
        } 
        catch (NoSuchAlgorithmException ex) 
        {
            Logger.getLogger(DiffieHelman.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (InvalidParameterSpecException ex) 
        {
            Logger.getLogger(DiffieHelman.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * @param p
     * @param g 
     */
    public DiffieHelman(BigInteger p, BigInteger g)
    {
        this.parameters = new DHParameterSpec(p, g);
    }
    
    /**
     * 
     * @return
     * @since 0.12
     */
    public BigInteger getP()
    {
        return parameters.getP();
    }
    
    /**
     * 
     * @return
     * @since 0.12
     */
    public BigInteger getG()
    {
        return parameters.getG();
    }
    
    /**
     * 
     * @return
     * @since 0.12
     */
    public BigInteger getS()
    {
        if(this.s == null)
        {
            SecureRandom random = new SecureRandom();
            s = BigInteger.probablePrime(parameters.getP().bitLength(), random);
        }
        
        return s;
    }
    
    /**
     * 
     * @return 
     * @since 0.12
     */
    public BigInteger getT()
    {
        if(this.t == null)
        {
            this.t = parameters.getG().modPow(getS(), parameters.getP());
        }
        
        return t;
    }
}
