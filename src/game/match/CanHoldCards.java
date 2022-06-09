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
        
        String typeOfHand = this.classifyHand();
        System.out.println("Hand: " + Arrays.toString(player.getHand()));
        System.out.println("Hand type: " + typeOfHand);


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
        //PRECISO MUDAR -- AQUI ELE TEM DE RETORNAR OS INDICES DAS CARTAS A REMOVER

        Player player = super.match.getPlayer();
        Deck deck = super.match.getDeck();

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

        if (isRoyalFlush || isStraightFlush || isFourOfAKind) {

            int[] indexArray = new int[]{1,2,3,4,5};
            return indexArray;

        } else if (isFourToARoyalFlush) {

            //falta implementar

        } else if (isThreeAces) {

            int[] indexArray = new int[3];
            int index = 0;
            Card[] card = player.getHand();
            for (int i=0; i<5; i++) {
                if(card[i].getRank() == Rank.ACE){
                    indexArray[index++] = i+1;
                }
            }

            return indexArray;


        } else if (isFullHouse || isFlush || isFiveConsecutiveCards) {

            int[] indexArray = new int[]{1,2,3,4,5};
            return indexArray;

        } else if (isThreeOfAKind) {


            for (Rank r : Rank.values()) {
                if (this.rankCount.get(r) == 3) {
                    Rank rank = r;
                }
            }

            int[] indexArray = new int[3];
            int index = 0;
            Card[] card = player.getHand();
            for (int i=0; i<5; i++) {

                if(card[i].getRank() ==  ){
                    indexArray[index++] = i+1;
                }
            }

        } else if (isTwoPair) {

        } else if (isJacksOrBetter) {

        } else {

        }

        return new int[0];
    }

    @Override
    public void printAdvice(){
        //CHAMA O getAdvice E IMPRIME SUGESTAO
        int[] numbers = this.getAdvice();
    }


    private boolean isFourToARoyalFlush() {


        return false;


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