package states;

import match.FullCommand;

public class Timetobet extends State{

    public void enter(FullCommand fullcommand){


    }

    public void update(FullCommand fullcommand){

        switch(fullcommand.command){

            case BET:
                //do everything needed to make a bet
                current = timetodeal;
                break;

            case DEAL:
                System.out.println("Invalid command, please Bet before");
                current = timetobet;
                break;

            case HOLD:
                System.out.println("Invalid command, please Bet before");
                current = timetobet;
                break;
            case ADVICE:
                System.out.println("Invalid command, please Bet before");
                current = timetobet;
                break;
            case CREDIT:
                // fazer player get credit
                current = timetobet;
            case STATISTICS:
                // mostrar estatísticas
                current = timetobet;
                break;
            default:
                System.out.println("Invalid command");
                current = timetobet;
        }


    }

}
