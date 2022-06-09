package game;

import command.CommandType;
import command.FullCommand;
import game.match.Match;
import game.match.Reader;
import player.Player;
import player.cards.Deck;

import java.util.List;

public class DoubleBonus710 implements GameMode{

    private boolean isDebug;
    private Player player;
    private Deck deck;
    private List<FullCommand> fullCommandList;
    private Match match;


    public DoubleBonus710(int playerCredit, String pathToCards, String pathToCommands){

        this.isDebug = true;
        this.initPlayer(playerCredit);

        this.initDebugDeck(pathToCards);
        this.initCommands(pathToCommands);

        this.initMatch();
    }

    public DoubleBonus710(int playerCredit){

        this.isDebug = false;
        this.initPlayer(playerCredit);

        this.initSimulationDeck();

        this.initMatch();
    }

    @Override
    public void initPlayer(int credit) {
        this.player = new Player(credit);
    }
    @Override
    public void initDebugDeck(String pathToFile) {
        this.deck = new Deck(Reader.readCardFile(pathToFile));
    }
    @Override
    public void initSimulationDeck() {
        this.deck = new Deck();
    }
    @Override
    public void initCommands(String pathToFile) {
        this.fullCommandList = Reader.readCommandFile(pathToFile);
    }
    @Override
    public void initMatch() {
        this.match = new Match(this.player,this.deck,this.isDebug);
    }
    @Override
    public void run() {

        if (this.isDebug){

            for(FullCommand command: this.fullCommandList){

                if (command.getCommand() == CommandType.BET){
                    this.match.bet(command);
                } else if (command.getCommand() == CommandType.DEAL){
                    this.match.dealCards();
                } else if (command.getCommand() == CommandType.HOLD){
                    this.match.holdCards(command);
                } else if (command.getCommand() == CommandType.ADVICE){
                    this.match.printAdvice();
                } else if (command.getCommand() == CommandType.CREDIT){
                    this.match.printCredit();
                } else if (command.getCommand() == CommandType.STATISTICS){
                    this.match.printStatistics();
                }

            }

        } else  {

            System.out.println("STLL NEED TO IMPLEMENT THE SIMULATION VERSION OF THIS GAME MODE");

        }

    }

}
