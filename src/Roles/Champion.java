package Roles;

import Design.Color;
import Player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The type Champion.
 *
 * @author : Fatemeh Abdi
 */
public class Champion extends MainRoles {

    /**
     * Instantiates a new Champion.
     */
    public Champion() {
        super("Champion");
    }

    @Override
    public boolean isMafia() {
        return false;
    }

    /**
     * Act int.
     *
     * @param players   the players
     * @param thePlayer the the player
     * @return the int
     */
    public int act(LinkedList<Player> players, Player thePlayer) {

        try {
            thePlayer.getWriter().writeUTF(prepareMessage(players, thePlayer));
            return getAnswer(thePlayer, 0, players.size() - 1);
        } catch (IOException e) {
            return 0;
        }
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

        String message = "\n\n" + Color.PURPLE_UNDERLINED + "Please select one of the following options for shooting" +
                Color.RESET + Color.PURPLE + "\n0. Nobody\n" + Color.RESET;
        for (Player player : players) {
            if (player.equals(thePlayer))
                continue;
            message += Color.PURPLE + index + ". " + player.getName() + "\n" + Color.RESET;
            index++;
        }

        return message;
    }
}
