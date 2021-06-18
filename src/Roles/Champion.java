package Roles;

import Player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Champion extends MainRoles {

    public Champion() {
        super("Champion");
    }

    @Override
    public String nightQuestion() {
        return null;
    }

    @Override
    public boolean isMafia() {
        return false;
    }

    public int act(LinkedList<Player> players, Player thePlayer) {

        try {
            thePlayer.getWriter().writeUTF(prepareMessage(players, thePlayer));
            return getAnswer(thePlayer, 0, players.size() - 1);
        } catch (IOException e) {
            return 0;
        }
    }

    public String prepareMessage(LinkedList<Player> players, Player thePlayer) {
        int index = 1;

        String message = "Please select one of the following options for shooting\n0. Nobody\n";
        for (Player player : players) {
            if (player.equals(thePlayer))
                continue;
            message += index + ". " + player.getName() + "\n";
            index++;
        }

        return message;
    }
}
