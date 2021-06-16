package Controllers.ClientHandlers;

import Player.Player;

import java.io.IOException;

public class SenderOfTheMessage extends Thread{

    private  Player thePlayer;
    private String message;

    public SenderOfTheMessage(Player thePlayer, String message) {
        this.thePlayer = thePlayer;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            thePlayer.getWriter().writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
