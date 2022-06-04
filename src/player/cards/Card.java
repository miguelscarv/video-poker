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
        return "" + this.rank.toString() + " OF " + this.suit.toString();
    }
}
