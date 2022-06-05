package match;

import java.util.Arrays;

public class FullCommand {
    public CommandType command;
    private int[] numbers;
    private boolean hasNumbers;

    public FullCommand(CommandType command){
        this.command = command;
        this.hasNumbers = false;
    }

    public void addNumbers(int[] numbers){
        this.numbers = numbers;
        this.hasNumbers = true;
    }

    public int[] getNumbers(){
        return this.numbers;
    }

    public boolean hasNumbers(){
        return this.hasNumbers;
    }

    public String toString() {
        if (hasNumbers){
            return "" + this.command + " with numbers " + Arrays.toString(numbers);
        } else {
            return "" + this.command;
        }
    }
         
}