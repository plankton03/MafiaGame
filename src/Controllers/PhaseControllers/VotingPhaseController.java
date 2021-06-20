package Controllers.PhaseControllers;

import Controllers.ClientHandlers.ExitHandler;
import Controllers.ClientHandlers.VotingPhaseHandler;
import Design.Color;
import Game.Game;
import Player.Player;
import Roles.Mayor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VotingPhaseController {


    private Game game;
    private LinkedList<Player> players;
    private LinkedList<Player> deadPlayers;
    private LinkedList<VotingPhaseHandler> votingPhaseHandlers;
    private ConcurrentHashMap<Player, Integer> voteResults = new ConcurrentHashMap<Player, Integer>();
    private ConcurrentHashMap<Player, Player> playersVote = new ConcurrentHashMap<Player, Player>();
    ArrayList<Player> suspectedPlayer = new ArrayList<Player>();


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

        sendMessageToAll(Color.CYAN + "\n....................................................................\n" + Color.RESET);

        sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "The Voting phase of the day begins. You have 30 seconds to vote.\n" + Color.RESET);


        for (Player player : players) {
            VotingPhaseHandler votingPhaseHandler = new VotingPhaseHandler(this, player);
            votingPhaseHandlers.add(votingPhaseHandler);
            votingPhaseHandler.start();
        }

        try {
            Thread.sleep(30 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        forceFinishVot();
        sendExitMessage();


        while (true) {
            if (voteIsDone()) {
                break;
            }
        }

        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        reportPlayersVot();
        reportVote();

        if (!askMayor()) {
            announcementOfVotingResults();
        }
        reportResult();

    }

    public void reportResult() {
        if (suspectedPlayer.size() == 0) {
            sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "\nNone of the people had enough votes to leave the game." + Color.RESET);
        } else {
            String message = "\n";

            for (int i = 0; i < suspectedPlayer.size(); i++) {
                if (i == suspectedPlayer.size() - 1) {
                    message += suspectedPlayer.get(i).getName();
                    break;
                }
                message += suspectedPlayer.get(i).getName() + " , ";
            }
            sendMessageToAll(Color.CYAN_BOLD_BRIGHT + message + " died :)\n" + Color.RESET);
        }
    }

    public void waitForVoteEnd() {
        while (true) {
            if (voteIsDone()) {
                break;
            }
        }
//        try {
//            TimeUnit.SECONDS.sleep(30);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        try {
//            Thread.sleep(30*1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        forceFinishVot();

    }

    public boolean voteIsDone() {
        if (votingPhaseHandlers == null)
            return true;
        for (VotingPhaseHandler handler : votingPhaseHandlers) {
            if (handler.isAlive())
                return false;
        }
        return true;
    }

    public boolean askMayor() {
        for (Player player : players) {
            if (player.getRole() instanceof Mayor) {
                int answer = ((Mayor) player.getRole()).act(player);
                if (answer == 1)
                    return true;
                else return false;
            }
        }
        return false;
    }

    public void sendExitMessage() {
        for (VotingPhaseHandler handler : votingPhaseHandlers) {
            if (handler.isAlive()) {
                try {
                    handler.getThePlayer().getWriter().writeUTF(Color.CYAN_UNDERLINED+"write any text you want" +
                            " , or click enter to exit the voting phase :)" + Color.RESET);
                } catch (IOException e) {
                    players.remove(handler.getThePlayer());
                    sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + handler.getThePlayer().getName()
                            + " is out of the game." + Color.RESET);
                }
            }
        }
    }

    public void sendMessageToAll(String message) {
        for (Player player : players) {
            try {
                player.getWriter().writeUTF(message);
            } catch (IOException e) {
                players.remove(player);
                sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + player.getName()
                        + " is out of the game." + Color.RESET);
            }
        }
        for (Player player : deadPlayers) {
            try {
                player.getWriter().writeUTF(message);
            } catch (IOException e) {
                deadPlayers.remove(player);
                sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + player.getName()
                        + " is out of the game." + Color.RESET);
            }
        }
    }

    public int findMax() {
        int max = 0;
        for (Map.Entry<Player, Integer> player : voteResults.entrySet()) {
            if (player.getValue() > max) {
                max = player.getValue();
            }
        }
        return max;
    }

    public void announcementOfVotingResults() {
        int max = findMax();
        if (max == 0)
            return;
        for (Map.Entry<Player, Integer> player : voteResults.entrySet()) {
            if (player.getValue() == max) {
                suspectedPlayer.add(player.getKey());
            }
        }

        if (suspectedPlayer.size() > 1)
            return;

        for (Player player : suspectedPlayer) {
            if (players.contains(player)) {
                try {
                    player.getWriter().writeUTF(Color.CYAN_BOLD_BRIGHT + "You are dead :(" + Color.RESET);
                    players.remove(player);
                    deadPlayers.add(player);
                    (new ExitHandler(player, game)).start();
                } catch (IOException e) {
                    players.remove(player);
                    sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + player.getName()
                            + " is out of the game." + Color.RESET);
                }
            }
        }
    }

    public void reportPlayersVot() {

        sendMessageToAll(Color.CYAN + "\n....................................................................\n" + Color.RESET);

        sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "List of players and people who voted for them.\n" + Color.RESET);

        for (Map.Entry<Player, Player> playerEntry : playersVote.entrySet()) {
            if (players.contains(playerEntry.getKey())) {
                sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "* " + playerEntry.getKey().getName() + Color.RESET
                        + Color.CYAN_BOLD + " has voted for " + playerEntry.getValue().getName() + Color.RESET);
            }
//            else {
//                sendMessageToAll(+playerEntry.getKey().getName() + " wanted to get out of the game");
//            }
        }

    }

    public void reportVote() {


        sendMessageToAll(Color.CYAN + "\n....................................................................\n" + Color.RESET);

        sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "The voting results are as follows:\n" + Color.RESET);


        for (Map.Entry<Player, Integer> player : voteResults.entrySet()) {
            if (players.contains(player.getKey())) {
                sendMessageToAll(Color.CYAN_BOLD + "* " + player.getKey().getName() + " : " + player.getValue() + Color.RESET);
            }
//            else {
//                sendMessageToAll(player.getKey().getName() + " is out of the game ... ");
//            }
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

    public LinkedList<VotingPhaseHandler> getVotingPhaseHandlers() {
        return votingPhaseHandlers;
    }

    public void forceFinishVot() {
        for (VotingPhaseHandler handler : votingPhaseHandlers) {
            handler.setEnd(true);
        }
    }


}
