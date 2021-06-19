package Controllers.ClientHandlers;

import Controllers.PhaseControllers.ChatPhaseController;
import Controllers.PhaseControllers.NightPhaseController;
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
        startListening();
    }

    public void startListening() {

        String rcv ;
        while (true) {
            try {
                if (exitChat)
                {
                    System.out.println("in handler ");
                    break;
                }
                rcv = thePlayer.getReader().readUTF();
                if (rcv.isBlank())
                    continue;
                else if (rcv.equals("#") && thePlayer.getRole() instanceof GotFather) {
                    controller.sendMessageToAll("I made my decision",this);
                    controller.setChatIsOver(true);
                    controller.getChatHandlers().remove(this);
                    controller.sendMessageToAll("write any text you want , or click enter to exit the game :)",this);
                    break;
                }
                controller.sendMessageToAll(rcv, this);
                if (exitChat)
                {
                    System.out.println("in handler ");
                    thePlayer.getWriter().writeUTF("The consultation of Mafia members is over.");
                    break;
                }
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
