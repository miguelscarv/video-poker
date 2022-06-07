package states;

import match.FullCommand;
import player.Player;

public interface State {

    void bet(FullCommand command);
    void printCredit();
    void dealCards();
    void holdCards(FullCommand command);
    void printAdvice();
    void printStatistics();

}
