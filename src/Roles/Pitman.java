package Roles;

import Design.Color;
import Player.Player;

import javax.swing.text.PlainDocument;
import java.io.IOException;

public class Pitman extends MainRoles {

    int numOfActivity = 0 ;

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

        if (numOfActivity >= 2)
            return 0;
        try {
            thePlayer.getWriter().writeUTF("\n\n"+Color.PURPLE_UNDERLINED +"Do you want to know the role of the dead?" +
                    Color.RESET+Color.PURPLE+
                    "\n0. No\n1. Yes" +Color.RESET);
            int answer =getAnswer(thePlayer,0,1);
            if (answer == 1)
                numOfActivity++;
            return answer;
        } catch (IOException e) {
            return 0;
        }
    }

    public int getNumOfActivity() {
        return numOfActivity;
    }
}
