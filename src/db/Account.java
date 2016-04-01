/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import aes.AdvanceEncryptionStandard;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Samuele Colombo
 * @since 0.2
 */
public class Account 
{
    /**
     * @since 0.2
     */
    private static final String DRIVER = "org.sqlite.JDBC";
    
    /**
     * @since 0.2
     */
    private static final String DATABASE = "jdbc:sqlite:.\\database\\accounts.sqlite";
    
    /**
     * @since 0.2
     */
    private static final String PASSWORD = "password";
    
    /*
    * @since 0.2
    */
    private static final String SELECT_ACCOUNT = "SELECT password FROM Account WHERE id='%s';";
    
    /**
     * @since 0.2
     */
    private static final String INSERT_ACCOUNT = "INSERT INTO Account (id,password) VALUES('%s', '%s');";
        
    
    /**
     * @since 0.2
     */
    private Account() 
    {

    }
    
    /**
     * 
     * @return 
     * @since 0.2
     */
    public static Account getInstance() 
    {
        return AccountHolder.INSTANCE;
    }
    
    /**
     * @since 0.2
     */
    private static class AccountHolder 
    {

        private static final Account INSTANCE = new Account();
    }
    
    /**
     * 
     * @param id 
     * @return 
     * @since 0.2
     */
    public boolean accountExists(String id)
    {
        return (getSharedKey(id) != null);
    }
    
    /**
     * 
     * @param id
     * @return 
     */
    public String getSharedKey(String id)
    {
        try 
        {
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            String query = String.format(SELECT_ACCOUNT, id);
            ResultSet result = statement.executeQuery(query);    
            
            while(result.next())
            {
                return result.getString(PASSWORD);
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * 
     * @param id
     * @param password
     */
    public void saveAccount(String id, String password)
    {
        try 
        {
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(DATABASE);
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            SecretKey secret = AdvanceEncryptionStandard.generateKey(password);
            String query = String.format(INSERT_ACCOUNT, id, Base64.encodeBase64String(secret.getEncoded()));
            statement.executeUpdate(query);    
            statement.close();
            connection.commit();
            connection.close();

        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
