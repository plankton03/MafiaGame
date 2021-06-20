package Roles;

import Design.Color;
import Player.Player;

import javax.swing.text.PlainDocument;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The type Got father.
 *
 * @author : Fatemeh Abdi
 */
public class GotFather extends MainRoles {

    /**
     * Instantiates a new Got father.
     */
    public GotFather() {
        super("GotFather");
    }

    @Override
    public boolean isMafia() {
        return true;
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
            return getAnswer(thePlayer, 1, players.size() - 1);
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
        String message = "\n\n" + Color.PURPLE_UNDERLINED + "Please select the victim of the night\n" + Color.RESET;
        int index = 1;

        for (Player player : players) {
            if (player.equals(thePlayer))
                continue;
            message += Color.PURPLE + index + ". " + player.getName() + "\n" + Color.RESET;
            index++;
        }
        return message;
    }
}
