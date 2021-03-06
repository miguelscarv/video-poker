package game.match;

import command.FullCommand;
import game.match.Match;
import game.match.Payout;
import game.match.State;
import player.Player;
import player.cards.Card;
import player.cards.Deck;
import player.cards.Rank;
import player.cards.Suit;
import game.match.IllegalException;

import java.util.*;

/**
 * Class CanHoldCards represents a state, in which the legal commands are: hold(h),advice(a),credit($) and statistics(s)
 * @author Miguel Carvalho, Filipe Ferraz, Joao Baptista
 */
public class CanHoldCards extends State {



    private Map<Suit, Integer> suitCount = new HashMap<Suit, Integer>();
    private Map<Rank, Integer> rankCount = new HashMap<Rank, Integer>();

    /**
     * @see "game.match.State Constructor."
     * @param match Match associated with the state.
     */
    public CanHoldCards(Match match) {
        super(match);
        this.setCountersToZero();
    }

    /**
     * At this state this method throws an illegal exception.
     * @see "game.match.State bet method."
     * @param command Full command with command type BET.
     * @throws IllegalException Exception Thrown when illegal command.
     */
    @Override
    public void bet(FullCommand command) throws IllegalException {
        throw new IllegalException("b");
    }

    /**
     * At this state this method throws an illegal exception.
     * @see "game.match.State dealCards method."
     * @throws IllegalException Exception Thrown when illegal command.
     */
    @Override
    public void dealCards() throws IllegalException {
        throw new IllegalException("d");
    }

    /**
     * At this state this method performs a hold, meaning that the player is going to hold the cards specified.
     * @see "game.match.State holdCards method."
     * @param command Full command with command type HOLD.
     */
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

    private boolean contains(final int[] arr, final int key) {
        for (int i : arr) {
            if (i - 1 == key) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method updates the credit of the player regarding the amount bet and the hand output.
     */
    private void computeHandOutcome() {

        Player player = this.match.getPlayer();
        Deck deck = super.match.getDeck();
        int beforeCredit = player.getCredit();

        String typeOfHand = this.classifyHand();
        if (this.match.getIsDebugMode()) {
            System.out.println("player\'s hand " + Arrays.toString(player.getHand()));
            System.out.println("Hand type: " + typeOfHand);
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

    /**
     * This method classifies the hand of the player regarding the present cards in the hand.
     * @return String that represents the hand outcome.
     */
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

    /**
     * This method checks if the hand of the player is classified as a "Three of a Kind".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isThreeOfAKind() {
        for (Rank r : Rank.values()) {
            if (rankCount.get(r) == 3) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks if the hand of the player is classified as a "Flush".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isFlush() {
        boolean isFlush = this.suitCount.get(Suit.CLUBS) == 5 ||
                this.suitCount.get(Suit.DIAMONDS) == 5 ||
                this.suitCount.get(Suit.HEARTS) == 5 ||
                this.suitCount.get(Suit.SPADES) == 5;
        return isFlush;
    }

    /**
     * This method checks if the hand of the player is classified as a "Jacks or Better".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isJacksOrBetter() {
        boolean isJacksOrBetter = this.rankCount.get(Rank.JACK) == 2 ||
                this.rankCount.get(Rank.QUEEN) == 2 ||
                this.rankCount.get(Rank.KING) == 2 ||
                this.rankCount.get(Rank.ACE) == 2;
        return isJacksOrBetter;
    }

    /**
     * This method checks if the hand of the player is classified as a "Two Pair".
     * @return Boolean true/false depending on the outcome.
     */
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

    /**
     * This method checks if the hand of the player is classified as a "Full House".
     * @return Boolean true/false depending on the outcome.
     */
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

    /**
     * This method checks if the hand of the player is classified as a "Four of a Kind".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isFourOfAKind() {
        for (Rank r : Rank.values()) {
            if (rankCount.get(r) >= 4) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks if the hand of the player is classified as a "Straight".
     * @return Boolean true/false depending on the outcome.
     */
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

    /**
     * This method resets the values of the suitCount and rankCount. Called after the classification of the hand.
     */
    private void setCountersToZero() {
        for (Rank r : Rank.values()) {
            rankCount.put(r, 0);
        }
        for (Suit s : Suit.values()) {
            suitCount.put(s, 0);
        }
    }

    /**
     * This method implements the full strategy. Always called in the Simulation mode.
     * @return Array containing the indices of the cards the player should hold.
     */
    public int[] getAdvice() {

        Player player = super.match.getPlayer();

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
        boolean isFourToAStraightFlush = this.isFourToAStraightFlush();
        boolean isFourToFlush = this.isFourToFlush();
        boolean isThreeToARoyalFlush = this.isThreeToARoyalFlush();
        boolean isFourToAnOutsideStraight = this.isFourToAnOutsideStraight();
        boolean isLowPair = this.isLowPair();
        boolean isAKQJUnsuited = this.isAKQJUnsuited();
        boolean isThreeToAStraightFlush1 = this.isThreeToAStraightFlush1();
        boolean isFourToAnInsideStraightWithThreeHighCards = this.isFourToAnInsideStraightWithThreeHighCards();
        boolean isQJSuited = this.isQJSuited();
        boolean isThreeToAFlushWithTwoHighCards = this.isThreeToAFlushWithTwoHighCards();
        boolean isTwoSuitedHighCards = this.isTwoSuitedHighCards();
        boolean isFourToAnInsideStraightWithTwoHighCards = this.isFourToAnInsideStraightWithTwoHighCards();
        boolean isThreeToAStraightFlush2 = this.isThreeToAStraightFlush2();
        boolean isFourToAnInsideStraightWithOneHighCards = this.isFourToAnInsideStraightWithOneHighCards();
        boolean isKQJUnsuited = this.isKQJUnsuited();
        boolean isJTSuited = this.isJTSuited();
        boolean isQJUnsuited = this.isQJUnsuited();
        boolean isThreeToAFlushWithOneHighCard = this.isThreeToAFlushWithOneHighCard();
        boolean isQTSuited = this.isQTSuited();
        boolean isThreeToAStraightFlush3 = this.isThreeToAStraightFlush3();
        boolean isKQUnsuited = this.isKQUnsuited();
        boolean isKJUnsuited = this.isKJUnsuited();
        boolean isAce = this.isAce();
        boolean isKTSuited = this.isKTSuited();
        boolean isJQK = this.isJQK();
        boolean isFourToAnInsideStraightWithNoHighCards = this.isFourToAnInsideStraightWithNoHighCards();
        boolean isThreeToAFlushWithNoHighCard = this.isThreeToAFlushWithNoHighCard();

        int[] indexArray;


        if (isRoyalFlush || isStraightFlush || isFourOfAKind) {

            indexArray = new int[]{1, 2, 3, 4, 5};


        } else if (isFourToARoyalFlush) {

            indexArray = new int[4];

            int index = 0;
            Card[] card = player.getHand();

            if (this.isFlush()) {


                for (int i = 0; i < 5; i++) {
                    if (card[i].getRank() == Rank.ACE ||
                            card[i].getRank() == Rank.KING ||
                            card[i].getRank() == Rank.QUEEN ||
                            card[i].getRank() == Rank.JACK ||
                            card[i].getRank() == Rank.TEN) {
                        indexArray[index++] = i + 1;
                    }
                }


            } else {

                Suit suit = this.getNumberOfAKindSuit(4);

                for (int i = 0; i < 5; i++) {

                    if (card[i].getSuit() == suit) {
                        indexArray[index++] = i + 1;
                    }
                }

            }


        } else if (isThreeAces) {

            indexArray = new int[3];
            int index = 0;
            Card[] card = player.getHand();
            for (int i = 0; i < 5; i++) {
                if (card[i].getRank() == Rank.ACE) {
                    indexArray[index++] = i + 1;
                }
            }

        } else if (isFullHouse || isFlush || isFiveConsecutiveCards) {

            indexArray = new int[]{1, 2, 3, 4, 5};

        } else if (isThreeOfAKind) {

            Rank rank = this.getThreeOfAKindRank();

            indexArray = new int[3];
            int index = 0;
            Card[] card = player.getHand();
            for (int i = 0; i < 5; i++) {

                if (card[i].getRank() == rank) {
                    indexArray[index++] = i + 1;
                }
            }

        } else if (isFourToAStraightFlush) {

            Suit suit = this.getNumberOfAKindSuit(4);

            indexArray = new int[4];
            int index = 0;
            Card[] card = player.getHand();
            for (int i = 0; i < 5; i++) {

                if (card[i].getSuit() == suit) {
                    indexArray[index++] = i + 1;
                }
            }

        } else if (isTwoPair) {

            indexArray = new int[4];
            Card[] card = player.getHand();
            int index = 0;

            for (Rank r : Rank.values()) {
                if (this.rankCount.get(r) == 2) {
                    for (int i = 0; i < 5; i++) {

                        if (card[i].getRank() == r) {
                            indexArray[index++] = i + 1;
                        }
                    }
                }
            }

            Arrays.sort(indexArray);


        } else if (isJacksOrBetter) {

            indexArray = new int[2];

            Rank rank = this.getPairRank();
            Card[] card = player.getHand();
            int index = 0;

            for (int i = 0; i < 5; i++) {

                if (card[i].getRank() == rank) {
                    indexArray[index++] = i + 1;
                }
            }


        } else if (isFourToFlush) {

            Suit suit = this.getNumberOfAKindSuit(4);

            indexArray = new int[4];
            int index = 0;
            Card[] card = player.getHand();
            for (int i = 0; i < 5; i++) {

                if (card[i].getSuit() == suit) {
                    indexArray[index++] = i + 1;
                }
            }

        } else if (isThreeToARoyalFlush) {

            indexArray = new int[3];

            Suit suit = this.getNumberOfAKindSuit(3);

            int index = 0;
            Card[] card = player.getHand();
            for (int i = 0; i < 5; i++) {

                if (card[i].getSuit() == suit) {
                    indexArray[index++] = i + 1;
                }
            }


        } else if (isFourToAnOutsideStraight) {

            indexArray = new int[4];

            int biggerCardIndex = this.getBiggerCardIndex();

            int outsideIndex = 0;
            int possibleOutsideIndex = 0;
            int counter = 0;

            Card[] card = player.getHand();
            for (int j = 0; j < 5; j++) {
                if (j != biggerCardIndex) {

                    if (Math.abs((card[biggerCardIndex].getRank()).ordinal() - (card[j].getRank()).ordinal()) > 3) {
                        possibleOutsideIndex = j;
                        counter++;
                    }
                }

            }

            if (counter > 1) outsideIndex = biggerCardIndex;
            else outsideIndex = possibleOutsideIndex;

            if(this.isLowPair()){
                Rank rank = this.getPairRank();
                for (int j = 0; j < 5; j++) {
                    if (card[j].getRank() == rank) {
                        outsideIndex = j;
                        break;
                    }
                }

            }

            int index = 0;

            for (int i = 0; i < 5; i++) {
                if (i != outsideIndex) {
                    indexArray[index++] = i + 1;
                }
            }


        } else if (isLowPair) {

            indexArray = new int[2];

            Rank rank = this.getPairRank();
            Card[] card = player.getHand();
            int index = 0;

            for (int i = 0; i < 5; i++) {

                if (card[i].getRank() == rank) {
                    indexArray[index++] = i + 1;
                }
            }


        } else if (isAKQJUnsuited) {

            indexArray = new int[4];

            Card[] card = player.getHand();
            int index = 0;

            for (int i = 0; i < 5; i++) {

                if (card[i].getRank() == Rank.ACE ||
                        card[i].getRank() == Rank.KING ||
                        card[i].getRank() == Rank.QUEEN ||
                        card[i].getRank() == Rank.JACK) {

                    indexArray[index++] = i + 1;
                }
            }


        } else if (isThreeToAStraightFlush1) {

            indexArray = new int[3];

            Suit suit = this.getNumberOfAKindSuit(3);

            int index = 0;
            Card[] card = player.getHand();
            for (int i = 0; i < 5; i++) {

                if (card[i].getSuit() == suit) {
                    indexArray[index++] = i + 1;
                }
            }

        } else if (isFourToAnInsideStraightWithThreeHighCards) {

            indexArray = new int[4];
            int outsideIndex = 0;

            outsideIndex = this.getFourToAnInsideStraightOutsideIndex();

            int index = 0;
            for (int i = 0; i < 5; i++) {
                if (i != outsideIndex)
                    indexArray[index++] = i + 1;
            }


        } else if (isQJSuited) {

            indexArray = new int[2];
            Card[] card = player.getHand();
            int index = 0;

            for (int i = 0; i < 5; i++) {

                if (card[i].getRank() == Rank.QUEEN ||
                        card[i].getRank() == Rank.JACK) {

                    indexArray[index++] = i + 1;
                }
            }


        } else if (isThreeToAFlushWithTwoHighCards) {

            indexArray = new int[3];
            Suit suit = this.getNumberOfAKindSuit(3);

            int index = 0;
            Card[] card = player.getHand();
            for (int i = 0; i < 5; i++) {

                if (card[i].getSuit() == suit) {
                    indexArray[index++] = i + 1;
                }
            }

        } else if (isTwoSuitedHighCards) {

            indexArray = new int[2];
            int index = 0;

            Card[] card = player.getHand();
            for (int i = 0; i < 5; i++) {
                if (this.suitCount.get(card[i].getSuit()) == 2) {
                    if (card[i].getRank() == Rank.JACK || card[i].getRank() == Rank.QUEEN
                            || card[i].getRank() == Rank.KING || card[i].getRank() == Rank.ACE) {

                        indexArray[index++] = i + 1;
                    }
                }
            }

        } else if (isFourToAnInsideStraightWithTwoHighCards) {

            indexArray = new int[4];
            int outsideIndex = 0;

            outsideIndex = this.getFourToAnInsideStraightOutsideIndex();

            int index = 0;
            for (int i = 0; i < 5; i++) {
                if (i != outsideIndex)
                    indexArray[index++] = i + 1;
            }


        } else if (isThreeToAStraightFlush2) {

            indexArray = new int[3];
            Suit suit = this.getNumberOfAKindSuit(3);

            int index = 0;
            Card[] card = player.getHand();
            for (int i = 0; i < 5; i++) {

                if (card[i].getSuit() == suit) {
                    indexArray[index++] = i + 1;
                }
            }

        } else if (isFourToAnInsideStraightWithOneHighCards) {

            indexArray = new int[4];
            int outsideIndex = 0;

            outsideIndex = this.getFourToAnInsideStraightOutsideIndex();

            int index = 0;
            for (int i = 0; i < 5; i++) {
                if (i != outsideIndex)
                    indexArray[index++] = i + 1;
            }


        } else if (isKQJUnsuited) {

            indexArray = new int[3];

            int index = 0;
            Card[] card = player.getHand();
            for (int i = 0; i < 5; i++) {

                if (card[i].getRank() == Rank.KING ||
                        card[i].getRank() == Rank.QUEEN ||
                        card[i].getRank() == Rank.JACK) {

                    indexArray[index++] = i + 1;
                }
            }

        } else if (isJTSuited) {

            indexArray = new int[2];

            Card[] card = player.getHand();
            int index = 0;

            for (int i = 0; i < 5; i++) {

                if (card[i].getRank() == Rank.JACK ||
                        card[i].getRank() == Rank.TEN) {

                    indexArray[index++] = i + 1;
                }
            }


        } else if (isQJUnsuited) {

            indexArray = new int[2];

            Card[] card = player.getHand();
            int index = 0;

            for (int i = 0; i < 5; i++) {

                if (card[i].getRank() == Rank.QUEEN ||
                        card[i].getRank() == Rank.JACK) {

                    indexArray[index++] = i + 1;
                }
            }


        } else if (isThreeToAFlushWithOneHighCard) {

            indexArray = new int[3];
            Suit suit = this.getNumberOfAKindSuit(3);

            int index = 0;
            Card[] card = player.getHand();
            for (int i = 0; i < 5; i++) {

                if (card[i].getSuit() == suit) {
                    indexArray[index++] = i + 1;
                }
            }


        } else if (isQTSuited) {

            indexArray = new int[2];
            Card[] card = player.getHand();
            int index = 0;

            for (int i = 0; i < 5; i++) {

                if (card[i].getRank() == Rank.QUEEN ||
                        card[i].getRank() == Rank.TEN) {

                    indexArray[index++] = i + 1;
                }
            }


        } else if (isThreeToAStraightFlush3) {

            indexArray = new int[3];
            Suit suit = this.getNumberOfAKindSuit(3);

            int index = 0;
            Card[] card = player.getHand();
            for (int i = 0; i < 5; i++) {

                if (card[i].getSuit() == suit) {
                    indexArray[index++] = i + 1;
                }
            }


        } else if (isKQUnsuited || isKJUnsuited) {

            indexArray = new int[2];
            Card[] card = player.getHand();
            int index = 0;

            for (int i = 0; i < 5; i++) {

                if (card[i].getRank() == Rank.KING ||
                        card[i].getRank() == Rank.QUEEN ||
                        card[i].getRank() == Rank.JACK) {

                    indexArray[index++] = i + 1;
                }
            }


        } else if (isAce) {

            indexArray = new int[1];
            Card[] card = player.getHand();

            for (int i = 0; i < 5; i++) {

                if (card[i].getRank() == Rank.ACE) {

                    indexArray[0] = i + 1;
                }
            }


        } else if (isKTSuited) {

            indexArray = new int[2];
            Card[] card = player.getHand();
            int index = 0;

            for (int i = 0; i < 5; i++) {

                if (card[i].getRank() == Rank.KING ||
                        card[i].getRank() == Rank.TEN) {

                    indexArray[index++] = i + 1;
                }
            }


        } else if (isJQK) {

            indexArray = new int[1];
            Card[] card = player.getHand();

            for (int i = 0; i < 5; i++) {

                if (card[i].getRank() == Rank.JACK ||
                        card[i].getRank() == Rank.QUEEN ||
                        card[i].getRank() == Rank.KING) {

                    indexArray[0] = i + 1;
                }
            }


        } else if (isFourToAnInsideStraightWithNoHighCards) {

            indexArray = new int[4];
            int outsideIndex = 0;

            outsideIndex = this.getFourToAnInsideStraightOutsideIndex();

            int index = 0;
            for (int i = 0; i < 5; i++) {
                if (i != outsideIndex)
                    indexArray[index++] = i + 1;
            }


        } else if (isThreeToAFlushWithNoHighCard) {

            indexArray = new int[3];

            Suit suit = this.getNumberOfAKindSuit(3);

            int index = 0;
            Card[] card = player.getHand();
            for (int i = 0; i < 5; i++) {

                if (card[i].getSuit() == suit) {
                    indexArray[index++] = i + 1;
                }
            }


        } else {

            indexArray = new int[0];

        }

        this.setCountersToZero();

        return indexArray;
    }

    /**
     * At this state this method performs an advice, meaning the player is adviced to hold specific cards in the hand.
     * Called only in the Debug Mode.
     * @see "game.match.State printAdvice method."
     */
    @Override
    public void printAdvice() {

        int[] numbers = this.getAdvice();

        if (numbers.length != 0) {
            System.out.println("the player should hold cards " + Arrays.toString(numbers));
        } else {
            System.out.println("the player should hold no cards");
        }

    }

    /**
     * This method checks if the hand of the player is classified as a "Four to a Royal Flush".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isFourToARoyalFlush() {

        if(this.isFourToFlush()) {
            int counter = 0;
            Player player = this.match.getPlayer();

            for (Card c : player.getHand()) {
                if (this.suitCount.get(c.getSuit()) >= 4) {
                    if (c.getRank() ==Rank.TEN || c.getRank() ==Rank.JACK || c.getRank() == Rank.QUEEN
                            || c.getRank() == Rank.KING || c.getRank() == Rank.ACE) {
                        counter++;
                    }
                }
            }
            return (counter >= 4);
        }

        return false;


    }

    /**
     * This method checks if the hand of the player is classified as a "Four to a Straight Flush".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isFourToAStraightFlush() {

        boolean is4TF = this.isFourToFlush();
        boolean is4TS = this.isFourToAnInsideStraight() || this.isFourToAnOutsideStraight();

        return is4TS && is4TF;

    }

    /**
     * This method checks if the hand of the player is classified as a "Four to a Flush".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isFourToFlush() {

        boolean is4TF = this.suitCount.get(Suit.CLUBS) >= 4 ||
                this.suitCount.get(Suit.DIAMONDS) >= 4 ||
                this.suitCount.get(Suit.HEARTS) >= 4 ||
                this.suitCount.get(Suit.SPADES) >= 4;

        return is4TF;

    }

    /**
     * This method checks if the hand of the player is classified as a "Three to a Flush".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isThreeToFlush() {

        boolean is3TF = this.suitCount.get(Suit.CLUBS) >= 3 ||
                this.suitCount.get(Suit.DIAMONDS) >= 3 ||
                this.suitCount.get(Suit.HEARTS) >= 3 ||
                this.suitCount.get(Suit.SPADES) >= 3;

        return is3TF;

    }

    /**
     * This method checks if the hand of the player is classified as a "Three to a Royal Flush".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isThreeToARoyalFlush() {

        if(this.isThreeToFlush()) {
            int counter = 0;
            Player player = this.match.getPlayer();

            for (Card c : player.getHand()) {
                if (this.suitCount.get(c.getSuit()) >= 3) {
                    if (c.getRank() ==Rank.TEN || c.getRank() ==Rank.JACK || c.getRank() == Rank.QUEEN
                            || c.getRank() == Rank.KING || c.getRank() == Rank.ACE) {
                        counter++;
                    }
                }
            }
            return (counter >= 3);
        }

        return false;

    }

    /**
     * This method checks if the hand of the player is classified as a "Four to an Outside Straight".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isFourToAnOutsideStraight() {


        int counter = 0;
        boolean toReturn = false;

        for (Rank r : Rank.values()) {
            if (this.rankCount.get(r) >= 1) {
                counter++;
            } else {
                counter = 0;
            }

            if (counter == 4) {
                toReturn = true;
            }
        }

        boolean isException = this.rankCount.get(Rank.JACK) >= 1 &&
                this.rankCount.get(Rank.QUEEN) >= 1 &&
                this.rankCount.get(Rank.KING) >= 1 &&
                this.rankCount.get(Rank.ACE) >= 1;


        return toReturn && (!isException);


    }

    /**
     * This method checks if the hand of the player is classified as a "Four to an Inside Straight".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isFourToAnInsideStraight() {

        boolean isFTIS = (this.rankCount.get(Rank.JACK) >= 1 &&
                this.rankCount.get(Rank.QUEEN) >= 1 &&
                this.rankCount.get(Rank.KING) >= 1 &&
                this.rankCount.get(Rank.ACE) >= 1) ||
                (this.rankCount.get(Rank.TWO) >= 1 &&
                        this.rankCount.get(Rank.THREE) >= 1 &&
                        this.rankCount.get(Rank.FOUR) >= 1 &&
                        this.rankCount.get(Rank.ACE) >= 1);


        int[] counter = new int[5];
        boolean toReturn = false;
        Player player = this.match.getPlayer();
        int finalCounter = 0;

        Card[] card = player.getHand();


        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {

                if (j != i) {

                    if (Math.abs((card[i].getRank()).ordinal() - (card[j].getRank()).ordinal()) <= 4) {
                        counter[i]++;
                    }
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            if (counter[i] >= 3)
                finalCounter++;
        }

        if (finalCounter >= 4) toReturn = true;
        return toReturn || isFTIS;


    }

    /**
     * This method checks if the hand of the player is classified as a "Low Pair".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isLowPair() {

        boolean isLowPair = this.rankCount.get(Rank.TWO) == 2 ||
                this.rankCount.get(Rank.THREE) == 2 ||
                this.rankCount.get(Rank.FOUR) == 2 ||
                this.rankCount.get(Rank.FIVE) == 2 ||
                this.rankCount.get(Rank.SIX) == 2 ||
                this.rankCount.get(Rank.SEVEN) == 2 ||
                this.rankCount.get(Rank.EIGHT) == 2 ||
                this.rankCount.get(Rank.NINE) == 2 ||
                this.rankCount.get(Rank.TEN) == 2;

        return isLowPair;


    }

    /**
     * This method checks if the hand of the player is classified as a "AKQJ Unsuited".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isAKQJUnsuited() {
        boolean isAKQJUnsuited = this.rankCount.get(Rank.ACE) == 1 &&
                this.rankCount.get(Rank.KING) == 1 && this.rankCount.get(Rank.QUEEN) == 1 &&
                this.rankCount.get(Rank.JACK) == 1;

        return isAKQJUnsuited;


    }

    /**
     * This method checks if the hand of the player is classified as a "Three to a Straight Flush type 1".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isThreeToAStraightFlush1() {

        boolean is3TF = this.isThreeToFlush();

        if (is3TF) {

            Player player = this.match.getPlayer();
            int highCardCounter = 0;
            int gapCounter = 0;

            for (Card c : player.getHand()) {
                if (this.suitCount.get(c.getSuit()) == 3) {
                    if (c.getRank() == Rank.JACK || c.getRank() == Rank.QUEEN
                            || c.getRank() == Rank.KING || c.getRank() == Rank.ACE) {

                        highCardCounter++;
                    }
                }
            }

            int counter = 0;
            boolean toReturn = false;

            if (highCardCounter == 0) {


                for (Rank r : Rank.values()) {
                    if (this.rankCount.get(r) == 1) {
                        counter++;
                    } else {
                        counter = 0;
                    }

                    if (counter == 3) {
                        toReturn = true;
                    }
                }

                boolean isException = this.rankCount.get(Rank.TWO) == 1 &&
                        this.rankCount.get(Rank.THREE) == 1 && this.rankCount.get(Rank.FOUR) == 1;


                return toReturn && (!isException);

            } else if (highCardCounter == 1) {


                gapCounter = this.getNumberOfGaps();

                if (gapCounter <= 1) return true;


            } else {


                gapCounter = this.getNumberOfGaps();

                if (gapCounter <= 2) return true;

            }

        }


        return false;


    }

    /**
     * This method checks if the hand of the player is classified as a "Four to an Inside Straight with 3 High Cards".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isFourToAnInsideStraightWithThreeHighCards() {

        boolean is4TIS = this.isFourToAnInsideStraight();
        Player player = this.match.getPlayer();
        Card[] card = player.getHand();

        if(is4TIS) {

            int counter = 0;
            int outsideIndex = this.getFourToAnInsideStraightOutsideIndex();

            for (int i = 0; i < 5; i++) {
                if(i!=outsideIndex) {

                    if (card[i].getRank() == Rank.QUEEN ||
                            card[i].getRank() == Rank.JACK||
                            card[i].getRank() == Rank.KING||
                            card[i].getRank() == Rank.ACE) {
                        counter++;
                    }
                }
            }

            if(counter >= 3) return true;
            else return false;

        }

        return false;


    }

    /**
     * This method checks if the hand of the player is classified as a "QJ Suited".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isQJSuited() {

        if (this.rankCount.get(Rank.JACK) == 1 && this.rankCount.get(Rank.QUEEN) == 1) {
            Player player = this.match.getPlayer();
            Suit s1 = Suit.CLUBS, s2 = Suit.CLUBS;

            for (Card c : player.getHand()) {
                if (c.getRank() == Rank.JACK) {
                    s1 = c.getSuit();
                } else if (c.getRank() == Rank.QUEEN) {
                    s2 = c.getSuit();
                }
            }

            return s1 == s2;

        }
        return false;
    }

    /**
     * This method checks if the hand of the player is classified as a "Three to a Flush with 2 High Cards".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isThreeToAFlushWithTwoHighCards() {

        Player player = this.match.getPlayer();
        int counter = 0;
        int high = 0;

        for (Card c : player.getHand()) {
            if (this.suitCount.get(c.getSuit()) >= 3) {
                if (c.getRank() == Rank.JACK || c.getRank() == Rank.QUEEN
                        || c.getRank() == Rank.KING || c.getRank() == Rank.ACE) {
                    if (high > 1) {
                        return false;
                    } else {
                        high++;
                    }
                }
            }
        }

        return high == 2;

    }

    /**
     * This method checks if the hand of the player is classified as a "Two Suited High Cards".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isTwoSuitedHighCards() {

        Player player = this.match.getPlayer();
        int high = 0;

        for (Card c : player.getHand()) {
            if (this.suitCount.get(c.getSuit()) == 2) {
                if (c.getRank() == Rank.JACK || c.getRank() == Rank.QUEEN
                        || c.getRank() == Rank.KING || c.getRank() == Rank.ACE) {

                    high++;
                }
            }
        }

        if (high == 2) return true;
        else return false;

    }

    /**
     * This method checks if the hand of the player is classified as a "Four to an Inside Straight with 2 High cards".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isFourToAnInsideStraightWithTwoHighCards() {

        boolean is4TIS = this.isFourToAnInsideStraight();
        Player player = this.match.getPlayer();
        Card[] card = player.getHand();

        if(is4TIS) {

            int counter = 0;
            int outsideIndex = this.getFourToAnInsideStraightOutsideIndex();

            for (int i = 0; i < 5; i++) {
                if(i!=outsideIndex) {

                    if (card[i].getRank() == Rank.QUEEN ||
                            card[i].getRank() == Rank.JACK||
                            card[i].getRank() == Rank.KING||
                            card[i].getRank() == Rank.ACE) {
                        counter++;
                    }
                }
            }

            if(counter >= 2) return true;
            else return false;

        }

        return false;

    }

    /**
     * This method checks if the hand of the player is classified as a "Three to a Straight Flush type 2".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isThreeToAStraightFlush2() {

        boolean is3TF = this.isThreeToFlush();

        if (is3TF) {

            Player player = this.match.getPlayer();
            int highCardCounter = 0;
            int gapCounter = 0;

            for (Card c : player.getHand()) {
                if (this.suitCount.get(c.getSuit()) == 3) {
                    if (c.getRank() == Rank.JACK || c.getRank() == Rank.QUEEN
                            || c.getRank() == Rank.KING || c.getRank() == Rank.ACE) {

                        highCardCounter++;
                    }
                }
            }

            gapCounter = this.getNumberOfGaps();
            boolean isOneGap = false;
            boolean isTwoGapsOneHighCard = false;
            boolean isInclusion1 = false;
            boolean isInclusion2 = false;


            if (highCardCounter == 1 && gapCounter == 2) isTwoGapsOneHighCard = true;
            if (highCardCounter == 0 && gapCounter == 1) isOneGap = true;

            isInclusion1 = (this.rankCount.get(Rank.TWO) == 1 &&
                    this.rankCount.get(Rank.THREE) == 1 && this.rankCount.get(Rank.FOUR) == 1);

            isInclusion2 = (this.rankCount.get(Rank.ACE) == 1) && (gapCounter <= 2);

            return isInclusion1 || isInclusion2 || isOneGap || isTwoGapsOneHighCard;


        }


        return false;


    }

    /**
     * This method checks if the hand of the player is classified as a "Four to an Inside Straight with 1 High card".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isFourToAnInsideStraightWithOneHighCards() {

        boolean is4TIS = this.isFourToAnInsideStraight();
        Player player = this.match.getPlayer();
        Card[] card = player.getHand();

        if(is4TIS) {

            int counter = 0;
            int outsideIndex = this.getFourToAnInsideStraightOutsideIndex();

            for (int i = 0; i < 5; i++) {
                if(i!=outsideIndex) {

                    if (card[i].getRank() == Rank.QUEEN ||
                            card[i].getRank() == Rank.JACK||
                            card[i].getRank() == Rank.KING||
                            card[i].getRank() == Rank.ACE) {
                        counter++;
                    }
                }
            }

            if(counter >= 1) return true;
            else return false;

        }

        return false;


    }

    /**
     * This method checks if the hand of the player is classified as a "KQJ Unsuited".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isKQJUnsuited() {


        return (this.rankCount.get(Rank.KING) == 1 &&
                this.rankCount.get(Rank.QUEEN) == 1 &&
                this.rankCount.get(Rank.JACK) == 1);
    }

    /**
     * This method checks if the hand of the player is classified as a "JT Suited".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isJTSuited() {
        //i might have overcomplicated

        boolean isJTSuited = false;
        Player player = this.match.getPlayer();
        Suit s1 = Suit.CLUBS, s2 = Suit.CLUBS;
        boolean j = false;
        boolean t = false;

        for (Card c : player.getHand()) {
            if (c.getRank() == Rank.JACK) {
                j = true;
                s1 = c.getSuit();
            } else if (c.getRank() == Rank.TEN) {
                t = true;
                s2 = c.getSuit();
            }
        }

        if (j && t) {
            return s1 == s2;
        } else {
            return false;
        }


    }

    /**
     * This method checks if the hand of the player is classified as a "QJ Unsuited".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isQJUnsuited() {


        return (this.rankCount.get(Rank.QUEEN) == 1 &&
                this.rankCount.get(Rank.JACK) == 1);

    }

    /**
     * This method checks if the hand of the player is classified as a "Three to a Flush with 1 High card".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isThreeToAFlushWithOneHighCard() {
        Player player = this.match.getPlayer();
        int counter = 0;
        int high = 0;

        for (Card c : player.getHand()) {
            if (this.suitCount.get(c.getSuit()) >= 3) {
                if (c.getRank() == Rank.JACK || c.getRank() == Rank.QUEEN
                        || c.getRank() == Rank.KING || c.getRank() == Rank.ACE) {
                    if (high > 0) {
                        return false;
                    } else {
                        high++;
                    }

                }
            }
        }


        return high == 1;


    }

    /**
     * This method checks if the hand of the player is classified as a "QT Suited".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isQTSuited() {


        if (this.rankCount.get(Rank.QUEEN) == 1 && this.rankCount.get(Rank.TEN) == 1) {
            Player player = this.match.getPlayer();
            Suit s1 = Suit.CLUBS, s2 = Suit.CLUBS;

            for (Card c : player.getHand()) {
                if (c.getRank() == Rank.QUEEN) {
                    s1 = c.getSuit();
                } else if (c.getRank() == Rank.TEN) {
                    s2 = c.getSuit();
                }
            }

            return s1 == s2;

        }
        return false;
    }

    /**
     * This method checks if the hand of the player is classified as a "Three to a Straight Flush type 3".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isThreeToAStraightFlush3() {

        boolean is3TF = this.isThreeToFlush();

        if (is3TF) {

            Player player = this.match.getPlayer();
            int highCardCounter = 0;
            int gapCounter = 0;

            for (Card c : player.getHand()) {
                if (this.suitCount.get(c.getSuit()) == 3) {
                    if (c.getRank() == Rank.JACK || c.getRank() == Rank.QUEEN
                            || c.getRank() == Rank.KING || c.getRank() == Rank.ACE) {

                        highCardCounter++;
                    }
                }
            }

            gapCounter = this.getNumberOfGaps();

            return (gapCounter == 2) && (highCardCounter == 0);


        }


        return false;


    }

    /**
     * This method checks if the hand of the player is classified as a "KQ Unsuited".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isKQUnsuited() {


        return (this.rankCount.get(Rank.KING) == 1 &&
                this.rankCount.get(Rank.QUEEN) == 1);

    }

    /**
     * This method checks if the hand of the player is classified as a "KJ Unsuited".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isKJUnsuited() {


        return (this.rankCount.get(Rank.KING) == 1 &&
                this.rankCount.get(Rank.JACK) == 1);

    }

    /**
     * This method checks if the hand of the player is classified as an "Ace".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isAce() {

        boolean isAce = this.rankCount.get(Rank.ACE) == 1;
        return isAce;


    }

    /**
     * This method checks if the hand of the player is classified as a "KT Suited".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isKTSuited() {

        if (this.rankCount.get(Rank.KING) == 1 && this.rankCount.get(Rank.TEN) == 1) {
            Player player = this.match.getPlayer();
            Suit s1 = Suit.CLUBS, s2 = Suit.CLUBS;

            for (Card c : player.getHand()) {
                if (c.getRank() == Rank.KING) {
                    s1 = c.getSuit();
                } else if (c.getRank() == Rank.TEN) {
                    s2 = c.getSuit();
                }
            }

            return s1 == s2;

        }
        return false;
    }

    /**
     * This method checks if the hand of the player is classified as a "Jack,Queen or King".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isJQK() {

        boolean isJQK = this.rankCount.get(Rank.JACK) == 1 ||
                this.rankCount.get(Rank.QUEEN) == 1 ||
                this.rankCount.get(Rank.KING) == 1;

        return isJQK;


    }

    /**
     * This method checks if the hand of the player is classified as a "Four to an Inside Straight with no High cards".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isFourToAnInsideStraightWithNoHighCards() {

        boolean is4TIS = this.isFourToAnInsideStraight();
        int counter = 0;
        boolean toReturn = false;

        if (this.rankCount.get(Rank.ACE) == 1) counter++;
        if (this.rankCount.get(Rank.KING) == 1) counter++;
        if (this.rankCount.get(Rank.QUEEN) == 1) counter++;
        if (this.rankCount.get(Rank.JACK) == 1) counter++;

        if (counter == 0) toReturn = true;


        return is4TIS && toReturn;


    }

    /**
     * This method checks if the hand of the player is classified as a "Three to a Flush with no High cards".
     * @return Boolean true/false depending on the outcome.
     */
    private boolean isThreeToAFlushWithNoHighCard() {

        Player player = this.match.getPlayer();
        int counter = 0;

        for (Card c : player.getHand()) {
            if (this.suitCount.get(c.getSuit()) >= 3) {
                if (c.getRank() == Rank.JACK || c.getRank() == Rank.QUEEN
                        || c.getRank() == Rank.KING || c.getRank() == Rank.ACE) {
                    return false;
                } else {
                    counter++;
                }
            }
        }


        return counter == 3;

    }

    /**
     * This method returns the Suit that appears "number" times in the hand.
     * @param number Number of times a Suit is supposed to appear in the hand.
     * @return Suit or null if there is no Suit that appears "number" times in hand.
     */
    private Suit getNumberOfAKindSuit(int number) {

        for (Suit s : this.suitCount.keySet()) {
            if (this.suitCount.get(s) >= number) {
                return s;
            }
        }

        return null;
    }

    /**
     * This method returns the Rank that appears twice in the hand (Pair).
     * @return Rank of the Pair or null if there is no Pair.
     */
    private Rank getPairRank() {

        if (this.rankCount.get(Rank.JACK) == 2) return Rank.JACK;
        if (this.rankCount.get(Rank.QUEEN) == 2) return Rank.QUEEN;
        if (this.rankCount.get(Rank.KING) == 2) return Rank.KING;
        if (this.rankCount.get(Rank.ACE) == 2) return Rank.ACE;
        if (this.rankCount.get(Rank.TWO) == 2) return Rank.TWO;
        if (this.rankCount.get(Rank.THREE) == 2) return Rank.THREE;
        if (this.rankCount.get(Rank.FOUR) == 2) return Rank.FOUR;
        if (this.rankCount.get(Rank.FIVE) == 2) return Rank.FIVE;
        if (this.rankCount.get(Rank.SIX) == 2) return Rank.SIX;
        if (this.rankCount.get(Rank.SEVEN) == 2) return Rank.SEVEN;
        if (this.rankCount.get(Rank.EIGHT) == 2) return Rank.EIGHT;
        if (this.rankCount.get(Rank.NINE) == 2) return Rank.NINE;
        if (this.rankCount.get(Rank.TEN) == 2) return Rank.TEN;

        return null;
    }

    /**
     * This method returns the number of gaps present on the hand.
     * Used to classify Straight Flush draws types.
     * @return Number of gaps.
     */
    private int getNumberOfGaps() {

        int gapCounter = 0;

        for (Rank r1 : Rank.values()) {
            if (this.belongsTo(r1)) {
                for (Rank r2 : Rank.values()) {
                    if (this.belongsTo(r2) && r1 != r2 && (r2.ordinal() > r1.ordinal())) {

                        if (Math.abs(r1.ordinal() - r2.ordinal()) > 1) {
                            gapCounter += (Math.abs(r1.ordinal() - r2.ordinal())-1);
                        }
                        break;
                    }
                }
            }
        }

        return gapCounter;

    }

    /**
     * This method checks if a card with a specific Rank has a Suit that appears 3 times in the hand.
     * Used to Classify Straight Flush draws types.
     * @param rank Specific Rank
     * @return Bolean true/false depending on the outcome.
     */
    private boolean belongsTo(Rank rank) {

        Player player = this.match.getPlayer();

        for (Card c : player.getHand()) {
            if (c.getRank() == rank) {
                if (this.suitCount.get(c.getSuit()) == 3) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * This method returns the index of the card that has the highest Rank in the hand.
     * @return Index of the card with the highest Rank.
     */
    private int getBiggerCardIndex() {

        Player player = this.match.getPlayer();
        Card[] card = player.getHand();
        int index = 0;
        Rank rank = Rank.TWO;

        for (Rank r : Rank.values()) {
            if (this.rankCount.get(r) >= 1) {

                rank = r;
            }
        }

        for (int i = 0; i < 5; i++) {
            if (card[i].getRank() == rank) {
                index = i;
            }
        }

        return index;
    }

    /**
     * This method returns the card index that does not belong to a Straight in a "Four to an Inside Straight" draw.
     * @return Index that does not belong to the Straight.
     */
    private int getFourToAnInsideStraightOutsideIndex() {

        int outsideIndex = 0;
        int possibleOutsideIndex = 0;
        int counter = 0;
        int biggerCardIndex = this.getBiggerCardIndex();
        Player player = this.match.getPlayer();

        Card[] card = player.getHand();
        for (int j = 0; j < 5; j++) {
            if(j!=biggerCardIndex) {

                if (Math.abs((card[biggerCardIndex].getRank()).ordinal() - (card[j].getRank()).ordinal()) > 4) {
                    possibleOutsideIndex = j;
                    counter++;
                }
            }

        }

        if (counter > 1) outsideIndex = biggerCardIndex;
        else outsideIndex = possibleOutsideIndex;

        return outsideIndex;

    }

    /**
     * This method returns the Rank that appears 3 times in the hand.
     * @return Rank that appears 3 times or null otherwise.
     */
    private Rank getThreeOfAKindRank() {

        for (Rank r : this.rankCount.keySet()) {
            if (this.rankCount.get(r) == 3) {
                return r;
            }
        }

        return null;
    }


}