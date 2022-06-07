package states;

import match.FullCommand;

public class CanDealCards implements State{

    Match match;

    public CanDealCards(Match match){
        this.match = match;
    }

    @Override
    public void bet(FullCommand command) {
        System.out.println("b: illegal command");
    }

    @Override
    public void printCredit() {
        System.out.println("The player\'s credit is: " + this.match.player.getCredit()+ " \n");
    }

    @Override
    public void dealCards() {
        this.match.player.addCardsToHand(this.match.deck);
        this.match.setState(this.match.getCanHoldCards());
    }

    @Override
    public void holdCards(FullCommand command) {
        System.out.println("h: illegal command");
    }

    @Override
    public void printAdvice() {
        System.out.println("a: illegal command");
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
        for (int value: this.match.player.getStatistics().values()) {
            sum += value;
        }
        System.out.println(String.format("Total                       %s", sum));
        System.out.println("------------------------------");

        float increase = ((float) this.match.player.getCredit()/this.match.player.getInitialCredit() - 1 )*100;
        System.out.println(String.format("Credit                 %s (%s%%)", this.match.player.getCredit(), round(increase,2)));
        System.out.println("------------------------------");
    }

    private static float round (float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }
}
