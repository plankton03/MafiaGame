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
    private BufferedReader reader;
    private PrintWriter writer;

    //TODO : in run:
    // 1. get name
    // 2. check name
    // 3. send part for player

    public Player(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
        isAlive = true;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public void run() {
        writer.println("Welcome to the Mafia game *_*\nPlease enter a username for yourself ...");

        while (true) {
            try {
                String chosenName = reader.readLine();
                if (!exist(chosenName)) {
                    setName(chosenName);
                    break;
                } else
                    writer.println("This username has already been used. Please choose another username.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        writer.println("Your role in the game : " + role.getRole());
    }

    public boolean exist(String name) {
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

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }
}
