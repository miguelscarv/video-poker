package main;

import match.FullCommand;
import match.ReadFile;
import player.cards.Deck;
import java.util.List;

public class Main {
    public static void main(String[] args){

        if(args[0].equals("-d")){
            //debug mode

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
