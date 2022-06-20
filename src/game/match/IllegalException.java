package game.match;

/**
 * IllegalException class represents the Exceptions that can be thrown.
 * @author Miguel Carvalho, Filipe Ferraz, Joao Baptista
 */
public class IllegalException extends Exception{

    /**
     * Exception thrown from introducing an invalid command
     * @param s Illegal command
     */
    public IllegalException(String s){
        super(s + ": illegal command");
    }

    /**
     * Exception thrown from betting an invalid amount
     */
    public IllegalException(){
        super("b: illegal amount");
    }

}
