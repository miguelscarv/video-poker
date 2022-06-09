package game;

public interface GameMode {

    void initPlayer(int credit);
    void initDebugDeck(String pathToFile);
    void initSimulationDeck();
    void initCommands(String pathToFile);
    void initMatch();
    void run();

}
