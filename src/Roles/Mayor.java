package Roles;

import Player.Player;

import java.io.IOException;

public class Mayor extends MainRoles{

    public Mayor() {
        super("Mayor");
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
            thePlayer.getWriter().writeUTF("Do you want to cancel the vote?\n1. Yes\n2. No");
            return getAnswer(thePlayer,1,2);
        } catch (IOException e) {
            return 2;
        }
    }
}
