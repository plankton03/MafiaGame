package Game;

import Controllers.PhaseControllers.FirstNightController;
import Player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;

public class Game {

    private LinkedList<Player> players;
    private LinkedList<Player> deadPlayers;
    private ServerSocket serverSocket;
    private int numOFPlayers;

    public Game(ServerSocket serverSocket, int numOFPlayers) {
        this.serverSocket = serverSocket;
        this.numOFPlayers = numOFPlayers;
        players = new LinkedList<Player>();
        deadPlayers = new LinkedList<Player>();

        Initializer initializer= new Initializer(serverSocket,numOFPlayers,this);
        try {
            initializer.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToAll(String message){
        for (Player player: players){
            player.getWriter().println(message);
        }
    }

    public void startFirstNight(){
        FirstNightController firstNightController = new FirstNightController(this);

        firstNightController.startFirstNight();

    }

    public LinkedList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(LinkedList<Player> players) {
        this.players = players;
    }
}
