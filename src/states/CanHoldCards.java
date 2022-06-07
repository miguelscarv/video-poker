package states;

import match.FullCommand;
import player.cards.Card;

import java.util.*;

public class CanHoldCards implements State{

    Match match;

    public CanHoldCards(Match match){
        this.match = match;
    }

    @Override
    public void bet(FullCommand command) {
        System.out.println("b: illegal command");
    }

    @Override
    public void printCredit() {
        System.out.println("The player\'s credit is: " + this.match.player.getCredit());
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
            for(int i=0; i < 5; i++){
                if(!contains(command.getNumbers(),i)){
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

    }

    public void computeHandOutcome(){
        System.out.println("STILL NEED TO IMPLEMENT THIS!!!!!");
    }

    @Override
    public void printAdvice() {
        //NEED TO IMPLEMENT THIS!!!!!!!!!!!
    }

    private boolean contains(final int[] arr, final int key) {
        for(int i: arr){
            if (i-1 == key){return true;}
        }
        return false;
    }

    @Override
    public void printStatistics() {

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
        for (int value: this.match.player.getStatistics().values()) {
            sum += value;
        }
        System.out.println(String.format("Total                       %s", sum));
        System.out.println("------------------------------");

        float increase = (1 - (float) this.match.player.getCredit()/this.match.player.getInitialCredit())*100;
        System.out.println(String.format("Credit                 %s (%s%%)", this.match.player.getCredit(), increase));

    }
}
