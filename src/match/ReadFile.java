package match;

import player.cards.Card;
import player.cards.Rank;
import player.cards.Suit;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFile {

    public static List<Card> deck = new ArrayList<Card>();

    public static void readCardFile(String pathToFile){

        String contents = "";

        try {

            File cardFile = new File(pathToFile);
            Scanner myReader = new Scanner(cardFile);
            contents = myReader.nextLine();
            myReader.close();

            String[] cardStrings = contents.split(" ");

            for (String s: cardStrings){
                Card card = getCorrespondingCard(s);
                deck.add(card);
            }

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred reading the card file");
            e.printStackTrace();
        }
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
}
