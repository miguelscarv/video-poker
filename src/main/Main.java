package main;

import match.FullCommand;
import match.Reader;
import player.cards.Card;
import player.cards.Deck;
import player.cards.Deck;
import states.*;



import java.util.List;

public class Main {
    public static void main(String[] args){

        if (args[0].equals("-d")){
            State.timetodeal = new Timetodeal();
            State.timetodecide = new Timetodecide();
            State.timetocheck = new Timetocheck();
            State.timetobet = new Timetobet();
            State.current = State.timetobet;

            Reader.readCommandFile("/Users/JB/Desktop/cmd-file.txt");
            List<FullCommand> fullCommandList = Reader.readCommandFile("/Users/miguelcarvalho/Desktop/cmd-file.txt");

            for (FullCommand command: fullCommandList){
                State.current.enter(command);
                State.current.update(command);
            }


        }else if (args[0].equals("-s")){
            //simulation mode

        }else{

            System.out.println("Invalid passing Arguments");
            System.exit(-1);
        }

        Deck deck = new Deck(Reader.readCardFile("/Users/miguelcarvalho/Desktop/card-file.txt"));


        List<FullCommand> fullCommandList = Reader.readCommandFile("/Users/miguelcarvalho/Desktop/cmd-file.txt");

        for (FullCommand command: fullCommandList){
            System.out.println(command);
        }

        deck.shuffle();
        deck.printCards(10);

    }
}
