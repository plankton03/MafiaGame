package Roles;

import Design.Color;
import Player.Player;

import java.io.IOException;

/**
 * The type Mayor.
 *
 * @author : Fatemeh Abdi
 */
public class Mayor extends MainRoles {

    /**
     * Instantiates a new Mayor.
     */
    public Mayor() {
        super("Mayor");
    }

    @Override
    public boolean isMafia() {
        return false;
    }

    /**
     * Act int.
     *
     * @param thePlayer the the player
     * @return the int
     */
    public int act(Player thePlayer) {
        try {
            thePlayer.getWriter().writeUTF("\n\n" + Color.PURPLE_UNDERLINED + "\nDo you want to cancel the vote?" +
                    Color.RESET + Color.PURPLE + "\n1. Yes\n2. No" + Color.RESET);
            return getAnswer(thePlayer, 1, 2);
        } catch (IOException e) {
            return 2;
        }
    }
}
