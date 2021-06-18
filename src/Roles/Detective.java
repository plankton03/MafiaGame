package Roles;

import Player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Detective extends MainRoles{

    public Detective() {
        super("Detective");
    }

    @Override
    public String nightQuestion() {
        return null;
    }

    @Override
    public boolean isMafia() {
        return false;
    }

    public int act(LinkedList<Player> players, Player thePlayer){

        try {
            thePlayer.getWriter().writeUTF(prepareMessage(players,thePlayer));
            return getAnswer(thePlayer,1,players.size()-1);
        } catch (IOException e) {
            return 0;
        }
    }

    public String prepareMessage(LinkedList<Player> players , Player thePlayer){
        String message = "Please select one of the people to query.\n";

        int index =1;
        for (Player player : players){
            if (player.equals(thePlayer))
                continue;
            message+= index + ". "+player.getName()+"\n";
            index++;
        }
        return message;
    }
}
