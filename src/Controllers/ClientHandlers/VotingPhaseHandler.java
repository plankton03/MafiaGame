package Controllers.ClientHandlers;

import Controllers.PhaseControllers.VotingPhaseController;
import Design.Color;
import Player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Voting phase handler.
 *
 * @author : Fatemeh Abdi
 */
public class VotingPhaseHandler extends Thread {


    private VotingPhaseController controller;
    private Player thePlayer;
    private int answer = 0;
    private boolean end = false;
    private long chatTime = 10 * 1000;
    private boolean hasActivity = false;


    /**
     * Instantiates a new Voting phase handler.
     *
     * @param controller the controller
     * @param thePlayer  the the player
     */
    public VotingPhaseHandler(VotingPhaseController controller, Player thePlayer) {
        this.controller = controller;
        this.thePlayer = thePlayer;
    }

    @Override
    public void run() {
        startVoting();
    }

    /**
     * Start voting.
     */
    public void startVoting() {

        String rcv;
//
        try {
            thePlayer.getWriter().writeUTF(playersListToVote());

        } catch (IOException e) {

        }


        while (true){
            gettingAnswer();
            if (end){
                break;
            }
        }

        matchAnswer(+1);

        if (hasActivity == false){
            thePlayer.setInactive(thePlayer.getInactive()+1);
        }else {
            thePlayer.setInactive(0);
        }
    }

    /**
     * Gets the player.
     *
     * @return the the player
     */
    public Player getThePlayer() {
        return thePlayer;
    }

    /**
     * Gets answer.
     */
    public synchronized void gettingAnswer() {
        while (true) {
            try {
                String ans = thePlayer.getReader().readUTF();
                if (end)
                    return;
                hasActivity = true;
                if (isInteger(ans)) {
                    if (isValidAnswer(Integer.parseInt(ans))) {
                        this.answer = Integer.parseInt(ans);
                        thePlayer.getWriter().writeUTF(Color.CYAN_UNDERLINED + "If you wish, you can vote another." +
                                Color.RESET);
                        break;
                    }
                    else {
                        thePlayer.getWriter().writeUTF(Color.CYAN + "The answer you entered is not a" +
                                " valid answer" +
                                ":( Please try again." + Color.RESET);
                    }
                }else {
                    thePlayer.getWriter().writeUTF(Color.CYAN + "The answer you entered is not a" +
                            " valid answer" +
                            ":( Please try again." + Color.RESET);
                }
            } catch (IOException e) {
                break;
            }
        }
    }


    /**
     * Players list to vote string.
     *
     * @return the string
     */
//
    public String playersListToVote() {
        ArrayList<String> names = new ArrayList<>();
        names.add("0. No Body");
        int i = 1;
        for (Player player : controller.getVoteResults().keySet()) {
            if (player.equals(thePlayer))
                continue;
            names.add(i + ". " + player.getName());
            i++;
        }
        String voteMessage = "\t" + Color.CYAN_UNDERLINED + "Players you can vote for as Mafia\n" + Color.RESET;
        for (String name : names)
            voteMessage += Color.CYAN_BOLD_BRIGHT + "\t" + name + "\n" + Color.RESET;
        return voteMessage;
    }

    /**
     * Match answer.
     *
     * @param i the
     */
    public void matchAnswer(int i) {

        if (answer == 0) {
            return;
        }
        ConcurrentHashMap<Player, Integer> voteResult = controller.getVoteResults();

        int index = 1;


        for (Player player : voteResult.keySet()) {
            if (player.equals(thePlayer))
                continue;
            if (index == answer) {
                controller.getPlayersVote().put(thePlayer, player);
                voteResult.put(player, voteResult.get(player) + i);
            }
            index++;
        }
    }

    /**
     * Sets end.
     *
     * @param end the end
     */
    public void setEnd(boolean end) {
        this.end = end;
    }

    /**
     * Is valid answer boolean.
     *
     * @param ans the ans
     * @return the boolean
     */
    public boolean isValidAnswer(int ans) {
        if (ans >= 0 && ans <= controller.getVoteResults().size() - 1)
            return true;
        return false;

    }

    /**
     * chacks that string is an integer
     *
     * @param s the s
     * @return the boolean
     */
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
}
