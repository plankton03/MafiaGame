package ServerClient;

import Game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

/**
 * The type Server.
 *
 * @author : Fatemeh Abdi
 */
public class Server {


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        Server server = new Server();
        final int port = 7000;

        try {

            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Consider the following to connect to the server\n");
            System.out.println("port : " + port);
            int numOfPlayers = getNumOfPlayers();
            Game game = new Game(serverSocket, numOfPlayers);

            game.startFirstNight();

            while (true) {

                game.startDay();

                game.startVoting();
                if (game.gameIsOver())
                    break;
                game.startNight();
                if (game.gameIsOver())
                    break;
            }

            Thread.sleep(5 * 1000);

            game.announcingTheWinner();

            game.sendToAll("Exit");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     * Gets num of players.
     *
     * @return the num of players
     */
    public static int getNumOfPlayers() {

        System.out.println("Please enter the number of the players ...\n(Preferred more than 8)");
        Scanner scanner = new Scanner(System.in);
        int nop = scanner.nextInt();
        System.out.println("Mafia game will create based on " + nop + " players :)");
        return nop;
    }

}
