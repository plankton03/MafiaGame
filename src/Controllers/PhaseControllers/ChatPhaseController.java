package Controllers.PhaseControllers;

import Controllers.ClientHandlers.ChatPhaseHandler;
import Controllers.ClientHandlers.SenderOfTheMessage;
import Game.Game;
import Player.Player;

import java.io.IOException;
import java.util.LinkedList;

public class ChatPhaseController {

    private Game game;
    private LinkedList<Player> players;
    private LinkedList<Player> deadPlayers;
    private boolean chatIsOver = false;
    private LinkedList<ChatPhaseHandler> chatPhaseHandlers;
    private final long chatTime = 100 * 1000;

    public ChatPhaseController(Game game) {
        this.game = game;
        players = game.getPlayers();
        deadPlayers = game.getDeadPlayers();
        chatPhaseHandlers = new LinkedList<>();
    }

    public void startChat() {


        try {
            sendMessageToAll("The day begins :)\n\nChat starts ...\n");
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (Player player : players) {
            (new ChatPhaseHandler(this, player)).start();
        }

        long start = System.currentTimeMillis();
        long end = start + chatTime;
        while (System.currentTimeMillis() < end) {
            if (allAreReadyToExitChat())
                return;
        }
        forceFinishChat();
    }

    public void sendMessageToAll(String message) throws IOException {
        for (ChatPhaseHandler chatPhaseHandler : chatPhaseHandlers) {
            (new SenderOfTheMessage(chatPhaseHandler.getThePlayer(), message)).start();
        }
        for (Player player : deadPlayers) {
            (new SenderOfTheMessage(player, message)).start();
        }
    }

    public void sendMessageToAll(String msg, ChatPhaseHandler thePlayer) throws IOException {
        String message = new String();
        for (ChatPhaseHandler player : chatPhaseHandlers) {
            if (player.equals(thePlayer))
                message = "You : " + msg;
            else
                message = thePlayer.getThePlayer().getName() + " : " + msg;
            (new SenderOfTheMessage(thePlayer.getThePlayer(), message)).start();
        }
        for (Player player : deadPlayers) {
            (new SenderOfTheMessage(player, message)).start();
        }
    }

    public void forceFinishChat() {
        for (ChatPhaseHandler chatPhaseHandler : chatPhaseHandlers) {
            chatPhaseHandler.setExitChat(true);
        }
    }

    public boolean allAreReadyToExitChat() {
        for (ChatPhaseHandler chatPhaseHandler : chatPhaseHandlers) {
            if (!chatPhaseHandler.isReadyToEndChat())
                return false;
        }
        return true;
    }

}
