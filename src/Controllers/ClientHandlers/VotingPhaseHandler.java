package Controllers.ClientHandlers;

import Controllers.PhaseControllers.VotingPhaseController;
import Design.Color;
import Player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class VotingPhaseHandler extends Thread {


    private VotingPhaseController controller;
    private Player thePlayer;
    private int answer = 0;
    private boolean end = false;
    private long chatTime = 10 * 1000;


    public VotingPhaseHandler(VotingPhaseController controller, Player thePlayer) {
        this.controller = controller;
        this.thePlayer = thePlayer;
    }

    @Override
    public void run() {
        startVoting();
    }

    public void startVoting() {

        String rcv;
//
        boolean flag = false;
        try {
            thePlayer.getWriter().writeUTF(playersListToVote());

        } catch (IOException e) {

        }

//        long start = System.currentTimeMillis();
//        long end = start + chatTime;
//        while (System.currentTimeMillis() < end) {
//            gettingAnswer();
//        }
//        while ()
//        gettingAnswer();

//        while (true){
//            gettingAnswer();
//
//            if (end)
//                break;
//        }

//        int i = 0;
//        while (i < 3) {
//            gettingAnswer();
//            i++;
//            if (end)
//            {
//                System.out.println("in loop");
//                break;
//
//            }
//        }

        while (true){
            gettingAnswer();
            if (end){
                break;
            }
        }

        matchAnswer(+1);
    }

    public Player getThePlayer() {
        return thePlayer;
    }

    public synchronized void gettingAnswer() {

        while (true) {
            try {
                String ans = thePlayer.getReader().readUTF();
                if (end)
                    return;
                if (isInteger(ans)) {
                    if (isValidAnswer(Integer.parseInt(ans))) {
                        this.answer = Integer.parseInt(ans);
                        thePlayer.getWriter().writeUTF(Color.CYAN_UNDERLINED + "If you wish, you can vote another." +
                                Color.RESET);
                        break;
                    }
                }else {
                    thePlayer.getWriter().writeUTF(Color.CYAN + "The answer you entered is not a" +
                            " valid answer" +
                            ":( Please try again." + Color.RESET);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


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

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isValidAnswer(int ans) {
        if (ans >= 0 && ans <= controller.getVoteResults().size() - 1)
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
