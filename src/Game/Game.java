package Game;

import Player.Player;

import java.util.LinkedList;

public class Game {

    LinkedList<Player> players;

    public LinkedList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(LinkedList<Player> players) {
        this.players = players;
    }
}
