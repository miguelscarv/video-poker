package game;

/**
 * Interface GameMode represents the actual game of cards that is being played.
 * The specifications for the actual game are specified by a class that implements this interface.
 * @author Miguel Carvalho, Filipe Ferraz, João Baptista
 */
public interface GameMode {

    void initPlayer(int credit);
    void initDebugDeck(String pathToFile);
    void initSimulationDeck();
    void initCommands(String pathToFile);
    void initMatch();
    void run();

}
