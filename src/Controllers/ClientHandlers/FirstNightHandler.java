package Controllers.ClientHandlers;

import Controllers.PhaseControllers.FirstNightController;
import Design.Color;
import Player.Player;

import java.io.IOException;
import java.util.LinkedList;

public class FirstNightHandler extends Thread {

    private LinkedList<Player> players;
    private Player thePlayer;
    private FirstNightController controller;

    public FirstNightHandler(LinkedList<Player> players, Player thePlayer, FirstNightController controller) {
        this.players = players;
        this.thePlayer = thePlayer;
        this.controller = controller;
    }

    @Override
    public void run() {

        String message = prepareMessage();

        try {
            thePlayer.getWriter().writeUTF(message);
        } catch (IOException e) {
            players.remove(thePlayer);
            controller.sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + thePlayer.getName()
                    + " is out of the game." + Color.RESET);
        }

    }

    private String prepareMessage() {
        if (thePlayer.getRole().isMafia())
            return prepareMafiaMessage();
        else if (thePlayer.getRole().getRole().equals("Mayor"))
            return prepareMayorMessage();
        else
            return Color.CYAN_UNDERLINED+"The game introduction night is underway :)"+Color.RESET;
    }

    private String prepareMafiaMessage() {
        String message = Color.CYAN_UNDERLINED+"The list of Mafia members is as follows :\n"+Color.RESET;

        for (Player player : players) {
            if (player.equals(thePlayer))
                continue;
            if (player.getRole().isMafia()) {
                message += Color.CYAN_BOLD_BRIGHT+"* " + player.getName() + " : " + player.getRole().getRole() + " \n"+Color.RESET;
            }
        }

        if (message.equals( Color.CYAN_UNDERLINED+"The list of Mafia members is as follows :\n"+Color.RESET))
            message = Color.CYAN_UNDERLINED+"There is no any mafia in the city :("+Color.CYAN_UNDERLINED;
        return message;
    }

    private String prepareMayorMessage() {
        for (Player player : players) {
            if (player.getRole().getRole().equals("City Doctor"))
                return Color.CYAN_BOLD_BRIGHT+"* " + player.getName() + " : City Doctor \n"+Color.RESET;
        }
        return Color.CYAN_UNDERLINED+"There is no any doctor in the city "+Color.RESET;
    }
}
