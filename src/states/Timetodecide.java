package states;

import match.FullCommand;

public class Timetodecide extends State{

    public void enter(FullCommand fullcommand){


    }

    public void update(FullCommand fullcommand){

        switch(fullcommand.getCommand()){

            case BET:
                System.out.println("Invalid command, please choose cards to hold");
                current = timetodecide;
                break;

            case DEAL:
                System.out.println("Invalid command, please choose cards to hold");
                current = timetodecide;
                break;

            case HOLD:
                // cenas do hold
                current = timetocheck;
                break;
            case ADVICE:
                //cenas do advice
                current = timetocheck;
                break;
            case CREDIT:
                // fazer player get credit
                current = timetodecide;
            case STATISTICS:
                // mostrar estatísticas
                current = timetodecide;
                break;
            default:
                System.out.println("Invalid command");
                current = timetodecide;
        }


    }


}
