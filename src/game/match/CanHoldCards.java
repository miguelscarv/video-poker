package game.match;

import command.FullCommand;
import player.Player;
import player.cards.Card;
import player.cards.Deck;
import player.cards.Rank;
import player.cards.Suit;

import java.util.*;

public class CanHoldCards extends State {


    private Map<Suit, Integer> suitCount = new HashMap<Suit, Integer>();
    private Map<Rank, Integer> rankCount = new HashMap<Rank, Integer>();

    public CanHoldCards(Match match) {
        super(match);
        this.setCountersToZero();
    }

    @Override
    public void bet(FullCommand command) throws IllegalException {
        throw new IllegalException("b");
    }

    @Override
    public void dealCards() throws IllegalException {
        throw new IllegalException("d");
    }

    @Override
    public void holdCards(FullCommand command) {

        Player player = super.match.getPlayer();
        Deck deck = super.match.getDeck();

        if (command.hasNumbers()) {

            List<Card> toRemoveList = new ArrayList<Card>();

            //THE COMMAND NUMBERS TELLS YOU WHAT CARDS TO HOLD ON TOO!!!!!
            for (int i = 0; i < 5; i++) {
                if (!contains(command.getNumbers(), i)) {
                    toRemoveList.add(player.getHand()[i]);
                }
            }

            Card[] toRemoveArray = new Card[toRemoveList.size()];
            toRemoveList.toArray(toRemoveArray);
            player.removeCardsFromHand(toRemoveArray);
            player.addCardsToHand(deck);


        } else {
            player.addHandCardsToDeck(deck);
            player.addCardsToHand(deck);
        }

        this.computeHandOutcome();
        super.match.setState(super.match.getCanBet());

    }

    private void computeHandOutcome() {

        Player player = this.match.getPlayer();
        Deck deck = super.match.getDeck();
        int beforeCredit = player.getCredit();
        
        String typeOfHand = this.classifyHand();
        if (this.match.getIsDebugMode()) {
            System.out.println("player\'s hand " + Arrays.toString(player.getHand()));
            //System.out.println("Hand type: " + typeOfHand);
        }

        if (typeOfHand.equals("Royal Flush")) {
            if (player.getLastBetAmount() == 5) {
                player.setCredit(player.getCredit() + 4000);
            } else {
                player.setCredit(player.getCredit() + player.getLastBetAmount() * Payout.royalFlush);
            }
        } else if (typeOfHand.equals("Straight Flush")) {
            player.setCredit(player.getCredit() + player.getLastBetAmount() * Payout.straightFlush);
        } else if (typeOfHand.equals("Four of a Kind")) {
            if (this.rankCount.get(Rank.ACE) == 4) {
                player.setCredit(player.getCredit() + player.getLastBetAmount() * Payout.fourAces);
            } else if (this.rankCount.get(Rank.TWO) >= 4 || this.rankCount.get(Rank.THREE) >= 4 || this.rankCount.get(Rank.FOUR) >= 4) {
                player.setCredit(player.getCredit() + player.getLastBetAmount() * Payout.four2_4);
            } else {
                player.setCredit(player.getCredit() + player.getLastBetAmount() * Payout.four5_K);
            }
        } else if (typeOfHand.equals("Full house")) {
            player.setCredit(player.getCredit() + player.getLastBetAmount() * Payout.fullHouse);
        } else if (typeOfHand.equals("Flush")) {
            player.setCredit(player.getCredit() + player.getLastBetAmount() * Payout.flush);
        } else if (typeOfHand.equals("Straight")) {
            player.setCredit(player.getCredit() + player.getLastBetAmount() * Payout.straight);
        } else if (typeOfHand.equals("Three of a Kind")) {
            player.setCredit(player.getCredit() + player.getLastBetAmount() * Payout.threeOfAKind);
        } else if (typeOfHand.equals("Two Pair")) {
            player.setCredit(player.getCredit() + player.getLastBetAmount() * Payout.twoPair);
        } else if (typeOfHand.equals("Jacks or Better")) {
            player.setCredit(player.getCredit() + player.getLastBetAmount() * Payout.jacksOrBetter);
        }

        int afterCredit = player.getCredit();

        if (this.match.getIsDebugMode()) {
            if (afterCredit > beforeCredit) {
                System.out.println("player wins with a " + typeOfHand.toUpperCase() + " and his credit is " + afterCredit);
            } else {
                System.out.println("player loses and his credit is " + afterCredit);
            }
        }

        player.addOneToStatistics(typeOfHand);
        player.addHandCardsToDeck(deck);

        if (!super.match.getIsDebugMode()) {
            deck.shuffle();
        }

        this.setCountersToZero();
    }

    private String classifyHand() {

        Player player = this.match.getPlayer();

        for (Card c : player.getHand()) {
            this.suitCount.put(c.getSuit(), this.suitCount.get(c.getSuit()) + 1);
            this.rankCount.put(c.getRank(), this.rankCount.get(c.getRank()) + 1);
        }

        String combination;

        boolean isFlush = this.isFlush();
        boolean isJacksOrBetter = this.isJacksOrBetter();
        boolean isThreeOfAKind = this.isThreeOfAKind();
        boolean isTwoPair = this.isTwoPair();
        boolean isFullHouse = this.isFullHouse();
        boolean isFourOfAKind = this.isFourOfAKind();
        boolean isFiveConsecutiveCards = this.isFiveConsecutiveCards();
        boolean isStraightFlush = isFiveConsecutiveCards && isFlush;
        boolean isRoyalFlush = isStraightFlush && this.rankCount.get(Rank.ACE) == 1 && this.rankCount.get(Rank.TEN) == 1;

        if (isRoyalFlush) {
            combination = "Royal Flush";
        } else if (isStraightFlush) {
            combination = "Straight Flush";
        } else if (isFourOfAKind) {
            combination = "Four of a Kind";
        } else if (isFullHouse) {
            combination = "Full house";
        } else if (isFlush) {
            combination = "Flush";
        } else if (isFiveConsecutiveCards) {
            combination = "Straight";
        } else if (isThreeOfAKind) {
            combination = "Three of a Kind";
        } else if (isTwoPair) {
            combination = "Two Pair";
        } else if (isJacksOrBetter) {
            combination = "Jacks or Better";
        } else {
            combination = "Other";
        }

        return combination;
    }


    private boolean isThreeOfAKind() {
        for (Rank r : Rank.values()) {
            if (rankCount.get(r) == 3) {
                return true;
            }
        }
        return false;
    }
    private boolean isFlush() {
        boolean isFlush = this.suitCount.get(Suit.CLUBS) == 5 ||
                this.suitCount.get(Suit.DIAMONDS) == 5 ||
                this.suitCount.get(Suit.HEARTS) == 5 ||
                this.suitCount.get(Suit.SPADES) == 5;
        return isFlush;
    }
    private boolean isJacksOrBetter() {
        boolean isJacksOrBetter = this.rankCount.get(Rank.JACK) == 2 ||
                this.rankCount.get(Rank.QUEEN) == 2 ||
                this.rankCount.get(Rank.KING) == 2 ||
                this.rankCount.get(Rank.ACE) == 2;
        return isJacksOrBetter;
    }
    private boolean isTwoPair() {
        int numberOfPairs = 0;

        for (Integer i : this.rankCount.values()) {
            if (i == 2) {
                numberOfPairs++;
            }
        }
        if (numberOfPairs == 2) {
            return true;
        }
        return false;
    }
    private boolean isFullHouse() {
        boolean hasPair = false;
        boolean hasThree = false;

        for (Integer i : this.rankCount.values()) {
            if (i == 2) {
                hasPair = true;
            }
            if (i == 3) {
                hasThree = true;
            }
        }
        return hasPair && hasThree;
    }
    private boolean isFourOfAKind() {
        for (Rank r : Rank.values()) {
            if (rankCount.get(r) >= 4) {
                return true;
            }
        }
        return false;
    }
    private boolean isFiveConsecutiveCards() {

        int counter = 0;
        boolean toReturn = this.rankCount.get(Rank.ACE) == 1 &&
                this.rankCount.get(Rank.TWO) == 1 &&
                this.rankCount.get(Rank.THREE) == 1 &&
                this.rankCount.get(Rank.FOUR) == 1 &&
                this.rankCount.get(Rank.FIVE) == 1;


        for (Rank r : Rank.values()) {
            if (this.rankCount.get(r) == 1) {
                counter++;
            } else {
                counter = 0;
            }

            if (counter == 5) {
                toReturn = true;
            }
        }


        return toReturn;
    }

    private void setCountersToZero() {
        for (Rank r : Rank.values()) {
            rankCount.put(r, 0);
        }
        for (Suit s : Suit.values()) {
            suitCount.put(s, 0);
        }
    }

    public int[] getAdvice(){

        Player player = super.match.getPlayer();
        Card[] cards = player.getHand();

        for (Card c : player.getHand()) {
            this.suitCount.put(c.getSuit(), this.suitCount.get(c.getSuit()) + 1);
            this.rankCount.put(c.getRank(), this.rankCount.get(c.getRank()) + 1);
        }

        boolean isFlush = this.isFlush();
        boolean isJacksOrBetter = this.isJacksOrBetter();
        boolean isThreeOfAKind = this.isThreeOfAKind();
        boolean isTwoPair = this.isTwoPair();
        boolean isFullHouse = this.isFullHouse();
        boolean isFourOfAKind = this.isFourOfAKind();
        boolean isFiveConsecutiveCards = this.isFiveConsecutiveCards();
        boolean isStraightFlush = isFiveConsecutiveCards && isFlush;
        boolean isRoyalFlush = isStraightFlush && this.rankCount.get(Rank.ACE) == 1 && this.rankCount.get(Rank.TEN) == 1;

        boolean isFourToARoyalFlush = this.isFourToARoyalFlush();
        boolean isThreeAces = isThreeOfAKind && this.rankCount.get(Rank.ACE) == 3;

        int[] indexArray;

        if (isRoyalFlush || isStraightFlush || isFourOfAKind || isFullHouse || isFlush || isFiveConsecutiveCards) {

            indexArray = new int[]{1,2,3,4,5};

        } else if (isFourToARoyalFlush) {

            //falta implementar

        } else if (isThreeAces) {

            indexArray = new int[3];
            int index = 0;

            for (int i=0; i<5; i++) {
                if(cards[i].getRank() == Rank.ACE){
                    indexArray[index++] = i+1;
                }
            }


        } else if (isThreeOfAKind) {

            Rank rank = this.getThreeOfAKindRank();

            indexArray = new int[3];
            int index = 0;

            for (int i=0; i<5; i++) {
                if(cards[i].getRank() == rank){
                    indexArray[index++] = i+1;
                }
            }

        } else if (isTwoPair) {

        } else if (isJacksOrBetter) {

        } else {

        }

        this.setCountersToZero();
        return new int[0];
    }

    @Override
    public void printAdvice(){

        int[] numbers = this.getAdvice();

        if (numbers.length != 0) {
            System.out.println("the player should hold cards " + Arrays.toString(numbers));
        } else {
            System.out.println("the player should hold no cards");
        }

    }

    private boolean isFourToARoyalFlush() {


        return false;


    }

    private Rank getThreeOfAKindRank(){

        for (Rank r: this.rankCount.keySet()){
            if (this.rankCount.get(r) == 3){
                return r;
            }
        }

        return null;
    }

    private boolean contains(final int[] arr, final int key) {
        for (int i : arr) {
            if (i - 1 == key) {
                return true;
            }
        }
        return false;
    }
}