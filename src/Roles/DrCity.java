package Roles;

import Design.Color;
import Player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The type Dr city.
 *
 * @author : Fatemeh Abdi
 */
public class DrCity extends MainRoles {

    private boolean saveMySelf = false;

    /**
     * Instantiates a new Dr city.
     */
    public DrCity() {
        super("City Doctor");
    }


    @Override
    public boolean isMafia() {
        return false;
    }

    /**
     * Act int.
     *
     * @param thePlayer the the player
     * @param players   the players
     * @return the int
     */
    public int act(Player thePlayer, LinkedList<Player> players) {

        try {
            thePlayer.getWriter().writeUTF(prepareMessage(players, thePlayer));
            int answer = getAnswer(thePlayer, 1, players.size());
            if (answer == players.indexOf(thePlayer) + 1) {
                if (saveMySelf) {
                    thePlayer.getWriter().writeUTF(Color.PURPLE_UNDERLINED + "You have used your opportunity to save yourself once." +
                            "Please select another person : " + Color.RESET);
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

    /**
     * Prepare message string.
     *
     * @param players   the players
     * @param thePlayer the the player
     * @return the string
     */
    public String prepareMessage(LinkedList<Player> players, Player thePlayer) {
        int index = 1;
        String message = Color.PURPLE_UNDERLINED + "\n\nPlease select one of the members below to save:\n" + Color.RESET;
        for (Player player : players) {
            if (player.equals(thePlayer)) {
                message += Color.PURPLE + index + ". Yourself\n" + Color.PURPLE;
                index++;
                continue;
            }
            message += Color.PURPLE + index + ". " + player.getName() + "\n" + Color.RESET;
            index++;
        }
        return message;
    }


}
