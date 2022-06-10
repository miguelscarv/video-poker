package player.cards;

public class Card {

    private Rank rank;
    private Suit suit;

    public Card(Rank r, Suit s){
        this.rank = r;
        this.suit = s;
    }

    public Rank getRank() { return this.rank; }
    public Suit getSuit() { return this.suit; }

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
