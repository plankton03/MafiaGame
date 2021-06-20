package Game;

import Controllers.PhaseControllers.*;
import Design.Color;
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

    public boolean gameIsOver(){
        if (getNumOfMafia() == 0 || getNumOfMafia() >= getNumOFCitizens())
            return true;
        return false;
    }

    public void announcingTheWinner(){
        String name = "\n\n\t\t\t\t";
        if (getNumOfMafia() == 0)
            name += "Citizens";
        else name += "Mafia group";
        name += " are the winner of the game *_*";
        for (Player player : players) {
            try {
                player.getWriter().writeUTF(Color.CYAN_BOLD_BRIGHT+name+Color.RESET);
            } catch (IOException e) {
                players.remove(player);
            }
        }
        for (Player player : deadPlayers) {
            try {
                player.getWriter().writeUTF(Color.CYAN_BOLD_BRIGHT+name+Color.RESET);
            } catch (IOException e) {
                deadPlayers.remove(player);
            }
        }

    }

    public int getNumOFCitizens(){
        int count = 0;
        for (Player player : players){
            if (!player.getRole().isMafia())
                count++;
        }
        return count;
    }

    public int getNumOfMafia(){
        int count = 0;
        for (Player player : players){
            if (player.getRole().isMafia())
                count++;
        }
        return count;
    }

    public void startDay() {

        ChatPhaseController chatPhaseController = new ChatPhaseController(this);

        chatPhaseController.startChat();

        System.out.println("suc");
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

    public void sendToAll(String message){
        for (Player player : players) {
            try {
                player.getWriter().writeUTF(message);
            } catch (IOException e) {
                players.remove(player);
            }
        }

        for (Player player : deadPlayers){
            try {
                player.getWriter().writeUTF(message);
            } catch (IOException e) {
                players.remove(player);
            }
        }
    }
}
