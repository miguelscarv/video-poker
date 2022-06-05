package match;

import player.cards.Card;
import player.cards.Rank;
import player.cards.Suit;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

final public class Reader {

    private static List<Card> deck = new ArrayList<Card>();
    private static List<FullCommand> fullCommandList = new ArrayList<FullCommand>();

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
            String[] individualCommandArray = contents.split("((?=[bdh$as]))");

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

    private static Card getCorrespondingCard(String s) {
        Rank rank = null;
        Suit suit = null;

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
        }

        Card card = new Card(rank, suit);
        return card;
    }

    private static FullCommand getCorrespondingFullCommand(String s){

        CommandType commandType = null;
        int[] numbers = null;

       if (s.length()  == 1){

           switch (s.charAt(0)) {

               case 'b':
                   commandType = CommandType.BET;
                   numbers = new int[]{5};
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

           }

       } else {

           switch (s.charAt(0)) {

               case 'b':
                   commandType = CommandType.BET;
                   numbers = new int[]{Integer.parseInt(s.substring(1))};
                   break;
               case 'h':
                   commandType = CommandType.HOLD;
                   numbers = new int[s.substring(1).length()];

                   for (int i = 0; i < s.substring(1).length(); i++){
                        numbers[i] = Character.getNumericValue(s.substring(1).charAt(i));
                   }
           }
       }

       FullCommand fullCommand = new FullCommand(commandType);
       if (numbers != null) { fullCommand.addNumbers(numbers);}

       return fullCommand;

    }
}
