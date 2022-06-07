package states;

import match.FullCommand;
import player.cards.Card;
import player.cards.Rank;
import player.cards.Suit;

import java.util.*;

public class CanHoldCards implements State {

    Match match;
    private Map<Suit, Integer> suitCount = new HashMap<Suit, Integer>();
    private Map<Rank, Integer> rankCount = new HashMap<Rank, Integer>();

    public CanHoldCards(Match match) {
        this.match = match;
        this.setCountersToZero();
    }

    @Override
    public void bet(FullCommand command) {
        System.out.println("b: illegal command");
    }

    @Override
    public void printCredit() {
        System.out.println("The player\'s credit is: " + this.match.player.getCredit() + " \n");
    }

    @Override
    public void dealCards() {
        System.out.println("d: illegal command");
    }

    @Override
    public void holdCards(FullCommand command) {

        if (command.hasNumbers()) {

            List<Card> toRemoveList = new ArrayList<Card>();

            //THE COMMAND NUMBERS TELLS YOU WHAT CARDS TO HOLD ON TOO!!!!!
            for (int i = 0; i < 5; i++) {
                if (!contains(command.getNumbers(), i)) {
                    toRemoveList.add(this.match.player.getHand()[i]);
                }
            }

            Card[] toRemoveArray = new Card[toRemoveList.size()];
            toRemoveList.toArray(toRemoveArray);
            this.match.player.removeCardsFromHand(toRemoveArray);
            this.match.player.addCardsToHand(this.match.deck);


        } else {
            this.match.player.addHandCardsToDeck(this.match.deck);
            this.match.player.addCardsToHand(this.match.deck);
        }

        this.computeHandOutcome();
        this.match.setState(this.match.getCanBet());

    }

    private void computeHandOutcome() {

        String typeOfHand = this.classifyHand();
        System.out.println("Hand: " + Arrays.toString(this.match.player.getHand()));
        System.out.println("Hand type: " + typeOfHand);
        if (typeOfHand.equals("Royal Flush")) {
            if (this.match.player.getLastBetAmount() == 5) {
                this.match.player.setCredit(this.match.player.getCredit() + 4000);
            } else {
                this.match.player.setCredit(this.match.player.getCredit() + this.match.player.getLastBetAmount() * Payout.royalFlush);
            }
        } else if (typeOfHand.equals("Straight Flush")) {
            this.match.player.setCredit(this.match.player.getCredit() + this.match.player.getLastBetAmount() * Payout.straightFlush);
        } else if (typeOfHand.equals("Four of a Kind")) {
            if (this.rankCount.get(Rank.ACE) == 4) {
                this.match.player.setCredit(this.match.player.getCredit() + this.match.player.getLastBetAmount() * Payout.fourAces);
            } else if (this.rankCount.get(Rank.TWO) >= 4 || this.rankCount.get(Rank.THREE) >= 4 || this.rankCount.get(Rank.FOUR) >= 4) {
                this.match.player.setCredit(this.match.player.getCredit() + this.match.player.getLastBetAmount() * Payout.four2_4);
            } else {
                this.match.player.setCredit(this.match.player.getCredit() + this.match.player.getLastBetAmount() * Payout.four5_K);
            }
        } else if (typeOfHand.equals("Full house")) {
            this.match.player.setCredit(this.match.player.getCredit() + this.match.player.getLastBetAmount() * Payout.fullHouse);
        } else if (typeOfHand.equals("Flush")) {
            this.match.player.setCredit(this.match.player.getCredit() + this.match.player.getLastBetAmount() * Payout.flush);
        } else if (typeOfHand.equals("Straight")) {
            this.match.player.setCredit(this.match.player.getCredit() + this.match.player.getLastBetAmount() * Payout.straight);
        } else if (typeOfHand.equals("Three of a Kind")) {
            this.match.player.setCredit(this.match.player.getCredit() + this.match.player.getLastBetAmount() * Payout.threeOfAKind);
        } else if (typeOfHand.equals("Two Pair")) {
            this.match.player.setCredit(this.match.player.getCredit() + this.match.player.getLastBetAmount() * Payout.twoPair);
        } else if (typeOfHand.equals("Jacks or Better")) {
            this.match.player.setCredit(this.match.player.getCredit() + this.match.player.getLastBetAmount() * Payout.jacksOrBetter);
        }

        this.match.player.addOneToStatistics(typeOfHand);
        this.match.player.addHandCardsToDeck(this.match.deck);

        if (!this.match.isDebugMode) {
            this.match.deck.shuffle();
        }

        this.setCountersToZero();
    }

    private String classifyHand() {

        for (Card c : this.match.player.getHand()) {
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

    @Override
    public void printAdvice() {
        System.out.println("\nSTILL NEED TO IMPLEMENT printAdvice!!!!!!\n");
    }

    private boolean contains(final int[] arr, final int key) {
        for (int i : arr) {
            if (i - 1 == key) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void printStatistics() {

        System.out.println("------------------------------");
        System.out.println("Hand");
        System.out.println("------------------------------");

        System.out.println(String.format("Jacks or Better             %s", this.match.player.getStatistics().get("Jacks or Better")));
        System.out.println(String.format("Two Pair                    %s", this.match.player.getStatistics().get("Two Pair")));
        System.out.println(String.format("Three of a Kind             %s", this.match.player.getStatistics().get("Three of a Kind")));
        System.out.println(String.format("Straight                    %s", this.match.player.getStatistics().get("Straight")));
        System.out.println(String.format("Flush                       %s", this.match.player.getStatistics().get("Flush")));
        System.out.println(String.format("Full house                  %s", this.match.player.getStatistics().get("Full house")));
        System.out.println(String.format("Four of a Kind              %s", this.match.player.getStatistics().get("Four of a Kind")));
        System.out.println(String.format("Straight Flush              %s", this.match.player.getStatistics().get("Straight Flush")));
        System.out.println(String.format("Royal Flush                 %s", this.match.player.getStatistics().get("Royal Flush")));
        System.out.println(String.format("Other                       %s", this.match.player.getStatistics().get("Other")));

        System.out.println("------------------------------");
        int sum = 0;
        for (int value : this.match.player.getStatistics().values()) {
            sum += value;
        }
        System.out.println(String.format("Total                       %s", sum));
        System.out.println("------------------------------");

        float increase = ((float) this.match.player.getCredit() / this.match.player.getInitialCredit() - 1) * 100;
        System.out.println(String.format("Credit                 %s (%s%%)", this.match.player.getCredit(), round(increase,2)));
        System.out.println("------------------------------");
    }

    private static float round(float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }
}