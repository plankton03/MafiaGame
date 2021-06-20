package Game;

import Controllers.PhaseControllers.*;
import Design.Color;
import Player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The type Game.
 *
 * @author : Fatemeh Abdi
 */
public class Game {

    private LinkedList<Player> players;
    private LinkedList<Player> deadPlayers;
    private ServerSocket serverSocket;
    private int numOFPlayers;
    private Initializer initializer;
    private ArrayList<Player> backupList = new ArrayList<Player>();

    /**
     * Instantiates a new Game.
     *
     * @param serverSocket the server socket
     * @param numOFPlayers the num of players
     */
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

        for (Player player : players) {
            backupList.add(player);
        }
    }

    /**
     * Gets backup list.
     *
     * @return the backup list
     */
    public ArrayList<Player> getBackupList() {
        return backupList;
    }

    /**
     * Start first night.
     */
    public void startFirstNight() {
        FirstNightController firstNightController = new FirstNightController(this);

        firstNightController.startFirstNight();

    }

    /**
     * Game is over boolean.
     *
     * @return the boolean
     */
    public boolean gameIsOver() {
        if (getNumOfMafia() == 0 || getNumOfMafia() >= getNumOFCitizens())
            return true;
        return false;
    }

    /**
     * Announcing the winner.
     */
    public void announcingTheWinner() {
        String name = "\n\n\t\t\t\t";
        if (getNumOfMafia() == 0)
            name += "Citizens";
        else name += "Mafia group";
        name += " are the winner of the game *_*";
        for (Player player : players) {
            try {
                player.getWriter().writeUTF(Color.CYAN_BOLD_BRIGHT + name + Color.RESET);
            } catch (IOException e) {
                players.remove(player);
            }
        }
        for (Player player : deadPlayers) {
            try {
                player.getWriter().writeUTF(Color.CYAN_BOLD_BRIGHT + name + Color.RESET);
            } catch (IOException e) {
                deadPlayers.remove(player);
            }
        }

    }

    /**
     * Get num of citizens int.
     *
     * @return the int
     */
    public int getNumOFCitizens() {
        int count = 0;
        for (Player player : players) {
            if (!player.getRole().isMafia())
                count++;
        }
        return count;
    }

    /**
     * Get num of mafia int.
     *
     * @return the int
     */
    public int getNumOfMafia() {
        int count = 0;
        for (Player player : players) {
            if (player.getRole().isMafia())
                count++;
        }
        return count;
    }

    /**
     * Start day.
     */
    public void startDay() {

        ChatPhaseController chatPhaseController = new ChatPhaseController(this);

        chatPhaseController.startChat();

        System.out.println("suc");
    }

    /**
     * Start night.
     */
    public void startNight() {

        NightPhaseController nightPhaseController = new NightPhaseController(this);

        nightPhaseController.startNightEvents();

    }

    /**
     * Start voting.
     */
    public void startVoting() {
        VotingPhaseController votingPhaseController = new VotingPhaseController(this);

        votingPhaseController.startVotingPhase();

    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public LinkedList<Player> getPlayers() {
        return players;
    }

    /**
     * Sets players.
     *
     * @param players the players
     */
    public void setPlayers(LinkedList<Player> players) {
        this.players = players;
    }

    /**
     * Gets initializer.
     *
     * @return the initializer
     */
    public Initializer getInitializer() {
        return initializer;
    }

    /**
     * Gets dead players.
     *
     * @return the dead players
     */
    public LinkedList<Player> getDeadPlayers() {
        return deadPlayers;
    }

    /**
     * Send to all.
     *
     * @param message the message
     */
    public void sendToAll(String message) {
        for (Player player : players) {
            try {
                player.getWriter().writeUTF(message);
            } catch (IOException e) {
                players.remove(player);
            }
        }

        for (Player player : deadPlayers) {
            try {
                player.getWriter().writeUTF(message);
            } catch (IOException e) {
                players.remove(player);
            }
        }
    }
}
