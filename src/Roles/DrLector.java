package Roles;

import Design.Color;
import Player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The type Dr lector.
 *
 * @author : Fatemeh Abdi
 */
public class DrLector extends MainRoles {

    private boolean saveMySelf = false;

    /**
     * Instantiates a new Dr lector.
     */
    public DrLector() {
        super("Doctor Lector");
    }


    /**
     * Act int.
     *
     * @param thePlayer the the player
     * @param mafia     the mafia
     * @param players   the players
     * @return the int
     */
    public int act(Player thePlayer, ArrayList<Player> mafia, LinkedList<Player> players) {

        try {
            thePlayer.getWriter().writeUTF(prepareMessage(mafia, thePlayer));
            int answer = getAnswer(thePlayer, 1, mafia.size());

            if (answer == mafia.indexOf(thePlayer) + 1) {
                if (saveMySelf) {
                    thePlayer.getWriter().writeUTF(Color.PURPLE_UNDERLINED + "You have used your opportunity to save yourself once. " +
                            "Please select another person : " + Color.RESET);
                    act(thePlayer, mafia, players);
                } else {
                    saveMySelf = true;
                    return players.indexOf(mafia.get(answer - 1)) + 1;
                }
            } else return players.indexOf(mafia.get(answer - 1)) + 1;
            return getAnswer(thePlayer, 1, mafia.size());
        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * Prepare message string.
     *
     * @param mafia     the mafia
     * @param thePlayer the the player
     * @return the string
     */
    public String prepareMessage(ArrayList<Player> mafia, Player thePlayer) {
        int index = 1;
        String message = "\n\n" + Color.PURPLE_UNDERLINED + "Please select one of the members below to save:\n" + Color.RESET;
        for (Player player : mafia) {
            if (player.getRole().equals(this)) {
                message += Color.PURPLE + index + ". Yourself\n" + Color.RESET;
                index++;
                continue;
            }
            message += Color.PURPLE + index + ". " + player.getName() + "  ->  " + player.getRole().getRole() + "\n" + Color.RESET;
            index++;
        }
        return message;
    }

    @Override
    public boolean isMafia() {
        return true;
    }
}
