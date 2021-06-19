package Roles;

import Design.Color;
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
                    thePlayer.getWriter().writeUTF(Color.PURPLE_UNDERLINED +"You have used your opportunity to save yourself once. " +
                            "Please select another person : "+Color.RESET);
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
        String message ="\n\n"+ Color.PURPLE_UNDERLINED+"Please select one of the members below to save:\n"+Color.RESET ;
        for (Player player : players) {
            if (player.getRole().isMafia()) {
                if (player.getRole().equals(this)) {
                    message +=Color.PURPLE+ index + ". Yourself\n"+Color.RESET;
                    index++;
                    continue;
                }
                message += Color.PURPLE+index + ". " + player.getName() + "  ->  " + player.getRole().getRole() + "\n"+Color.RESET;
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
