package Roles;

import Player.Player;

import java.io.IOException;
import java.util.ArrayList;

public class DrLector extends MainRoles {

    public DrLector() {
        super("Doctor Lector");
    }

    @Override
    public String nightQuestion() {
        return null;
    }


    public int act(Player thePlayer, ArrayList<Player> players, int numOfMafia) {

        try {
            thePlayer.getWriter().writeUTF(prepareMessage(players, thePlayer));
            return getAnswer(thePlayer,1,numOfMafia);
        } catch (IOException e) {
            return 0;
        }
    }

    public String prepareMessage(ArrayList<Player> players, Player thePlayer) {
        int index = 1;
        String message = "Please select one of the members below to save:\n";
        for (Player player : players) {
            if (player.getRole().isMafia() && !player.getRole().equals(this)) {
                message += index + ". " + player.getName() + " ... " + player.getRole().getRole() + "\n";
                index++;
            }
        }
        return message;
    }

    @Override
    public boolean isMafia() {
        return true;
    }
}
