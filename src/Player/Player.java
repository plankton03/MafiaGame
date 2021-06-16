package Player;

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
    private BufferedReader reader;
    private PrintWriter writer;

    //TODO : in run:
    // 1. get name
    // 2. check name
    // 3. send part for player

    public Player(Socket socket , Game game) {
        this.socket = socket;
        this.game = game;
        isAlive = true;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
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
                if (!exist(chosenName)){
                    setName(chosenName);
                    break;
                }
                else
                    writer.println("This username has already been used. Please choose another username.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        writer.println("Your role in the game : "+ role.getClass());
    }

    public boolean exist(String name){
        synchronized (game){
            for (Player player : game.getPlayers()){
                if (player.name.equals(name)){
                    return true;
                }
            }
            return false;
        }
    }



    public void setName(String name) {
        synchronized (game){
            this.name = name;
        }
    }



}
