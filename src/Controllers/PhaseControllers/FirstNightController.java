package Controllers.PhaseControllers;

import Controllers.ClientHandlers.FirstNightHandler;
import Game.Game;
import Player.Player;

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
        for (Player player : players) {
            firstNightHandlers.add(new FirstNightHandler(players, player));
            firstNightHandlers.get(firstNightHandlers.size() - 1).start();
        }

        waitForExit();
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
