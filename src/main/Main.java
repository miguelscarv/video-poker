package main;

import match.FullCommand;
import match.ReadFile;
import player.cards.Deck;
import states.State;
import states.Timetocheck;
import states.Timetodeal;
import states.Timetodecide;


import java.util.List;

public class Main {
    public static void main(String[] args){

        if(args[0].equals("-d")){
            //debug mode

            State.timetodeal = new Timetodeal();
            State.timetodecide = new Timetodecide();
            State.timetocheck = new Timetocheck();
            State.current = State.timetobet;

            ReadFile.readCommandFile("/Users/JB/Desktop/cmd-file.txt");
            List<FullCommand> fullCommandList2 = ReadFile.fullCommandList;

            for (FullCommand command: fullCommandList2){
                State.current.enter(command);
                State.current.update(command);
            }





        }else if (args[0].equals("-s")){
            //simulation mode

        }else{

            System.out.println("Invalid passing Arguments");
            System.exit(0);
        }

        ReadFile.readCardFile("/Users/miguelcarvalho/Desktop/card-file.txt");
        Deck deck = new Deck(ReadFile.deck);

        ReadFile.readCommandFile("/Users/miguelcarvalho/Desktop/cmd-file.txt");
        List<FullCommand> fullCommandList = ReadFile.fullCommandList;

        for (FullCommand command: fullCommandList){
            System.out.println(command);
        }

    }
}
