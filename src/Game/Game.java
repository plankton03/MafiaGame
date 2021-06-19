package Game;

import Controllers.PhaseControllers.ChatPhaseController;
import Controllers.PhaseControllers.FirstNightController;
import Controllers.PhaseControllers.NightPhaseController;
import Controllers.PhaseControllers.VotingPhaseController;
import Player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Game {

    private LinkedList<Player> players;
    private LinkedList<Player> deadPlayers;
    private ServerSocket serverSocket;
    private int numOFPlayers;
    private Initializer initializer;
    private ArrayList<Player> backupList = new ArrayList<Player>();

    public Game(ServerSocket serverSocket, int numOFPlayers) {
        this.serverSocket = serverSocket;
        this.numOFPlayers = numOFPlayers;
        players = new LinkedList<Player>();
        deadPlayers = new LinkedList<Player>();

        initializer = new Initializer(serverSocket, numOFPlayers, this);
        try {
            initializer.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Player player : players){
            backupList.add(player);
        }
    }

    public ArrayList<Player> getBackupList() {
        return backupList;
    }

    public void startFirstNight() {
        FirstNightController firstNightController = new FirstNightController(this);

        firstNightController.startFirstNight();

    }

    public void startDay() {

        ChatPhaseController chatPhaseController = new ChatPhaseController(this);

        chatPhaseController.startChat();
    }
    public void startNight() {

        NightPhaseController nightPhaseController = new NightPhaseController(this);

        nightPhaseController.startNightEvents();

    }
    public void startVoting(){
        VotingPhaseController votingPhaseController = new VotingPhaseController(this);

        votingPhaseController.startVotingPhase();

    }

    public LinkedList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(LinkedList<Player> players) {
        this.players = players;
    }

    public Initializer getInitializer() {
        return initializer;
    }

    public LinkedList<Player> getDeadPlayers() {
        return deadPlayers;
    }

    public void sendToAll(String message) throws IOException {
        for (Player player : players) {
            player.getWriter().writeUTF(message);
        }
    }
}
