package Controllers.ClientHandlers;

import Player.Player;

import java.io.IOException;
import java.util.LinkedList;

public class FirstNightHandler extends Thread {

    private LinkedList<Player> players;
    private Player thePlayer;

    public FirstNightHandler(LinkedList<Player> players, Player thePlayer) {
        this.players = players;
        this.thePlayer = thePlayer;
    }

    @Override
    public void run() {

        String message = prepareMessage();

        try {
            thePlayer.getWriter().writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String prepareMessage() {
        if (thePlayer.getRole().isMafia())
            return prepareMafiaMessage();
        else if (thePlayer.getRole().getRole().equals("Mayor"))
            return prepareMayorMessage();
        else
            return "The game introduction night is underway :)";
    }

    private String prepareMafiaMessage() {
        String message = "The list of Mafia members is as follows :\n";

        for (Player player : players) {
            if (player.equals(thePlayer))
                continue;
            if (player.getRole().isMafia()) {
                message += "# " + player.getName() + " : " + player.getRole().getRole() + " \n";
            }
        }

        if (message.equals("The list of Mafia members is as follows :\n"))
            message = "There is no any mafia in the city :(";
        return message;
    }

    private String prepareMayorMessage() {
        for (Player player : players) {
            if (player.getRole().getRole().equals("City Doctor"))
                return "# " + player.getName() + " : City Doctor \n";
        }
        return "There is no any doctor in the city ";
    }
}
