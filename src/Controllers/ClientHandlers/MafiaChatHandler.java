package Controllers.ClientHandlers;

import Controllers.PhaseControllers.ChatPhaseController;
import Controllers.PhaseControllers.NightPhaseController;
import Player.Player;

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
        startListening();
    }

    public void startListening() {
        String rcv;
        while (true) {
            try {
                rcv = thePlayer.getReader().readUTF();
                if (rcv.isBlank())
                    continue;
                else if (rcv.equals("$") && thePlayer.getRole().getRole().equals("GotFather")) {
                    controller.exitChat(controller.getChatHandlers());
                    controller.setChatIsOver(true);
                    break;
                }
                controller.sendMessageToAll(rcv, this);
                if (exitChat)
                    break;
            } catch (IOException e) {
                e.printStackTrace();
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
