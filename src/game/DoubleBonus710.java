package game;

import command.CommandType;
import command.FullCommand;
import game.match.CanHoldCards;
import game.match.Match;
import game.match.Reader;
import player.Player;
import player.cards.Deck;

import java.util.List;

/**
 * DoubleBonus710 class represents the actual specific video poker game that is being played.
 * In this case our game is a variation of jacks or better with a bonus payout for four aces.
 * @author Miguel Carvalho, Filipe Ferraz, Joao Baptista
 */
public class DoubleBonus710 implements GameMode{

    private boolean isDebug;
    private int betAmount;
    private int numberOfDeals;
    private Player player;
    private Deck deck;
    private List<FullCommand> fullCommandList;
    private Match match;

    /**
     * Constructor to initialize game object from an initial credit, a card and a command file (Debug Mode).
     * @param playerCredit Initial player credit.
     * @param pathToCards String representing the path to the card file.
     * @param pathToCommands String representing the path to the command file.
     */
    public DoubleBonus710(int playerCredit, String pathToCommands, String pathToCards){

        this.isDebug = true;
        this.initPlayer(playerCredit);

        this.initDebugDeck(pathToCards);
        this.initCommands(pathToCommands);

        this.initMatch();
    }

    /**
     * Constructor to initialize game object from an initial credit, a bet amount and a number of deals(Simulation Mode)
     * @param playerCredit Initial credit of the player.
     * @param betAmount Amount that will be bet in all rounds.
     * @param numberOfDeals Number of deals (number of rounds played).
     */
    public DoubleBonus710(int playerCredit, int betAmount, int numberOfDeals){

        this.isDebug = false;
        this.betAmount = betAmount;
        this.numberOfDeals = numberOfDeals;

        this.initPlayer(playerCredit);

        this.initSimulationDeck();

        this.initMatch();
    }

    /**
     * This method creates and initializes all the information of the player.
     * @param credit Initial credit of the player.
     */
    @Override
    public void initPlayer(int credit) {
        this.player = new Player(credit);
    }

    /**
     * This method creates a deck from the card file (Debug Mode).
     * @param pathToFile Path to the card file.
     */
    @Override
    public void initDebugDeck(String pathToFile) {
        this.deck = new Deck(Reader.readCardFile(pathToFile));
    }

    /**
     * This method creates a deck with all the 52 cards (Simulation Mode).
     */
    @Override
    public void initSimulationDeck() {
        this.deck = new Deck();
    }

    /**
     * This method creates a list with all the commands read from the command file (Debug Mode).
     * @param pathToFile Path to the command file.
     */
    @Override
    public void initCommands(String pathToFile) {
        this.fullCommandList = Reader.readCommandFile(pathToFile);
    }

    /**
     * This method creates a match in which a player and a deck would be created.
     */
    @Override
    public void initMatch() {
        this.match = new Match(this.player,this.deck,this.isDebug);
    }

    /**
     * This method starts the full implementation of the game.
     * In Debug mode, the program finish when all the commands are executed.
     * In Simulation mode, the program finish when all the rounds are completed, after the statistics are printed.
     */
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

            for (int i = 0; i < this.numberOfDeals; i++){

                FullCommand betCommand = new FullCommand(CommandType.BET);
                betCommand.addNumbers(new int[]{this.betAmount});
                this.match.bet(betCommand);

                this.match.dealCards();

                CanHoldCards chc = (CanHoldCards) this.match.getCanHoldCards();
                int[] numbers = chc.getAdvice();
                FullCommand holdCommand = new FullCommand(CommandType.HOLD);
                holdCommand.addNumbers(numbers);
                this.match.holdCards(holdCommand);

            }

            this.match.printStatistics();

        }

    }

}
