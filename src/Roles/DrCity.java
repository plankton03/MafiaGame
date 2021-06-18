package Roles;

import Player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class DrCity extends MainRoles {

    public DrCity() {
        super("City Doctor");
    }

    @Override
    public String nightQuestion() {
        return null;
    }

    @Override
    public boolean isMafia() {
        return false;
    }

    public int act(Player thePlayer, LinkedList<Player> players) {

        try {
            thePlayer.getWriter().writeUTF(prepareMessage(players, thePlayer));
            return getAnswer(thePlayer, 1, players.size()-1);
        } catch (IOException e) {
            return 0;
        }
    }

    public String prepareMessage(LinkedList<Player> players, Player thePlayer) {
        int index = 1;
        String message = "Please select one of the members below to save:\n";
        for (Player player : players) {
            if (player.equals(thePlayer))
                continue;
            message += index + ". " + player.getName() + " ... " + player.getRole().getRole() + "\n";
            index++;
        }
        return message;
    }


}
