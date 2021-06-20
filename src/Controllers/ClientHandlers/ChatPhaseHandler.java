package Controllers.ClientHandlers;

import Controllers.PhaseControllers.ChatPhaseController;
import Design.Color;
import Player.Player;

import java.io.IOException;

/**
 * The type Chat phase handler.
 *
 * @author : Fatemeh Abdi
 */
public class ChatPhaseHandler extends Thread {


    private ChatPhaseController controller;
    private Player thePlayer;
    private boolean isReadyToEndChat = false;
    private boolean exitChat = false;

    /**
     * Instantiates a new Chat phase handler.
     *
     * @param controller the controller
     * @param thePlayer  the the player
     */
    public ChatPhaseHandler(ChatPhaseController controller, Player thePlayer) {
        this.controller = controller;
        this.thePlayer = thePlayer;
    }

    @Override
    public void run() {
        startListening();
    }

    /**
     * Start listening.
     */
    public void startListening() {
        String rcv;
        while (true) {
            try {
                rcv = thePlayer.getReader().readUTF();
                if (rcv.isBlank())
                    continue;
                else if (rcv.equals("#")) {
                    isReadyToEndChat = true;
                    rcv = "I'm ready to vote";
                    controller.sendMessageToAll(rcv, this);
                    return;
                }
                controller.sendMessageToAll(rcv, this);
                if (exitChat) {
                    controller.getChatPhaseHandlers().remove(this);
                    break;
                }
            } catch (IOException e) {
                controller.getGame().getPlayers().remove(thePlayer);
                controller.getChatPhaseHandlers().remove(this);
                controller.sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + thePlayer.getName()
                        + " is out of the game." + Color.RESET);
                break;
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

    /**
     * Is ready to end chat boolean.
     *
     * @return the boolean
     */
    public boolean isReadyToEndChat() {
        return isReadyToEndChat;
    }
}
