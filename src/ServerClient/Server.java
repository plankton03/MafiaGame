package ServerClient;

import Game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {


    public static void main(String[] args) {

        Server server = new Server();
        final int port = 7000;

        //TODO : get the num of players
        //TODO : create game players and roles
        //TODO : create chat controller

        try {

            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Consider the following to connect to the server\n");
            System.out.println("port : " + port);
            int numOfPlayers = getNumOfPlayers();
            Game game = new Game(serverSocket, numOfPlayers);

            System.out.println("\nStarting first night ... ");
            game.startFirstNight();
            System.out.println("\nThe first night is over.");

            game.startDay();




            game.sendToAll("Exit");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static int getNumOfPlayers() {

        System.out.println("Please enter the number of the players ...\n(Preferred more than 8)");
        Scanner scanner = new Scanner(System.in);
        int nop = scanner.nextInt();
        System.out.println("Mafia game will create based on " + nop + " players :)");
        return nop;
    }

}
