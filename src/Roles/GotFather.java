package Roles;

import Player.Player;

import javax.swing.text.PlainDocument;
import java.io.IOException;
import java.util.ArrayList;

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

    public int act(ArrayList<Player> players, Player thePlayer) {
        try {
            thePlayer.getWriter().writeUTF(prepareMessage(players, thePlayer));
            return getAnswer(thePlayer, 1, players.size() - 1);
        } catch (IOException e) {
            return 0;
        }

    }

    public String prepareMessage(ArrayList<Player> players, Player thePlayer) {
        String message = "Please select the victim of the night\n";
        int index = 1;
        for (Player player : players) {
            if (player.equals(thePlayer))
                continue;
            message += index + ". " + player.getName() + "\n";
        }
        return message;
    }
}
