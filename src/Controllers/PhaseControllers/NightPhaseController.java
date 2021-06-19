package Controllers.PhaseControllers;

import Controllers.ClientHandlers.ChatPhaseHandler;
import Controllers.ClientHandlers.ExitHandler;
import Controllers.ClientHandlers.MafiaChatHandler;
import Game.Game;
import Player.Player;
import Roles.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class NightPhaseController {


    private Game game;
    private LinkedList<Player> players;
    private LinkedList<Player> deadPlayers;
    private ArrayList<Player> mafiaPlayers;
    private ArrayList<MafiaChatHandler> chatHandlers = new ArrayList<>();
//    private boolean chatIsOver ;
    private HashMap<Role, Integer> answers;
    private long chatTime = 1000*1000;

    private ArrayList<Player> deadPlayersAtNight;

    public NightPhaseController(Game game) {
//        chatIsOver = false;
        this.game = game;
        players = game.getPlayers();
        deadPlayers = game.getDeadPlayers();

        mafiaPlayers = new ArrayList<Player>();

        for (Player player : players) {
            if (player.getRole().isMafia())
                mafiaPlayers.add(player);
        }

        answers = new HashMap<>();
        deadPlayersAtNight = new ArrayList<Player>();
    }

    public ArrayList<Player> getMafiaPlayers() {
        return mafiaPlayers;
    }

    //todo : if baraye estelam

    public void startNightEvents() {

//        reset();

        sendMessageToAll("Starting night ...");
        startMafiaChat();
//
        System.out.println("Ending chat");
        askQuestions();

        applyResults();

        sendMessageToAll("Night events ... ");
        reportNightEvents();

        removeDeadPlayers();

    }

    public void reportNightEvents() {
        String message = "";
        if (deadPlayersAtNight.size() == 0)
            message = "Nobody";
        for (int i = 0; i < deadPlayersAtNight.size(); i++) {
            if (i == deadPlayersAtNight.size() - 1) {
                message += deadPlayersAtNight.get(i).getName();
                break;
            }
            message += deadPlayersAtNight.get(i).getName() + " , ";
        }
        message += " died :)";
        sendMessageToAll(message);
    }

    public void removeDeadPlayers() {
        if (deadPlayersAtNight.size() == 0)
            return;
        for (Player player : deadPlayersAtNight) {
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

    public void applyResults() {

        for (Player player : players) {

            Role role = player.getRole();
            if (!answers.containsKey(role))
                continue;
            if (answers.get(role) == 0)
                continue;
            if (role instanceof Champion) {
                if (matchAnswers(answers.get(role), player).getRole().isMafia()) {
                    deadPlayersAtNight.add(matchAnswers(answers.get(role), player));
                } else {
                    deadPlayersAtNight.add(player);
                }
            } else if (role instanceof GotFather) {
                deadPlayersAtNight.add(matchAnswers(answers.get(role), player));
            } else if (role instanceof Psychologist) {
                matchAnswers(answers.get(role), player).getRole().setSilent(true);
            } else if (role instanceof Detective) {
                Player chosenPlayer = matchAnswers(answers.get(role), player);
                try {
                    if (chosenPlayer.getRole() instanceof GotFather || !chosenPlayer.getRole().isMafia())
                        player.getWriter().writeUTF("Citizen");
                    else
                        chosenPlayer.getWriter().writeUTF("Mafia");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Player player : players) {
            if (player.getRole() instanceof DrLector || player.getRole() instanceof DrCity) {
                deadPlayersAtNight.remove(players.get(answers.get(player.getRole()) - 1));
            }
        }
    }

    public Player matchAnswers(int answer, Player thePlayer) {
        int index = 1;
        for (Player player : players) {
            if (player.equals(thePlayer)) {
                continue;
            }
            if (index == answer)
                return player;
            index++;
        }
        return null;
    }

    public void startMafiaChat() {

        for (Player player : mafiaPlayers) {
            MafiaChatHandler mafiaChatHandler = new MafiaChatHandler(this, player);
            chatHandlers.add(mafiaChatHandler);
            mafiaChatHandler.start();
        }

        long start = System.currentTimeMillis();
        long end = start + chatTime;

        while (System.currentTimeMillis() < end) {
            if (chatIsOver()) {
                System.out.println("Shat is over in while");
                break;
            }
        }
//        setChatIsOver(true);

    }

    public boolean chatIsOver(){
        if (chatHandlers.size() == 0)
            return true;
        for (MafiaChatHandler handler : chatHandlers){
            if (handler.isAlive())
                return false;
        }
        return true;
    }



    public void exitChat() {
        for (MafiaChatHandler handler : chatHandlers) {
            handler.setExitChat(true);
        }
    }

    public ArrayList<MafiaChatHandler> getChatHandlers() {
        return chatHandlers;
    }

    public void sendMessageToAll(String message) {
        for (Player player : players) {
            try {
                player.getWriter().writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Player player : game.getDeadPlayers()){
            try {
                player.getWriter().writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToAll(String msg, MafiaChatHandler handler) {
        String message ;
        for (MafiaChatHandler player : chatHandlers) {
            if (player.equals(handler)) {
                try {
                    player.getThePlayer().getWriter().writeUTF("You : " + msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                continue;
            }
            message = handler.getThePlayer().getName() + " : " + msg;
            try {
                player.getThePlayer().getWriter().writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Player player : deadPlayers) {
            message = handler.getThePlayer().getName() + " : " + msg;
            try {
                player.getWriter().writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void setChatIsOver(boolean chatIsOver) {
        for (MafiaChatHandler handler : chatHandlers){
            System.out.println("Chat is over in method  ");
            handler.setExitChat(true);
        }
//        this.chatIsOver = chatIsOver;
    }

    public void askQuestions() {
        askGotFather();
        askLectorDr();
        askCityDr();
        askDetective();
        askChampion();
        askPsychologist();
        askPitman();
    }

    public void askGotFather() {
        for (Player player : players) {
            if (player.getRole() instanceof GotFather) {
                int answer = ((GotFather) player.getRole()).act(players, player);
                answers.put(player.getRole(), answer);
                return;
            }
        }
    }

    public void askLectorDr() {
        for (Player player : players) {
            if (player.getRole() instanceof DrLector) {
                int answer = ((DrLector) player.getRole()).act(player, players, mafiaPlayers.size());
                answers.put(player.getRole(), answer);
                return;
            }
        }
    }


    public void askCityDr() {
        for (Player player : players) {
            if (player.getRole() instanceof DrCity) {
                int answer = ((DrCity) player.getRole()).act(player, players);
                answers.put(player.getRole(), answer);
                return;
            }
        }
    }

    public void askDetective() {
        for (Player player : players) {
            if (player.getRole() instanceof Detective) {
                int answer = ((Detective) player.getRole()).act(players, player);
                answers.put(player.getRole(), answer);
                return;
            }
        }
    }

    public void askChampion() {
        for (Player player : players) {
            if (player.getRole() instanceof Champion) {
                int answer = ((Champion) player.getRole()).act(players, player);
                answers.put(player.getRole(), answer);
                return;
            }
        }
    }

    public void askPsychologist() {
        for (Player player : players) {
            if (player.getRole() instanceof Psychologist) {
                int answer = ((Psychologist) player.getRole()).act(players, player);
                answers.put(player.getRole(), answer);
                return;
            }
        }
    }


    public void askPitman() {

        for (Player player : players) {
            if (player.getRole() instanceof Pitman) {
                Pitman pitman = (Pitman) player.getRole();
                int answer = pitman.act(player);
                answers.put(pitman, answer);
                return;

            }
        }

    }

    public Player findPlayerBasedOnRole(String role) {
        for (Player player : players) {
            if (player.getRole().getRole().equals(role))
                return player;
        }
        return null;
    }
}
