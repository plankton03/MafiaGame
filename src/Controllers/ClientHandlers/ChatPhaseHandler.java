package Controllers.ClientHandlers;

import Controllers.PhaseControllers.ChatPhaseController;
import Design.Color;
import Player.Player;

import java.io.IOException;

public class ChatPhaseHandler extends Thread {


    private ChatPhaseController controller;
    private Player thePlayer;
    private boolean isReadyToEndChat = false;
    private boolean exitChat = false;

    public ChatPhaseHandler(ChatPhaseController controller, Player thePlayer) {
        this.controller = controller;
        this.thePlayer = thePlayer;
    }

    @Override
    public void run() {
        startListening();
    }

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
                }
                controller.sendMessageToAll(rcv, this);
                if (exitChat)
                {
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

    public boolean isReadyToEndChat() {
        return isReadyToEndChat;
    }
}
