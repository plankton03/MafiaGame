package Controllers.PhaseControllers;

import Controllers.ClientHandlers.ChatPhaseHandler;
import Game.Game;
import Player.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class ChatPhaseController {

    private Game game;
    private LinkedList<Player> players;
    private LinkedList<Player> deadPlayers;
    private boolean chatIsOver = false;
    private LinkedList<ChatPhaseHandler> chatPhaseHandlers;
    private final long chatTime = 20 * 1000;

    public ChatPhaseController(Game game) {
        this.game = game;
        players = game.getPlayers();
        deadPlayers = game.getDeadPlayers();
        chatPhaseHandlers = new LinkedList<>();
    }

    public void startChat() {
        try {
            sendMessageToAll("\n......................................" +
                    "\nThe day begins :)\n\nChat starts ...\n");
            for (Player player : players) {
                ChatPhaseHandler chatPhaseHandler = new ChatPhaseHandler(this, player);
                chatPhaseHandlers.add(chatPhaseHandler);
                chatPhaseHandler.start();
            }

            long start = System.currentTimeMillis();
            long end = start + chatTime;
            while (System.currentTimeMillis() < end) {
                if (allAreReadyToExitChat()) {
                    sendMessageToAll("\n\nAll users announced their readiness to start voting. You can no longer chat.");
                    return;
                }
            }
            forceFinishChat();
            sendMessageToAll("\n\nThe time allotted for the chat has expired. You can no longer chat with other users.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToAll(String message) throws IOException {
        for (Player player : players) {
            player.getWriter().writeUTF(message);
        }
        for (Player player : deadPlayers) {
            player.getWriter().writeUTF(message);
        }
    }

    public void sendMessageToAll(String msg, ChatPhaseHandler thePlayer) throws IOException {
        String message = new String();
        for (ChatPhaseHandler player : chatPhaseHandlers) {
            if (player.equals(thePlayer)) {
                player.getThePlayer().getWriter().writeUTF("You : " + msg);
                continue;
            }
            message = thePlayer.getThePlayer().getName() + " : " + msg;
            player.getThePlayer().getWriter().writeUTF(message);
        }
        for (Player player : deadPlayers) {
            message = thePlayer.getThePlayer().getName() + " : " + msg;
            player.getWriter().writeUTF(message);
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
