package Controllers.ClientHandlers;

import Controllers.PhaseControllers.ChatPhaseController;
import Controllers.PhaseControllers.NightPhaseController;
import Design.Color;
import Player.Player;
import Roles.GotFather;

import java.io.IOException;

public class MafiaChatHandler extends Thread {
    private NightPhaseController controller;
    private Player thePlayer;
    private boolean exitChat = false;


    public MafiaChatHandler(NightPhaseController controller, Player thePlayer) {
        this.controller = controller;
        this.thePlayer = thePlayer;
    }

    @Override
    public void run() {
        try {
            thePlayer.getWriter().writeUTF(Color.CYAN_BOLD_BRIGHT+"\nMafia members chat starts. This chat lasts until " +
                    "the head of the Mafia announces his readiness by sending a '#' character or takes 5 minutes."+Color.RESET);
        } catch (IOException e) {
            controller.getGame().getPlayers().remove(thePlayer);
            controller.sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + thePlayer.getName()
                    + " is out of the game." + Color.RESET);
        }
        startListening();
    }

    public void startListening() {

        String rcv;
        while (true) {
            try {
                if (exitChat) {
                    break;
                }
                rcv = thePlayer.getReader().readUTF();
                if (rcv.isBlank())
                    continue;
                else if (rcv.equals("#") && thePlayer.getRole() instanceof GotFather) {
                    controller.sendMessageToAll("I made my decision", this);
                    controller.setChatIsOver(true);
                    controller.getChatHandlers().remove(this);
                    controller.sendMessageToAll("write any text you want , or click enter to exit the game :)", this);
                    break;
                }
                controller.sendMessageToAll(rcv, this);
                if (exitChat) {
                    thePlayer.getWriter().writeUTF(Color.CYAN_BOLD_BRIGHT+"The consultation of Mafia members is over."+Color.RESET);
                    break;
                }
            } catch (IOException e) {
                controller.getGame().getPlayers().remove(thePlayer);
                controller.sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + thePlayer.getName()
                        + " is out of the game." + Color.RESET);
            }
        }
    }


    public void setExitChat(boolean exitChat) {
        this.exitChat = exitChat;
    }

    public Player getThePlayer() {
        return thePlayer;
    }


}
