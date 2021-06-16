package ServerClient;

import Game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {


    public static void main(String[] args) {

        Server server = new Server();
        final int port = 8000;

        //TODO : get the num of players
        //TODO : create game players and roles
        //TODO : create chat controller

        try {

            ServerSocket serverSocket = new ServerSocket(port);
            int numOfPlayers = getNumOfPlayers();
            Game game = new Game(serverSocket, numOfPlayers);

            System.out.println("Starting first night ... ");
            game.startFirstNight();
            game.sendToAll("Exit");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static int getNumOfPlayers() {
        System.out.println("Consider the following to connect to the server\n");
//        System.out.println("IP Address : "+ "84.47.251.185");
        System.out.println("port : " + 8000);
        System.out.println("Please enter the number of the players ...\n(Preferred more than 8)");
        Scanner scanner = new Scanner(System.in);
        int nop = scanner.nextInt();
        System.out.println("Mafia game will create based on " + nop + " players :)");
        return nop;
    }

}
