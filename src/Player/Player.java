package Player;

import Design.Color;
import Game.Game;
import Roles.Role;

import java.io.*;
import java.net.Socket;

public class Player implements Runnable {
    private Game game;
    private boolean isAlive;
    private String name;
    private Role role;
    private Socket socket;
    private DataInputStream reader;
    private DataOutputStream writer;
    private Color color = new Color();
    private int inactive = 0;


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

    public int getInactive() {
        return inactive;
    }

    public void setInactive(int inactive) {
        this.inactive = inactive;
    }

    @Override
    public void run() {
        try {
            writer.writeUTF(color.CYAN_BOLD_BRIGHT + "\n\t\t\t\tWelcome to the Mafia game *_*" + color.RESET);
            writer.writeUTF(color.CYAN_UNDERLINED + "\n\nPlease enter a username for yourself :" + color.RESET);
            while (true) {
                String chosenName = reader.readUTF();
                if (!game.getInitializer().getNames().contains(chosenName) && !chosenName.isBlank()) {
                    game.getInitializer().addNewName(chosenName);
                    name = chosenName;
                    break;
                } else
                    writer.writeUTF(color.CYAN_UNDERLINED + "This username has already been used or is not valid :(" +
                            "\nPlease choose another username." + color.RESET);
            }
            writer.writeUTF(color.CYAN_BOLD_BRIGHT + "\n\t\t\t\tYour role in the game : " + role.getRole() + color.RESET);
            writer.writeUTF(color.CYAN_UNDERLINED+"\nIf you are ready to start the game, enter a" +
                    " text of your choice or press Enter.\nKeep in mind that you can exit at any time by entering the exit word."+color.RESET);
            reader.readUTF();
        } catch (IOException ioException) {
            System.out.println("Player disconnected ");
        }

        isAlive = true;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public String getName() {
        return name;
    }

    public DataOutputStream getWriter() {
        return writer;
    }

    public void setName(String name) {
        synchronized (game) {
            this.name = name;
        }
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
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

    public void setWriter(DataOutputStream writer) {
        this.writer = writer;
    }
}
