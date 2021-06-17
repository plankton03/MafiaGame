package Roles;

import Player.Player;

import javax.swing.text.PlainDocument;
import java.io.IOException;

public class Pitman extends MainRoles {

    public Pitman() {
        super("Pitman");
    }

    @Override
    public String nightQuestion() {
        return null;
    }

    @Override
    public boolean isMafia() {
        return false;
    }

    public int act(Player thePlayer) {
        try {
            thePlayer.getWriter().writeUTF("Do you want to know the role of the dead?\n0. No\n1. yes" );
            return getAnswer(thePlayer,0,1);
        } catch (IOException e) {
            return 0;
        }
    }
}
