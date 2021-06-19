package Controllers.ClientHandlers;

import Controllers.PhaseControllers.VotingPhaseController;
import Design.Color;
import Player.Player;

import java.io.IOException;
import java.util.ArrayList;
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
    private long chatTime = 30 * 1000;


    public VotingPhaseHandler(VotingPhaseController controller, Player thePlayer) {
        this.controller = controller;
        this.thePlayer = thePlayer;
    }

    @Override
    public void run() {
        startVoting();
    }

    public void startVoting() {
        String answer = "";
        try {
            thePlayer.getWriter().writeUTF(playersListToVote());

            long start = System.currentTimeMillis();
            long end = start + chatTime;
            while (System.currentTimeMillis() < end) {
                while (true) {
                    answer = thePlayer.getReader().readUTF();
                    if (isInteger(answer)) {

//                        this.answer = Integer.parseInt(answer);
                        if (isValidAnswer(Integer.parseInt(answer))) {
                            if (this.answer > 0) {
                                matchAnswer(-1);
                            }
                            this.answer = Integer.parseInt(answer);
                            matchAnswer(+1);
                            thePlayer.getWriter().writeUTF(Color.CYAN_UNDERLINED + "If you wish, you can vote another." +
                                    Color.RESET);
                        }
                    } else
                        thePlayer.getWriter().writeUTF(Color.WHITE_UNDERLINED + "The answer you entered is not a" +
                                " valid answer" +
                                ":( Please try again." + Color.RESET);
                }
            }
            if (answer.equals("")) {
                thePlayer.setInactive(thePlayer.getInactive() + 1);
                if (thePlayer.getInactive() == 3) {
                    try {
                        thePlayer.getWriter().writeUTF(Color.CYAN_BOLD_BRIGHT + "You are dead :(" + Color.RESET);
                        controller.getPlayers().remove(thePlayer);
                        controller.getDeadPlayers().add(thePlayer);
                        (new ExitHandler(thePlayer, controller.getGame())).start();
                    } catch (IOException e) {
                        controller.getGame().getPlayers().remove(this);
                        controller.sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " +
                                thePlayer.getName() + " is out of the game." + Color.RESET);
                    }
                }
            }
        } catch (IOException e) {
            controller.getGame().getPlayers().remove(this);
            controller.sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " +
                    thePlayer.getName() + " is out of the game." + Color.RESET);
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
