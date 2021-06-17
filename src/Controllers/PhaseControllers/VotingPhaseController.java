package Controllers.PhaseControllers;

import Controllers.ClientHandlers.ChatPhaseHandler;
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

    private Game game;
    private LinkedList<Player> players;
    private LinkedList<Player> deadPlayers ;
    private long voteTime;
    private LinkedList<VotingPhaseHandler> votingPhaseHandlers;
    private ConcurrentHashMap<Player,Integer> voteResults = new ConcurrentHashMap<Player, Integer>();
    private ConcurrentHashMap<Player,Player> playersVote = new ConcurrentHashMap<Player, Player>();

    private boolean voteIsOver = false;

    public VotingPhaseController(Game game) {
        this.game = game;
        players = game.getPlayers();
        for (Player player : players){
            voteResults.put(player,0);
        }
        deadPlayers = game.getDeadPlayers();
        votingPhaseHandlers = new LinkedList<>();
    }


    public void startVotingPhase(){

        try {
            sendMessageToAll("\n......................................" +
                    "\nVoting begins :)\nYou only have "+ voteTime +" seconds to vote...\n\n");

            for (Player player : players) {
                VotingPhaseHandler votingPhaseHandler = new VotingPhaseHandler(this, player);
                votingPhaseHandlers.add(votingPhaseHandler);
                votingPhaseHandler.start();
            }

            reportVote();

            announcementOfVotingResults();

            reportResult();

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

    public void announcementOfVotingResults(){
        int max = 1;
        ArrayList<Player> suspectedPlayer = new ArrayList<Player>();
        for (Map.Entry<Player, Integer> player : voteResults.entrySet() ){
            if (player.getValue() >= max){
                max = player.getValue();
                suspectedPlayer.add(player.getKey());
            }
        }

        for (Player player : suspectedPlayer){
            try {
                player.getWriter().writeUTF("You are dead :(");
                players.remove(player);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


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
