package match;

public class IllegalException extends Exception{

    public IllegalException(String s){
        super(s + ": illegal command");
    }

    public IllegalException(){
        super("b: illegal amount");
    }

}
