/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import db.Account;

/**
 *
 * @author Samuele Colombo
 * @since 0.2
 */
public class AccountManager 
{
    /**
     * @since 0.2
     */
    private static final String EMPTY_STRING = "Argument is empty";
    
    /**
     * @since 0.2
     */
    private static final String SHORT_STRING = "The string must be greater than 4";
    
    /**
     * 
     */
    private static final String ACCOUNT_EXISTS = "The account already exists";
    
    /**
     * @since 0.2
     */
    private static final String WRONG_ARGUMENTS = "Wrong number of arguments";
    
    /**
     * @since 0.2
     */
    private static String id;
    
    /**
     * @since 0.2
     */
    private static String password;
    
    /**
     * @param args the command line arguments
     * @since 0.2
     */
    public static void main(String[] args) 
    {
        // Check if the number of argument is correct
        if(args.length != 2) throw new IllegalArgumentException(WRONG_ARGUMENTS);
        // Get the account
        id = parseAccount(args[0]);
        // Get the password
        password = parsePassword(args[1]);
        // Save the account
        Account.getInstance().saveAccount(id, password);
    }
    
    /**
     * 
     * @param arg
     * @return 
     * @since 0.2
     */
    private static String parseAccount(String arg)
    {
        // Check if the string is empty
        if(arg.isEmpty()) throw new IllegalArgumentException(EMPTY_STRING);
        // Check if the account id is greather than 4
        if(arg.length() < 4) throw new IllegalArgumentException(SHORT_STRING);
        // Check if the account already exists
        if(Account.getInstance().accountExists(arg)) throw new IllegalArgumentException(ACCOUNT_EXISTS);
        return arg;
    }
    
    /**
     * 
     * @param arg
     * @return 
     */
    private static String parsePassword(String arg)
    {
        if(arg == null || arg.isEmpty()) throw new IllegalArgumentException(EMPTY_STRING);
        // Check if the password is great than 8 character
        if(arg.length() < 8) throw new IllegalArgumentException(SHORT_STRING);
        
        return arg;
    }
    
    
    
}
