import Game.Game;
import Game.Initializer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {


    public static void main(String[] args) {
        final int port = 8000;

        //TODO : get the num of players
        //TODO : create game players and roles
        //TODO : create chat controller

        try {
            ServerSocket serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int numOfPlayers = getNumOfPlayers();
        Game game = new Game();

    }

    public static int getNumOfPlayers(){
        System.out.println("Please enter the number of the players ...\n(Preferred more than 8)");
        Scanner scanner = new Scanner(System.in);
        int nop = scanner.nextInt();
        return nop;
    }

}
