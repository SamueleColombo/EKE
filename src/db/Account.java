/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import aes.AdvanceEncryptionStandard;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
 * This class manages the connection between the program and the SQLite database.
 * This class is a singleton, but it wasn't implemented like that.
 * 
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
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
    private static final String DATABASE_NAME = "accounts.sqlite";
    
    /**
     * @since 0.2
     */
    private static final String DATABASE_RESOURCE = "jdbc:sqlite:.\\" + DATABASE_NAME;
    
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
    
    /*
    * @since 0.2
    */
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Account (id TEXT PRIMARY KEY,password TEXT)";
        
    
    /**
     * @since 0.2
     */
    private Account() 
    {
        File file = new File(DATABASE_NAME);
        
        if(!file.exists())
        {
            try 
            {
                // Create a new database file
                file.createNewFile();
                // Load the class for name
                Class.forName(DRIVER);
                // Initialize the connection with the database
                Connection connection = DriverManager.getConnection(DATABASE_RESOURCE);
                // Create a new statement
                Statement statement = connection.createStatement();
                // Execute the current query
                statement.executeUpdate(CREATE_TABLE);
                // Close the statement
                statement.close();
                // Close the connection
                connection.close();
                
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (ClassNotFoundException ex) 
            {
                Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
     * Check if an account exists.
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
     * Get the shared key from the account id (username).
     * 
     * @param id
     * @return 
     * @since 0.2
     */
    public String getSharedKey(String id)
    {
        String password = null;
        
        try 
        {
            // Load the class for name
            Class.forName(DRIVER);
            // Initialize the connection with the database
            Connection connection = DriverManager.getConnection(DATABASE_RESOURCE);
            // Create a new statement
            Statement statement = connection.createStatement();
            // Build the select query
            String query = String.format(SELECT_ACCOUNT, id);
            // Execute the current query
            ResultSet result = statement.executeQuery(query);    
            
            while(result.next())
            {
                // Return the password field
                password = result.getString(PASSWORD);
                // Exit from the loop
                break;
            }
            // Close the statement
            statement.close();
            // Close the connection
            connection.close();
        } 
        catch (SQLException | ClassNotFoundException ex) 
        {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Return the password (could be empty)
        return password;
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
            // Load the class for name
            Class.forName(DRIVER);
            // Initialize the connection with the database
            Connection connection = DriverManager.getConnection(DATABASE_RESOURCE);
            // Disable the auto commit property
            connection.setAutoCommit(false);
            // Create a new statement
            Statement statement = connection.createStatement();
            // Retrieve the SecretKey object from plaintext password
            SecretKey secret = AdvanceEncryptionStandard.generateKey(password);
            // Buil the insert query (and encode the shared key)
            String query = String.format(INSERT_ACCOUNT, id, Base64.encodeBase64String(secret.getEncoded()));
            // Execute the query
            statement.executeUpdate(query);    
            // Close the statement
            statement.close();
            // Commit the operations 
            connection.commit();
            // Close the connection
            connection.close();

        } 
        catch (SQLException | ClassNotFoundException | NoSuchAlgorithmException | InvalidKeySpecException ex) 
        {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
