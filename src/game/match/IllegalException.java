package game.match;

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
