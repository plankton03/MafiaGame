package Controllers.ClientHandlers;

import Player.Player;

import java.util.LinkedList;

public class FirstNightHandler extends Thread {

    private LinkedList<Player> players;
    private Player thePlayer;

    public FirstNightHandler(LinkedList<Player> players, Player thePlayer) {
        this.players = players;
        this.thePlayer = thePlayer;
    }

    @Override
    public void run() {

        String message = prepareMessage();

        if (message == null)
            return;

        thePlayer.getWriter().println(message);
    }

    private String prepareMessage(){
        if (thePlayer.getRole().isMafia())
            return prepareMafiaMessage();
        else if (thePlayer.getRole().getClass().equals("Mayor"))
            return prepareMayorMessage();
        else return null;
    }

    private String prepareMafiaMessage(){
        String message = "The list of Mafia members is as follows :\n";

        for (Player player : players){
            if (player.equals(thePlayer))
                continue;
            if (player.getRole().isMafia()){
                message += "# "+player.getName() + " : "+player.getRole().getClass()+" \n";
            }
        }
        return message;
    }

    private String prepareMayorMessage(){
        for (Player player : players){
            if (player.getRole().getClass().equals("DrCity"))
                return "# "+ player.getName()+" : City Doctor \n";
        }
        return null;
    }
}
