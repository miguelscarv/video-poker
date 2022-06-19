package player.cards;

/**
 * Card class represents a regular deck card. As a regular deck card, it has a suit and a rank.
 * @author Miguel Carvalho, Filipe Ferraz, João Baptista
 */
public class Card {

    private Rank rank;
    private Suit suit;

    /**
     * Constructor to initialize card object.
     * @param r Rank of the card.
     * @param s Suit of the card.
     */
    public Card(Rank r, Suit s){
        this.rank = r;
        this.suit = s;
    }

    /**
     * This method returns the rank of the present card.
     * @return Rank of the card.
     */
    public Rank getRank() { return this.rank; }

    /**
     * This method returns the suit of the present card.
     * @return Suit of the card.
     */
    public Suit getSuit() { return this.suit; }


    /**
     * This method computes a valid representation of a card that can be printed.
     * @return Unique String that identifies the card.
     */
    @Override
    public String toString(){

        String rank;
        String suit;

        if (this.rank == Rank.ACE){
            rank = "A";
        } else if (this.rank == Rank.KING){
            rank = "K";
        } else if (this.rank == Rank.QUEEN){
            rank = "Q";
        } else if (this.rank == Rank.JACK){
            rank = "J";
        } else if (this.rank == Rank.TEN){
            rank = "T";
        } else if (this.rank == Rank.NINE){
            rank = "9";
        } else if (this.rank == Rank.EIGHT){
            rank = "8";
        } else if (this.rank == Rank.SEVEN){
            rank = "7";
        } else if (this.rank == Rank.SIX){
            rank = "6";
        } else if (this.rank == Rank.FIVE){
            rank = "5";
        } else if (this.rank == Rank.FOUR){
            rank = "4";
        } else if (this.rank == Rank.THREE){
            rank = "3";
        } else {
            rank = "2";
        }


        if (this.suit == Suit.CLUBS){
            suit = "C";
        } else if (this.suit == Suit.DIAMONDS){
            suit = "D";
        } else if (this.suit == Suit.HEARTS){
            suit = "H";
        } else {
            suit = "S";
        }




        return rank+suit;
    }

}
