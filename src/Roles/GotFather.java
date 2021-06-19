package Roles;

import Design.Color;
import Player.Player;

import javax.swing.text.PlainDocument;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class GotFather extends MainRoles {

    public GotFather() {
        super("GotFather");
    }

    @Override
    public String nightQuestion() {
        return null;
    }

    @Override
    public boolean isMafia() {
        return true;
    }

    public int act(LinkedList<Player> players, Player thePlayer) {
        try {
            thePlayer.getWriter().writeUTF(prepareMessage(players, thePlayer));
            return getAnswer(thePlayer, 1, players.size() - 1);
        } catch (IOException e) {
            return 0;
        }

    }

    public String prepareMessage(LinkedList<Player> players, Player thePlayer) {
        String message ="\n\n"+ Color.PURPLE_UNDERLINED + "Please select the victim of the night\n"+Color.RESET;
        int index = 1;

        for (Player player : players) {
            if (player.equals(thePlayer))
                continue;
            message += Color.PURPLE+index + ". " + player.getName() + "\n"+Color.RESET;
            index++;
        }
        return message;
    }
}
