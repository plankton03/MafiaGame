package Controllers.PhaseControllers;

import Controllers.ClientHandlers.FirstNightHandler;
import Design.Color;
import Game.Game;
import Player.Player;

import java.io.IOException;
import java.util.LinkedList;

public class FirstNightController {

    private Game game;
    private LinkedList<Player> players;
    private LinkedList<FirstNightHandler> firstNightHandlers;

    public FirstNightController(Game game) {
        this.game = game;
        this.players = game.getPlayers();
        firstNightHandlers = new LinkedList<>();
    }

    public void startFirstNight() {

        sendMessageToAll(Color.CYAN + "\n....................................................................\n" + Color.RESET);

        for (Player player : players) {
            firstNightHandlers.add(new FirstNightHandler(players, player, this));
            firstNightHandlers.get(firstNightHandlers.size() - 1).start();
        }

        waitForExit();
    }

    public void sendMessageToAll(String message) {
        for (Player player : players) {
            try {
                player.getWriter().writeUTF(message);
            } catch (IOException e) {
                players.remove(player);
                sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + player.getName() + " is out of the game." + Color.RESET);
            }
        }
    }

    private void waitForExit() {
        while (true) {
            if (isDone())
                break;
        }
    }

    private boolean isDone() {
        for (FirstNightHandler fnh : firstNightHandlers) {
            if (fnh.isAlive())
                return false;
        }
        return true;
    }

}
