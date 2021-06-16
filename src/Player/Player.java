package Player;

import Game.Game;
import Roles.Role;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class Player implements Runnable {
    private Game game;
    private boolean isAlive;
    private String name;
    private Role role;
    private Socket socket;
    private DataInputStream reader;
    private DataOutputStream writer;

    //TODO : in run:
    // 1. get name
    // 2. check name
    // 3. send part for player

    public Player(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
        isAlive = false;
        try {
            reader = new DataInputStream(socket.getInputStream());
            writer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public void run() {
        try {
            writer.writeUTF("\n\n\nWelcome to the Mafia game *_*\n\n\nPlease enter a username for yourself ...");
            while (true) {
                String chosenName = reader.readUTF();
                if (!game.getInitializer().getNames().contains(chosenName) && !chosenName.isBlank()) {
                    game.getInitializer().addNewName(chosenName);
                    name = chosenName;
                    break;
                } else
                    writer.writeUTF("\nThis username has already been used or is not valid :(\nPlease choose another username.");
            }
            writer.writeUTF("Your role in the game : " + role.getRole());

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        isAlive = true;
    }

    public boolean exists(String name) {
        LinkedList<Player> players = game.getPlayers();
        synchronized (players) {
            for (Player player : players) {
                if (player.getName() == null)
                    continue;
                if (player.getName().equals(name)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void setName(String name) {
        synchronized (game) {
            this.name = name;
        }
    }

    public Role getRole() {
        return role;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public String getName() {
        return name;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataInputStream getReader() {
        return reader;
    }

    public void setReader(DataInputStream reader) {
        this.reader = reader;
    }

    public DataOutputStream getWriter() {
        return writer;
    }

    public void setWriter(DataOutputStream writer) {
        this.writer = writer;
    }
}
