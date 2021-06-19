package Roles;

import Player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class DrCity extends MainRoles {

    private boolean saveMySelf = false;

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
            int answer = getAnswer(thePlayer, 1, players.size());
            if (answer == players.indexOf(thePlayer) + 1) {
                if (saveMySelf) {
                    thePlayer.getWriter().writeUTF("You have used your opportunity to save yourself once." +
                            "Please select another person : ");
                    act(thePlayer, players);
                } else {
                    saveMySelf = true;
                    return answer;
                }
            } else return answer;
        } catch (IOException e) {
            return 0;
        }
        return 0;
    }

    public String prepareMessage(LinkedList<Player> players, Player thePlayer) {
        int index = 1;
        String message = "Please select one of the members below to save:\n";
        for (Player player : players) {
            if (player.equals(thePlayer)) {
                message += index + ". Yourself\n";
                index++;
                continue;
            }
            message += index + ". " + player.getName() +"\n";
            index++;
        }
        return message;
    }


}
