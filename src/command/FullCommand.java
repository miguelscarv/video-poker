package command;

import java.util.Arrays;

/**
 * FullCommand class represents a command read from the command file (Debug mode).
 * As a command, it may, in case of command hold and bet, or may not contain numbers.
 * @author Miguel Carvalho, Filipe Ferraz, João Baptista
 */
public class FullCommand {
    private CommandType command;
    private int[] numbers;
    private boolean hasNumbers;

    /**
     * Constructor to initialize full command object.
     * @param command Command type of the full command.
     */
    public FullCommand(CommandType command){
        this.command = command;
        this.hasNumbers = false;
    }

    /**
     * This method adds numbers to a specific command. Used when the command type is Bet or Hold.
     * @param numbers Array with the numbers that are being added to the command. In case of command type Bet, only one number is added.
     */
    public void addNumbers(int[] numbers){
        this.numbers = numbers;
        this.hasNumbers = true;
    }

    /**
     * This method returns the numbers present in the full command.
     * In case of command type Bet, only one number is returned, which represents the amount that will be bet.
     * In case of command type Hold, the numbers returned represent the cards that are going to be hold in that round.
     * @return Array of numbers.
     */
    public int[] getNumbers(){
        return this.numbers;
    }

    /**
     * This method returns either true or false, depending on if the current full command has numbers or not.
     * @return boolean true/false depending on the outcome.
     */
    public boolean hasNumbers(){
        return this.hasNumbers;
    }

    /**
     * This method returns the command type of the current full command.
     * @return Command type of the full command.
     */
    public CommandType getCommand() { return this.command; }

    /**
     * This method computes a valid representation of a full command that can be printed.
     * @return String that uniquely identifies a given full command.
     */
    public String toString() {
        if (hasNumbers){
            return "" + this.command + " with numbers " + Arrays.toString(numbers);
        } else {
            return "" + this.command;
        }
    }
         
}
