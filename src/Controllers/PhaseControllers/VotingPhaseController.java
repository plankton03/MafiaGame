package Controllers.PhaseControllers;

import Controllers.ClientHandlers.ChatPhaseHandler;
import Controllers.ClientHandlers.ExitHandler;
import Controllers.ClientHandlers.VotingPhaseHandler;
import Game.Game;
import Player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VotingPhaseController {

    //TODO : starting
    // time
    // analyze
    // removing and reporting
    // exception ha ... joda mdiriyat kon :)
    // Mayor

    private Game game;
    private LinkedList<Player> players;
    private LinkedList<Player> deadPlayers;
    private long voteTime;
    private LinkedList<VotingPhaseHandler> votingPhaseHandlers;
    private ConcurrentHashMap<Player, Integer> voteResults = new ConcurrentHashMap<Player, Integer>();
    private ConcurrentHashMap<Player, Player> playersVote = new ConcurrentHashMap<Player, Player>();
    ArrayList<Player> suspectedPlayer = new ArrayList<Player>();

    private boolean voteIsOver = false;

    public VotingPhaseController(Game game) {
        this.game = game;
        players = game.getPlayers();
        for (Player player : players) {
            voteResults.put(player, 0);
        }
        deadPlayers = game.getDeadPlayers();
        votingPhaseHandlers = new LinkedList<>();
    }


    public void startVotingPhase() {

        resetSuspended();

        sendMessageToAll("\n......................................" +
                "\nVoting begins :)\nYou only have " + voteTime + " seconds to vote...\n\n");

        for (Player player : players) {
            VotingPhaseHandler votingPhaseHandler = new VotingPhaseHandler(this, player);
            votingPhaseHandlers.add(votingPhaseHandler);
            votingPhaseHandler.start();
        }

        reportPlayersVot();
        reportVote();  //show results and exited players

        if (!askMayor()) {
            announcementOfVotingResults();
        }
        reportResult();

    }

    public void reportResult() {
        if (suspectedPlayer.size() == 0) {
            sendMessageToAll("None of the people had enough votes to leave the game.");
        } else {
            String message = new String();
            for (Player player : suspectedPlayer)
                message += player.getName();
            sendMessageToAll(message + " died :)");
        }
    }

    public boolean askMayor() {
        for (Player player : players) {
            if (player.getRole().getRole().equals("Mayor")) {
                try {
                    player.getWriter().writeUTF("Do you want to cancel the vote?\n1. Yes\n2. No");
                    int answer = player.getRole().act();
                    if (answer == 1)
                        return true;
                    else return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }

    public void resetSuspended() {
        suspectedPlayer.clear();
    }


    public void sendMessageToAll(String message) {
        for (Player player : players) {
            try {
                player.getWriter().writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Player player : deadPlayers) {
            try {
                player.getWriter().writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void announcementOfVotingResults() {
        int max = 1;
        for (Map.Entry<Player, Integer> player : voteResults.entrySet()) {
            if (player.getValue() >= max) {
                max = player.getValue();
                suspectedPlayer.add(player.getKey());
            }
        }

        for (Player player : suspectedPlayer) {
            if (players.contains(player)) {
                try {
                    player.getWriter().writeUTF("You are dead :(");
                    players.remove(player);
                    deadPlayers.add(player);
                    (new ExitHandler(player, game)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void reportPlayersVot() {
        sendMessageToAll("List of players and their votes.");
        for (Map.Entry<Player, Player> playerEntry : playersVote.entrySet()) {
            if (players.contains(playerEntry.getKey())) {
                sendMessageToAll(playerEntry.getKey().getName() + " has voted for " + playerEntry.getValue().getName());
            } else {
                sendMessageToAll(playerEntry.getKey().getName() + " wanted to get out of the game");
            }
        }

        sendMessageToAll("\n\n\t\t\t\t.........................................");
    }

    public void reportVote() {
        sendMessageToAll("The voting results are as follows : ");

        for (Map.Entry<Player, Integer> player : voteResults.entrySet()) {
            if (players.contains(player.getKey())) {
                sendMessageToAll("* " + player.getKey().getName() + " : " + player.getValue());
            } else {
                sendMessageToAll(player.getKey().getName() + " is out of the game ... ");
            }
        }
        sendMessageToAll("\n\n\t\t\t\t.........................................");

    }

    public boolean isValidAnswer(int answer) {
        if (answer == 1 || answer == 2)
            return true;
        return false;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public ConcurrentHashMap<Player, Integer> getVoteResults() {
        return voteResults;
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(LinkedList<Player> players) {
        this.players = players;
    }

    public LinkedList<Player> getDeadPlayers() {
        return deadPlayers;
    }

    public ConcurrentHashMap<Player, Player> getPlayersVote() {
        return playersVote;
    }

    public void setDeadPlayers(LinkedList<Player> deadPlayers) {
        this.deadPlayers = deadPlayers;
    }

    public long getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(long voteTime) {
        this.voteTime = voteTime;
    }

    public LinkedList<VotingPhaseHandler> getVotingPhaseHandlers() {
        return votingPhaseHandlers;
    }

    public void setVotingPhaseHandlers(LinkedList<VotingPhaseHandler> votingPhaseHandlers) {
        this.votingPhaseHandlers = votingPhaseHandlers;
    }

    public boolean isVoteIsOver() {
        return voteIsOver;
    }

    public void setVoteIsOver(boolean voteIsOver) {
        this.voteIsOver = voteIsOver;
    }
}
