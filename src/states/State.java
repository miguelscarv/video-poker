package states;

import match.CommandType;
import match.FullCommand;
import player.Player;

public abstract class State {

    public static State timetobet;
    public static State timetodeal;
    public static State timetodecide;
    public static State timetocheck;
    public static State current;

    static Player player;

    public void enter(FullCommand fullcommand){}
    public void update(FullCommand fullcommand){}



}
