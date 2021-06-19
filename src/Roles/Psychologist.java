package Roles;

import Design.Color;
import Player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Psychologist extends MainRoles{



    public Psychologist() {
        super("Psychologist");
    }

    @Override
    public String nightQuestion() {
        return null;
    }

    @Override
    public boolean isMafia() {
        return false;
    }

    public int act(LinkedList<Player> players , Player thePlayer){

        try {
            thePlayer.getWriter().writeUTF(prepareMessage(players,thePlayer));
            return getAnswer(thePlayer,0,players.size()-1);
        }catch (IOException e){
            return 0;
        }

    }

    public String prepareMessage(LinkedList<Player> players , Player thePlayer){
            String message = "\n\n"+Color.PURPLE_UNDERLINED +"Please select one to silence." +
                    Color.RESET+Color.PURPLE+"\n0. Nobody\n"+Color.RESET;

            int index =1;
            for (Player player : players){
                if (player.equals(thePlayer))
                    continue;
                message+= Color.PURPLE+index + ". "+player.getName()+"\n"+Color.RESET;
                index++;
            }
            return message;
    }
}
