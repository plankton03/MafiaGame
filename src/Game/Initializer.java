package Game;

import Player.Player;
import Roles.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

/**
 * The type Initializer.
 *
 * @author : Fatemeh Abdi
 */
public class Initializer {
    private ServerSocket serverSocket;
    private int numOfPlayers;
    private Game game;
    private Vector<String> names = new Vector<>();


    /**
     * Instantiates a new Initializer.
     *
     * @param serverSocket the server socket
     * @param numOfPlayers the num of players
     * @param game         the game
     */
    public Initializer(ServerSocket serverSocket, int numOfPlayers, Game game) {
        this.serverSocket = serverSocket;
        this.numOfPlayers = numOfPlayers;
        this.game = game;
    }

    /**
     * Initialize.
     *
     * @throws IOException the io exception
     */
    public void initialize() throws IOException {

        LinkedList<Role> roles = createRoles();

        LinkedList<Player> players = createPlayers();

        matchPlayerRole(players, roles);

        runPlayers(players);

        while (true) {
            if (initializedOver(players))
                break;
        }
        updateGameState(players);

        System.out.println("\nGame preparation is complete.\n");
    }


    /**
     * Create roles linked list.
     *
     * @return the linked list
     */
    public LinkedList<Role> createRoles() {
        LinkedList<Role> roles = new LinkedList<>();
        for (int i = 1; i <= numOfPlayers; i++) {
            if (i % 3 == 0) {
                if (i == 3)
                    roles.add(new GotFather());
                else if (i == 6)
                    roles.add(new DrLector());
                else
                    roles.add(new SimMafia());

            } else {
                if (i == 1)
                    roles.add(new Detective());
                else if (i == 2)
                    roles.add(new DrCity());
                else if (i == 4)
                    roles.add(new Mayor());
                else if (i == 5)
                    roles.add(new Champion());
                else if (i == 7)
                    roles.add(new Pitman());
                else if (i == 8)
                    roles.add(new Psychologist());
                else
                    roles.add(new SimCitizen());
            }
        }

        Collections.shuffle(roles);
        Collections.shuffle(roles);

        return roles;
    }

    /**
     * Create players linked list.
     *
     * @return the linked list
     * @throws IOException the io exception
     */
    public LinkedList<Player> createPlayers() throws IOException {

        LinkedList<Player> players = new LinkedList<Player>();
        for (int i = 1; i <= numOfPlayers; i++) {
            players.add(new Player(serverSocket.accept(), game));
            System.out.println("Player " + i + " connected ");
            players.get(players.size() - 1).getWriter().writeUTF("You are connected to the game server :)");
        }
        return players;
    }

    /**
     * Add new name.
     *
     * @param name the name
     */
    public void addNewName(String name) {
        names.add(name);
    }

    /**
     * Gets names.
     *
     * @return the names
     */
    public Vector<String> getNames() {
        return names;
    }

    /**
     * Match player role.
     *
     * @param players the players
     * @param roles   the roles
     */
    public void matchPlayerRole(LinkedList<Player> players, LinkedList<Role> roles) {
        for (int i = 0; i < numOfPlayers; i++) {
            players.get(i).setRole(roles.get(i));
        }
    }

    /**
     * Run players.
     *
     * @param players the players
     */
    public void runPlayers(LinkedList<Player> players) {
        for (Player player : players) {
            new Thread(player).start();
        }
    }

    /**
     * Update game state.
     *
     * @param players the players
     */
    public void updateGameState(LinkedList<Player> players) {
        game.setPlayers(players);
    }

    private boolean initializedOver(LinkedList<Player> players) {
        synchronized (players) {
            for (Player player : players) {
                if (!player.isAlive())
                    return false;
            }
            return true;
        }
    }
}
