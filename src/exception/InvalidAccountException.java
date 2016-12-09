/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Samuele Colombo <s.colombo003@studenti.unibs.it>
 */
public class InvalidAccountException extends Exception {

    /**
     * Creates a new instance of <code>InvalidAccountException</code> without
     * detail message.
     */
    public InvalidAccountException() {
    }

    /**
     * Constructs an instance of <code>InvalidAccountException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidAccountException(String msg) {
        super(msg);
    }
}
