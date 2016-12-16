/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

/**
 * This class provides a simple interface to create new accounts.
 * 
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 * @since 0.2
 */
public class AccountManager 
{
    /**
     * @since 0.2
     */
    private static final String COMMAND_USAGE = "Usage: java -cp EKE.jar db.AccountManager <username> <password>\n";
    
    /**
     * @since 0.2
     */
    private static final String EMPTY_STRING = " - The %s is empty\n";
    
    /**
     * @since 0.2
     */
    private static final String SHORT_STRING = " - The %s must be greater than %d\n";
    
    /**
     * @since 0.21
     */
    private static final String ACCOUNT_EXISTS = " - This username already exists\n";
    
    /**
     * @since 0.21
     */
    private static final String USERNAME = "username";
    
    /**
     * @since 0.21
     */
    private static final String PASSWORD = "password";
    
    /**
     * @since 0.2
     */
    private static final String WRONG_ARGUMENTS = " - Wrong number of arguments (%d found)\n";
    
    /**
     * @since 0.21
     */
    private static final String ACCOUNT_CREATED = "The account %s is been created\n";
    
    /**
     * @since 0.2
     */
    private static String id;
    
    /**
     * @since 0.2
     */
    private static String password;
    
    /**
     * This method provides the functionality to create a new user.
     * 
     * @param args the command line arguments
     * @since 0.21
     */
    public static void main(String[] args) 
    {
        try
        {
            // Check if the number of argument is correct
            if(args.length != 2) throw new IllegalArgumentException(COMMAND_USAGE + String.format(WRONG_ARGUMENTS, args.length));
            // Get the account
            id = parseAccount(args[0]);
            // Get the password
            password = parsePassword(args[1]);
            // Save the account
            Account.getInstance().saveAccount(id, password);
            // Print the message
            System.out.println(String.format(ACCOUNT_CREATED, id));
        }
        catch(IllegalArgumentException ex)
        {
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     * This method validates the account.
     * 
     * @param arg
     * @return 
     * @since 0.2
     */
    private static String parseAccount(String arg) throws IllegalArgumentException
    {
        // Check if the string is empty
        if(arg.trim().isEmpty()) throw new IllegalArgumentException(COMMAND_USAGE + String.format(EMPTY_STRING, USERNAME));
        // Check if the account id is greather than 4
        if(arg.length() < 4) throw new IllegalArgumentException(COMMAND_USAGE + String.format(SHORT_STRING, USERNAME, 4));
        // Check if the account already exists
        if(Account.getInstance().accountExists(arg)) throw new IllegalArgumentException(COMMAND_USAGE + ACCOUNT_EXISTS);
        return arg;
    }
    
    /**
     * This method validates the password.
     * 
     * @param arg
     * @return 
     */
    private static String parsePassword(String arg) throws IllegalArgumentException
    {
        if(arg == null || arg.trim().isEmpty()) throw new IllegalArgumentException(COMMAND_USAGE + String.format(EMPTY_STRING, PASSWORD));
        // Check if the password is great than 8 character
        if(arg.length() < 8) throw new IllegalArgumentException(COMMAND_USAGE + String.format(SHORT_STRING, PASSWORD, 8));
        // Return the password
        return arg;
    }    
    
}
