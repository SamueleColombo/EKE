/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 * @since 0.12
 */
public class WrongChallengeException extends Exception {

    /**
     * Creates a new instance of <code>WrongChallengeException</code> without
     * detail message.
     * @since 0.12
     */
    public WrongChallengeException() 
    {
        //
    }

    /**
     * Constructs an instance of <code>WrongChallengeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     * @since 0.12
     */
    public WrongChallengeException(String msg) 
    {
        super(msg);
    }
}
