package game.match;

import command.CommandType;
import command.FullCommand;
import player.cards.Card;
import player.cards.Rank;
import player.cards.Suit;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Reader class represents the class responsible for reading the given card and command file.
 * @author Miguel Carvalho, Filipe Ferraz, Joao Baptista
 */
final public class Reader {

    private static List<Card> deck = new ArrayList<Card>();
    private static List<FullCommand> fullCommandList = new ArrayList<FullCommand>();

    /**
     * This method reads the content of the card file in order to create a deck.
     * @param pathToFile Path to the card file
     * @return Deck created from reading the card file
     */
    public static List<Card> readCardFile(String pathToFile){

        String contents = "";

        try {

            File cardFile = new File(pathToFile);
            Scanner myReader = new Scanner(cardFile);

            while (myReader.hasNext()) {
                contents += myReader.nextLine();
            }

            myReader.close();

            contents = contents.replaceAll("\\s+","");
            String[] cardStrings = contents.split("(?<=\\G..)");

            for (String s: cardStrings){
                Card card = getCorrespondingCard(s);
                deck.add(card);
            }

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred reading the card file");
            e.printStackTrace();
        }

        return deck;
    }

    /**
     * This method reads the command file in order to create a list with all the commands.
     * @param pathToFile Path to the command file
     * @return List of commands read from the command file
     */
    public static List<FullCommand> readCommandFile(String pathToFile){

        String contents = "";

        try {

            File cardFile = new File(pathToFile);
            Scanner myReader = new Scanner(cardFile);

            while (myReader.hasNext()) {
                contents += myReader.nextLine();
            }

            myReader.close();

            contents = contents.replaceAll("\\s+","");
            String[] individualCommandArray = contents.split("((?=[/\\D+/g]))");


            for (String uniqueCommand: individualCommandArray){
                FullCommand command = getCorrespondingFullCommand(uniqueCommand);
                fullCommandList.add(command);
            }



        } catch (FileNotFoundException e) {
            System.out.println("An error occurred reading the command file");
            e.printStackTrace();
        }

        return fullCommandList;
    }

    /**
     * This method creates the cards present in the card file.
     * @param s Each card in String format
     * @return Card corresponding to the input String
     */
    private static Card getCorrespondingCard(String s) {
        Rank rank = null;
        Suit suit = null;

        if(s.length() != 2){
            System.out.println("Invalid Card Format");
            System.exit(1);
        }

        switch (s.charAt(0)) {

            case '2':
                rank = Rank.TWO;
                break;
            case '3':
                rank = Rank.THREE;
                break;
            case '4':
                rank = Rank.FOUR;
                break;
            case '5':
                rank = Rank.FIVE;
                break;
            case '6':
                rank = Rank.SIX;
                break;
            case '7':
                rank = Rank.SEVEN;
                break;
            case '8':
                rank = Rank.EIGHT;
                break;
            case '9':
                rank = Rank.NINE;
                break;
            case 'T':
                rank = Rank.TEN;
                break;
            case 'Q':
                rank = Rank.QUEEN;
                break;
            case 'J':
                rank = Rank.JACK;
                break;
            case 'K':
                rank = Rank.KING;
                break;
            case 'A':
                rank = Rank.ACE;
                break;
            default:
                System.out.println("Invalid Card Format");
                System.exit(1);
        }

        switch (s.charAt(1)) {

            case 'S':
                suit = Suit.SPADES;
                break;
            case 'H':
                suit = Suit.HEARTS;
                break;
            case 'C':
                suit = Suit.CLUBS;
                break;
            case 'D':
                suit = Suit.DIAMONDS;
                break;
            default:
                System.out.println("Invalid Card Format");
                System.exit(1);
        }

        Card card = new Card(rank, suit);
        return card;
    }

    /**
     * This method creates the commands present in the command file.
     * @param s Each command in String format
     * @return FullCommand corresponding to the input String
     */
    private static FullCommand getCorrespondingFullCommand(String s){

        CommandType commandType = null;
        int[] numbers = null;

        switch (s.charAt(0)) {

            case 'b':
                commandType = CommandType.BET;
                if (s.length() != 1){
                    numbers = new int[]{Integer.parseInt(s.substring(1))};
                }
                break;

            case '$':
                commandType = CommandType.CREDIT;
                break;
            case 'a':
                commandType = CommandType.ADVICE;
                break;
            case 's':
                commandType = CommandType.STATISTICS;
                break;
            case 'd':
                commandType = CommandType.DEAL;
                break;
            case 'h':
                commandType = CommandType.HOLD;
                if (s.length() == 1) {
                    numbers = new int[0];
                }else {
                    numbers = new int[s.substring(1).length()];

                    for (int i = 0; i < s.substring(1).length(); i++){
                        numbers[i] = Character.getNumericValue(s.substring(1).charAt(i));
                    }
                }
                break;
        }


        FullCommand fullCommand = new FullCommand(commandType);
        if (numbers != null) { fullCommand.addNumbers(numbers);}

        return fullCommand;

    }
}
