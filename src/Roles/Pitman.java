package Roles;

import Design.Color;
import Player.Player;

import javax.swing.text.PlainDocument;
import java.io.IOException;

/**
 * The type Pitman.
 *
 * @author : Fatemeh Abdi
 */
public class Pitman extends MainRoles {

    /**
     * The Num of activity.
     */
    int numOfActivity = 0;

    /**
     * Instantiates a new Pitman.
     */
    public Pitman() {
        super("Pitman");
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

        if (numOfActivity >= 2)
            return 0;
        try {
            thePlayer.getWriter().writeUTF("\n\n" + Color.PURPLE_UNDERLINED + "Do you want to know the role of the dead?" +
                    Color.RESET + Color.PURPLE +
                    "\n0. No\n1. Yes" + Color.RESET);
            int answer = getAnswer(thePlayer, 0, 1);
            if (answer == 1)
                numOfActivity++;
            return answer;
        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * Gets num of activity.
     *
     * @return the num of activity
     */
    public int getNumOfActivity() {
        return numOfActivity;
    }
}
