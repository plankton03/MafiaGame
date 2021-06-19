package Roles;

import Player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class DrLector extends MainRoles {

    private boolean saveMySelf = false;

    public DrLector() {
        super("Doctor Lector");
    }

    @Override
    public String nightQuestion() {
        return null;
    }


    public int act(Player thePlayer, LinkedList<Player> players, int numOfMafia) {

        try {
            thePlayer.getWriter().writeUTF(prepareMessage(players, thePlayer));
            int answer = getAnswer(thePlayer, 1, players.size());

            if (answer == players.indexOf(thePlayer) + 1) {
                if (saveMySelf) {
                    thePlayer.getWriter().writeUTF("You have used your opportunity to save yourself once. " +
                            "Please select another person : ");
                    act(thePlayer, players,numOfMafia);
                } else {
                    saveMySelf = true;
                    return answer;
                }
            } else return answer;
            return getAnswer(thePlayer, 1, numOfMafia);
        } catch (IOException e) {
            return 0;
        }
    }

    public String prepareMessage(LinkedList<Player> players, Player thePlayer) {
        int index = 1;
        String message = "Please select one of the members below to save:\n";
        for (Player player : players) {
            if (player.getRole().isMafia()) {
                if (player.getRole().equals(this)) {
                    message += index + ". Yourself\n";
                    index++;
                    continue;
                }
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
