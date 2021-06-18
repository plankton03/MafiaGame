package Controllers.ClientHandlers;

import Controllers.PhaseControllers.VotingPhaseController;
import Player.Player;

import javax.swing.text.PlainDocument;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class VotingPhaseHandler extends Thread {

    //TODO  :
    // prepare message
    // get answer
    // check and match answer
    // updating list in controller
    // end

    //TODO : Vghti hazf mishe va ray dadan behesh


    private VotingPhaseController controller;
    private Player thePlayer;
    private int answer = 0;
    private boolean end = false;


    public VotingPhaseHandler(VotingPhaseController controller, Player thePlayer) {
        this.controller = controller;
        this.thePlayer = thePlayer;
    }

    @Override
    public void run() {
        startVoting();
    }

    public void startVoting() {
        try {
            thePlayer.getWriter().writeUTF(playersListToVote());

            while (true) {
                String answer = thePlayer.getReader().readUTF();
                if (isInteger(answer)) {
                    this.answer = Integer.parseInt(answer);
                    if (isValidAnswer())
                    {
                        matchAnswer();
                        break;
                    }
                }

                thePlayer.getWriter().writeUTF("The answer you entered is not a valid answer:( Please try again.");
            }
        } catch (IOException e) {
            System.out.println("player : "+thePlayer.getName()+" disconnected");
//            e.printStackTrace();
        }
    }

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
        String voteMessage = "Players you can vote for as Mafia\n";
        for (String name : names)
            voteMessage += name + "\n";
        return voteMessage;
    }

    public void matchAnswer() {

        if (answer == 0) {
//            controller.getPlayersVote().put(thePlayer, null);
            return;
        }
        ConcurrentHashMap<Player, Integer> voteResult = controller.getVoteResults();

        int index = 1;


        for (Player player : voteResult.keySet()) {
            if (player.equals(thePlayer))
                continue;
            if (index == answer) {
                controller.getPlayersVote().put(thePlayer, player);
                voteResult.put(player, voteResult.get(player) + 1);
            }
            index++;
        }
    }

    public boolean isValidAnswer() {
        if (answer >= 0 && answer <= controller.getVoteResults().size()-1)
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
}
