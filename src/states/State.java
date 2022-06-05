package states;

import match.FullCommand;

public abstract class State {

    public static State timetobet;
    public static State timetodeal;
    public static State timetodecide;
    public static State timetocheck;
    public static State current;

    public void enter(FullCommand fullcommand){}
    public void update(FullCommand fullcommand){}


}
