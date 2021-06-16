package Controllers.PhaseControllers;

import Controllers.ClientHandlers.FirstNightHandler;
import Game.Game;
import Player.Player;
import java.util.LinkedList;

public class FirstNightController {

    private Game game;
    private LinkedList<Player> players;

    public FirstNightController(Game game) {
        this.game = game;
        this.players = game.getPlayers();
    }

    public void startFirstNight(){
        for (Player player : players){
            FirstNightHandler firstNightHandler = new FirstNightHandler(players,player);
            firstNightHandler.start();
        }
    }

}
