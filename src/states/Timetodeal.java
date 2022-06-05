package states;

import match.FullCommand;

public class Timetodeal extends State {

    public void enter(FullCommand fullcommand){


    }

    public void update(FullCommand fullcommand){

        switch(fullcommand.command){

            case BET:
                System.out.println("Invalid command, please Deal");
                current = timetodeal;
                break;

            case DEAL:
                // cenas do deal
                current = timetodecide;
                break;

            case HOLD:
                System.out.println("Invalid command, please Deal");
                current = timetodeal;
                break;
            case ADVICE:
                System.out.println("Invalid command, please Deal");
                current = timetodeal;
                break;
            case CREDIT:
                // fazer player get credit
                current = timetodeal;
            case STATISTICS:
                // mostrar estatísticas
                current = timetodeal;
                break;
            default:
                System.out.println("Invalid command");
                current = timetodeal;
        }


    }
}
