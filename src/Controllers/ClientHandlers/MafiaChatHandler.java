package Controllers.ClientHandlers;

import Controllers.PhaseControllers.ChatPhaseController;
import Controllers.PhaseControllers.NightPhaseController;
import Design.Color;
import Player.Player;
import Roles.GotFather;

import java.io.IOException;

/**
 * The type Mafia chat handler.
 *
 * @author : Fatemeh Abdi
 */
public class MafiaChatHandler extends Thread {
    private NightPhaseController controller;
    private Player thePlayer;
    private boolean exitChat = false;


    /**
     * Instantiates a new Mafia chat handler.
     *
     * @param controller the controller
     * @param thePlayer  the the player
     */
    public MafiaChatHandler(NightPhaseController controller, Player thePlayer) {
        this.controller = controller;
        this.thePlayer = thePlayer;
    }

    @Override
    public void run() {
        try {
            thePlayer.getWriter().writeUTF(Color.CYAN_BOLD_BRIGHT + "\nMafia members chat starts. This chat lasts until " +
                    "the head of the Mafia announces his readiness by sending a '#' character or takes 5 minutes." + Color.RESET);
        } catch (IOException e) {
            controller.getGame().getPlayers().remove(thePlayer);
            controller.getChatHandlers().remove(this);
            controller.sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + thePlayer.getName()
                    + " is out of the game." + Color.RESET);
            return;
        }
        startListening();
    }

    /**
     * Start listening.
     */
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
                    controller.sendExitMessage("write any text you want , or click enter to exit the chat :)");
                    break;
                }
                controller.sendMessageToAll(rcv, this);
                if (exitChat) {
                    thePlayer.getWriter().writeUTF(Color.CYAN_BOLD_BRIGHT + "The consultation of Mafia members is over." + Color.RESET);
                    break;
                }
            } catch (IOException e) {
                controller.getGame().getPlayers().remove(thePlayer);
                controller.getChatHandlers().remove(this);
                controller.sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + thePlayer.getName()
                        + " is out of the game." + Color.RESET);
                return;
            }
        }
    }


    /**
     * Sets exit chat.
     *
     * @param exitChat the exit chat
     */
    public void setExitChat(boolean exitChat) {
        this.exitChat = exitChat;
    }

    /**
     * Gets the player.
     *
     * @return the the player
     */
    public Player getThePlayer() {
        return thePlayer;
    }


}
