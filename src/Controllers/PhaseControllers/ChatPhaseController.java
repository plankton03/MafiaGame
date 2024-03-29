package Controllers.PhaseControllers;

import Controllers.ClientHandlers.ChatPhaseHandler;
import Controllers.ClientHandlers.VotingPhaseHandler;
import Design.Color;
import Game.Game;
import Player.Player;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;

/**
 * The type Chat phase controller.
 *
 * @author : Fatemeh Abdi
 */
public class ChatPhaseController {

    private Game game;
    private LinkedList<Player> players;
    private LinkedList<Player> deadPlayers;
    private boolean chatIsOver = false;
    private LinkedList<ChatPhaseHandler> chatPhaseHandlers;
    private final long chatTime = 150 * 1000;

    /**
     * Instantiates a new Chat phase controller.
     *
     * @param game the game
     */
    public ChatPhaseController(Game game) {
        this.game = game;
        players = game.getPlayers();
        deadPlayers = game.getDeadPlayers();
        chatPhaseHandlers = new LinkedList<>();
    }

    /**
     * Gets game.
     *
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Start chat.
     */
    public void startChat() {

        sendMessageToAll(Color.CYAN + "\n....................................................................\n" + Color.RESET);

        sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "The chat phase of the day begins. You have 5 minutes to chat." +
                "\nIf you are ready to vote, please enter a '#' character.\n" + Color.RESET);
        for (Player player : players) {
            if (player.getRole().isSilent())
                continue;
            ChatPhaseHandler chatPhaseHandler = new ChatPhaseHandler(this, player);
            chatPhaseHandlers.add(chatPhaseHandler);
            chatPhaseHandler.start();
        }

        long start = System.currentTimeMillis();
        long end = start + chatTime;
        while (System.currentTimeMillis() < end) {
            if (allAreReadyToExitChat()) {
                sendMessageToAll(Color.CYAN_UNDERLINED + "\nAll users announced their readiness to start voting. " +
                        "You can no longer chat." + Color.RESET);
                for (Player player : players){
                    if (player.getRole().isSilent())
                        player.getRole().setSilent(false);
                }
                return;
            }
            try {
                Thread.sleep(2*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        forceFinishChat();

        sendMessageToAll(Color.CYAN_UNDERLINED + "\nThe time allotted for the chat has expired. " +
                "You can no longer chat with other users." + Color.RESET);
        sendExitMessage(Color.CYAN_UNDERLINED+"write any text you want , or click enter to exit the chat :)" + Color.RESET);

        for (Player player : players){
            if (player.getRole().isSilent())
                player.getRole().setSilent(false);
        }
    }

    /**
     * Send exit message.
     *
     * @param message the message
     */
    public void sendExitMessage(String message) {
        for (ChatPhaseHandler handler : chatPhaseHandlers) {
            if (handler.isAlive()) {
                try {
                    handler.getThePlayer().getWriter().writeUTF(message);
                } catch (ConcurrentModificationException c) {
                } catch (IOException e) {
                    players.remove(handler.getThePlayer());
                    sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + handler.getThePlayer().getName()
                            + " is out of the game." + Color.RESET);
                }
            }
        }
    }

    /**
     * Send message to all.
     *
     * @param message the message
     */
    public void sendMessageToAll(String message) {
        for (Player player : players) {
            try {
                player.getWriter().writeUTF(message);
            } catch (ConcurrentModificationException c) {
            } catch (IOException e) {
                players.remove(player);
                sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + player.getName()
                        + " is out of the game." + Color.RESET);
            }
        }
        for (Player player : deadPlayers) {
            try {
                player.getWriter().writeUTF(message);
            } catch (ConcurrentModificationException c) {
            } catch (IOException e) {
                players.remove(player);
                sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + player.getName()
                        + " is out of the game." + Color.RESET);
            }
        }
    }

    /**
     * Send message to all.
     *
     * @param msg       the msg
     * @param thePlayer the the player
     */
    public void sendMessageToAll(String msg, ChatPhaseHandler thePlayer){
        String message;
        for (ChatPhaseHandler player : chatPhaseHandlers) {
            if (player.equals(thePlayer)) {
                try {
                    player.getThePlayer().getWriter().writeUTF(Color.WHITE_UNDERLINED + "You"
                            + Color.RESET +"   "+ Color.WHITE_BOLD_BRIGHT + msg + Color.RESET);
                } catch (ConcurrentModificationException c) {
                } catch (IOException e) {
                    players.remove(player.getThePlayer());
                    chatPhaseHandlers.remove(player);
                    sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + player.getThePlayer().getName()
                            + " is out of the game." + Color.RESET);
                }
                continue;
            }
            message = Color.WHITE_UNDERLINED + thePlayer.getThePlayer().getName()+ Color.RESET+"   ";
            message += Color.WHITE_BOLD_BRIGHT + msg + Color.RESET;
            try {
                player.getThePlayer().getWriter().writeUTF(message);
            } catch (ConcurrentModificationException c) {
            } catch (IOException e) {
                players.remove(player.getThePlayer());
                chatPhaseHandlers.remove(player);
                sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + player.getThePlayer().getName()
                        + " is out of the game." + Color.RESET);
            }
        }
        for (Player player : deadPlayers) {
            message = Color.WHITE_UNDERLINED + thePlayer.getThePlayer().getName()+ Color.RESET+"   ";
            message += Color.WHITE_BOLD_BRIGHT + msg + Color.RESET;
            try {
                player.getWriter().writeUTF(message);
            } catch (ConcurrentModificationException c) {
            } catch (IOException e) {
                players.remove(player);
                sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + player.getName()
                        + " is out of the game." + Color.RESET);
            }
        }
    }

    /**
     * Gets chat phase handlers.
     *
     * @return the chat phase handlers
     */
    public LinkedList<ChatPhaseHandler> getChatPhaseHandlers() {
        return chatPhaseHandlers;
    }

    /**
     * Force finish chat.
     */
    public void forceFinishChat() {
        for (ChatPhaseHandler chatPhaseHandler : chatPhaseHandlers) {
            chatPhaseHandler.setExitChat(true);
        }
    }

    /**
     * All are ready to exit chat boolean.
     *
     * @return the boolean
     */
    public boolean allAreReadyToExitChat() {
        for (ChatPhaseHandler chatPhaseHandler : chatPhaseHandlers) {
            if (!chatPhaseHandler.isReadyToEndChat())
                return false;
        }
        return true;
    }

}
