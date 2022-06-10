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

            Rank rank = this.getThreeOfAKindRank();

            int[] indexArray = new int[3];
            int index = 0;
            Card[] card = player.getHand();
            for (int i=0; i<5; i++) {

                if(card[i].getRank() == rank ){
                    indexArray[index++] = i+1;
                }
            }

        } else if (isFourToAStraightFlush) {



        } else if (isTwoPair) {

        } else if (isJacksOrBetter) {



        } else if (isFourToFlush) {

        } else if (isThreeToARoyalFlush) {

        } else if (isFourToAnOutsideStraight) {

        } else if (isLowPair) {

        } else if (isAKQJUnsuited) {

        } else if (isThreeToAStraightFlush1) {

        } else if (isFourToAnInsideStraightWithThreeHighCards) {

        } else if (isQJSuited) {

        } else if (isThreeToAFlushWithTwoHighCards) {

        } else if (isTwoSuitedHighCards) {

        } else if (isFourToAnInsideStraightWithTwoHighCards) {

        } else if (isThreeToAStraightFlush2) {

        } else if (isFourToAnInsideStraightWithOneHighCards) {

        } else if (isKQJUnsuited) {

        } else if (isJTSuited) {

        } else if (isQJUnsuited) {

        } else if (isThreeToAFlushWithOneHighCard) {

        } else if (isQTSuited) {

        } else if (isThreeToAStraightFlush3) {

        } else if (isKQUnsuited || isKJUnsuited) {

        } else if (isAce) {

        } else if (isKTSuited) {

        } else if (isJQK) {

        } else if (isFourToAnInsideStraightWithNoHighCards) {

        } else if (isThreeToAFlushWithNoHighCard) {


        } else {


        }

        this.setCountersToZero();
        return new int[0];
    }

    @Override
    public void printAdvice(){

        int[] numbers = this.getAdvice();

        if (numbers.length != 0) {
            System.out.println("The player should hold cards " + Arrays.toString(numbers));
        } else {
            System.out.println("The player should hold no cards");
        }

    }

    private boolean isFourToARoyalFlush() {


        return false;


    }

    private boolean isFourToAStraightFlush() {


        return false;


    }

    private boolean isFourToFlush() {


        return false;


    }

    private boolean isThreeToARoyalFlush() {


        return false;


    }

    private boolean isFourToAnOutsideStraight() {


        return false;


    }

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

    private boolean isAKQJUnsuited(){
    	boolean isAKQJUnsuited = this.rankCount.get(Rank.ACE)==1 && 
    			this.rankCount.get(Rank.KING)==1 && this.rankCount.get(Rank.QUEEN)==1 &&
    			this.rankCount.get(Rank.JACK)==1; 

        return isAKQJUnsuited;


    }

    private boolean isThreeToAStraightFlush1(){


        return false;


    }

    private boolean isFourToAnInsideStraightWithThreeHighCards(){


        return false;


    }

    private boolean isQJSuited(){


        return false;


    }

    private boolean isThreeToAFlushWithTwoHighCards(){
    	
    	Player player = this.match.getPlayer();
    	int counter = 0;
    	int high = 0;
    	
    	for (Card c : player.getHand()) {
            if(this.suitCount.get(c.getSuit()) >= 3) {
            	if(c.getRank()==Rank.JACK || c.getRank()==Rank.QUEEN
            			|| c.getRank()==Rank.KING || c.getRank()==Rank.ACE){
            		if(high>1) {
            			return false;
            		}else {
            			high++;
            		}
            	}else {
            		counter++;
            	}
            }
        }
    	
        
    	return counter==3;


    }

    private boolean isTwoSuitedHighCards(){


        return false;


    }

    private boolean isFourToAnInsideStraightWithTwoHighCards(){


        return false;


    }

    private boolean isThreeToAStraightFlush2(){


        return false;


    }

    private boolean isFourToAnInsideStraightWithOneHighCards(){


        return false;


    }

    private boolean isKQJUnsuited(){


        return false;


    }


    private boolean isJTSuited(){
    	//i might have overcomplicated
    	
    	boolean isJTSuited = false;
    	Player player = this.match.getPlayer();
    	Suit s1 = Suit.CLUBS, s2 = Suit.CLUBS;
    	boolean j = false;
    	boolean t = false;
    	
    	for (Card c : player.getHand()) {
            if(c.getRank() == Rank.JACK) {
            	j = true;
            	s1 = c.getSuit();
            }else if(c.getRank() == Rank.TEN) {
            	t = true;
            	s2 = c.getSuit();
            }
        }
    	
    	if(j && t) {
    		return s1==s2;
    	}else {
    		return false;
    	}


    }

    private boolean isQJUnsuited(){


        return false;


    }

    private boolean isThreeToAFlushWithOneHighCard(){
    	Player player = this.match.getPlayer();
    	int counter = 0;
    	int high = 0;
    	
    	for (Card c : player.getHand()) {
            if(this.suitCount.get(c.getSuit()) >= 3) {
            	if(c.getRank()==Rank.JACK || c.getRank()==Rank.QUEEN
            			|| c.getRank()==Rank.KING || c.getRank()==Rank.ACE){
            		if(high>0) {
            			return false;
            		}else {
            			high++;
            		}
            	}else {
            		counter++;
            	}
            }
        }
    	
        
    	return counter==3;
    	


    }

    private boolean isQTSuited(){


        return false;


    }

    private boolean isThreeToAStraightFlush3(){
    	
    	return false;


    }

    private boolean isKQUnsuited(){


        return false;


    }

    private boolean isKJUnsuited(){


        return false;


    }

    private boolean isAce(){

    	boolean isAce = this.rankCount.get(Rank.ACE) == 1;
        return isAce;


    }

    private boolean isKTSuited(){


        return false;


    }

    private boolean isJQK(){

    	boolean isJQK = this.rankCount.get(Rank.JACK) == 1 || 
    			this.rankCount.get(Rank.QUEEN) == 1 ||
    			this.rankCount.get(Rank.KING) == 1;
    	
    	return isJQK;


    }

    private boolean isFourToAnInsideStraightWithNoHighCards(){


        return false;


    }

    private boolean isThreeToAFlushWithNoHighCard(){
    	 
    	Player player = this.match.getPlayer();
    	int counter = 0;
    	
    	for (Card c : player.getHand()) {
            if(this.suitCount.get(c.getSuit()) >= 3) {
            	if(c.getRank()==Rank.JACK || c.getRank()==Rank.QUEEN
            			|| c.getRank()==Rank.KING || c.getRank()==Rank.ACE) {
            		return false;
            	}else {
            		counter++;
            	}
            }
        }
    	
        
    	return counter==3;

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