package Controllers.PhaseControllers;

import Controllers.ClientHandlers.ExitHandler;
import Controllers.ClientHandlers.MafiaChatHandler;
import Design.Color;
import Game.Game;
import Player.Player;
import Roles.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * The type Night phase controller.
 *
 * @author : Fatemeh Abdi
 */
public class NightPhaseController {


    private Game game;
    private LinkedList<Player> players;
    private LinkedList<Player> deadPlayers;
    private ArrayList<Player> mafiaPlayers;
    private ArrayList<MafiaChatHandler> chatHandlers = new ArrayList<>();
    private HashMap<Role, Integer> answers;
    private long chatTime = 5 * 60 * 1000;
    private boolean inquiry = false;


    /**
     * Gets game.
     *
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    private ArrayList<Player> deadPlayersAtNight;

    /**
     * Instantiates a new Night phase controller.
     *
     * @param game the game
     */
    public NightPhaseController(Game game) {
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

    /**
     * Gets mafia players.
     *
     * @return the mafia players
     */
    public ArrayList<Player> getMafiaPlayers() {
        return mafiaPlayers;
    }


    /**
     * Start night events.
     */
    public void startNightEvents() {


        sendMessageToAll(Color.CYAN + "\n....................................................................\n" + Color.RESET);

        sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "The night begins.\n" + Color.RESET);


        startMafiaChat();

        askQuestions();

        applyResults();

        removeDeadPlayers();

        reportNightEvents();

    }

    /**
     * Report night events.
     */
    public void reportNightEvents() {
        sendMessageToAll(Color.CYAN + "\n....................................................................\n" + Color.RESET);

        sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "The night is over.\n" + Color.RESET);

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
        sendMessageToAll(Color.CYAN_BOLD_BRIGHT + message + Color.RESET);

        if (inquiry) {
            reportRoles();
        }

        reportSilence();
    }

    /**
     * Report silence.
     */
    public void reportSilence() {
        for (Player player : players) {
            if (player.getRole().isSilent())
                sendMessageToAll(Color.CYAN_BOLD_BRIGHT + player.getName() + " can not speak today." + Color.RESET);
        }
    }

    /**
     * Report roles.
     */
    public void reportRoles() {
        String message = "\t";
        for (Player player : game.getBackupList()) {
            if (!players.contains(player)) {
                message += "   " + player.getRole().getRole();
            }
        }
        sendMessageToAll(Color.CYAN_BOLD_BRIGHT + message + Color.RESET);
    }

    /**
     * Remove dead players.
     */
    public void removeDeadPlayers() {
        if (deadPlayersAtNight.size() == 0)
            return;
        for (Player player : deadPlayersAtNight) {
            if (players.contains(player)) {
                try {
                    player.getWriter().writeUTF(Color.CYAN_BOLD_BRIGHT + "You are dead :(" + Color.RESET);
                    players.remove(player);
//                    deadPlayers.add(player);
                    (new ExitHandler(player, game)).start();
                } catch (ConcurrentModificationException c) {
                } catch (IOException e) {
                    game.getPlayers().remove(player);
                    sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + player.getName()
                            + " is out of the game." + Color.RESET);
                }
            }
        }
    }

    /**
     * Apply results.
     */
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
                        player.getWriter().writeUTF(Color.PURPLE + "\t\t* Citizen" + Color.RESET);
                    else
                        player.getWriter().writeUTF(Color.PURPLE + "\t\t* Mafia" + Color.RESET);
                } catch (ConcurrentModificationException c) {
                } catch (IOException e) {
                    sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + player.getName() + " is out of the game.");
                    players.remove(player);
                }
            } else if (role instanceof Pitman) {
                inquiry = true;
            }
        }

        for (Player player : players) {
            if (player.getRole() instanceof DrLector || player.getRole() instanceof DrCity) {
                if (!answers.containsKey(player.getRole()))
                    continue;
                if (answers.get(player.getRole()) == 0)
                    continue;
//                if (deadPlayersAtNight.contains(players.get(answers.get(player.getRole()) - 1)))
                deadPlayersAtNight.remove(players.get(answers.get(player.getRole()) - 1));
            }
        }
    }

    /**
     * Match answers player.
     *
     * @param answer    the answer
     * @param thePlayer the the player
     * @return the player
     */
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

    /**
     * Start mafia chat.
     */
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
                break;
            }
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Chat is over boolean.
     *
     * @return the boolean
     */
    public boolean chatIsOver() {
        if (chatHandlers.size() == 0)
            return true;
        for (MafiaChatHandler handler : chatHandlers) {
            if (handler.isAlive())
                return false;
        }
        return true;
    }


    /**
     * Gets chat handlers.
     *
     * @return the chat handlers
     */
    public ArrayList<MafiaChatHandler> getChatHandlers() {
        return chatHandlers;
    }

    /**
     * Send message to all.
     *
     * @param message the message
     */
    public void sendMessageToAll(String message) {
        for (Player player : players) {
            try {
                player.getWriter().writeUTF(message);
            } catch (ConcurrentModificationException c) {
            } catch (IOException e) {
                game.getPlayers().remove(player);
                sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + player.getName()
                        + " is out of the game." + Color.RESET);
            }
        }
        for (Player player : game.getDeadPlayers()) {
            try {
                player.getWriter().writeUTF(message);
            } catch (ConcurrentModificationException c) {
            } catch (IOException e) {
                deadPlayers.remove(player);
            }
        }
    }

    /**
     * Send message to all.
     *
     * @param msg     the msg
     * @param handler the handler
     */
    public void sendMessageToAll(String msg, MafiaChatHandler handler) {
        String message;
        for (MafiaChatHandler player : chatHandlers) {
            if (player.equals(handler)) {
                try {
                    player.getThePlayer().getWriter().writeUTF(Color.WHITE_UNDERLINED + "You" + Color.RESET +
                            "   " + Color.WHITE_BOLD_BRIGHT + msg + Color.RESET);
                } catch (ConcurrentModificationException c) {
                } catch (IOException e) {
                    game.getPlayers().remove(player.getThePlayer());
                    chatHandlers.remove(player);
                    sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + player.getThePlayer().getName()
                            + " is out of the game." + Color.RESET);
                }
            } else {
                message = Color.WHITE_UNDERLINED + handler.getThePlayer().getName() + Color.RESET + "   "
                        + Color.WHITE_BOLD_BRIGHT + msg + Color.RESET;
                try {
                    player.getThePlayer().getWriter().writeUTF(message);
                } catch (ConcurrentModificationException c) {
                } catch (IOException e) {
                    getGame().getPlayers().remove(player.getThePlayer());
                    chatHandlers.remove(player);
                    sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + player.getThePlayer().getName()
                            + " is out of the game." + Color.RESET);
                }
            }
        }
    }

    /**
     * Sets chat is over.
     *
     * @param chatIsOver the chat is over
     */
    public synchronized void setChatIsOver(boolean chatIsOver) {
        for (MafiaChatHandler handler : chatHandlers) {
            handler.setExitChat(true);
        }
    }

    /**
     * Ask questions.
     */
    public void askQuestions() {
        askGotFather();
        askLectorDr();
        askCityDr();
        askDetective();
        askChampion();
        askPsychologist();
        askPitman();
    }

    /**
     * Ask got father.
     */
    public void askGotFather() {
        for (Player player : players) {
            if (player.getRole() instanceof GotFather) {
                int answer = ((GotFather) player.getRole()).act(players, player);
                answers.put(player.getRole(), answer);
                return;
            }
        }
    }

    /**
     * Send exit message.
     *
     * @param message the message
     */
    public void sendExitMessage(String message) {
        for (MafiaChatHandler handler : chatHandlers) {
            if (handler.isAlive()) {
                try {
                    handler.getThePlayer().getWriter().writeUTF(Color.CYAN_UNDERLINED + message + Color.RESET);
                } catch (ConcurrentModificationException c) {
                } catch (IOException e) {
                    getGame().getPlayers().remove(handler.getThePlayer());
                    chatHandlers.remove(handler);
                    sendMessageToAll(Color.CYAN_BOLD_BRIGHT + "!!! " + handler.getThePlayer().getName()
                            + " is out of the game." + Color.RESET);
                }
            }
        }
    }

    /**
     * Ask lector dr.
     */
    public void askLectorDr() {
        for (Player player : players) {
            if (player.getRole() instanceof DrLector) {
                int answer = ((DrLector) player.getRole()).act(player, mafiaPlayers, players);
                answers.put(player.getRole(), answer);
                return;
            }
        }
    }


    /**
     * Ask city dr.
     */
    public void askCityDr() {
        for (Player player : players) {
            if (player.getRole() instanceof DrCity) {
                int answer = ((DrCity) player.getRole()).act(player, players);
                answers.put(player.getRole(), answer);
                return;
            }
        }
    }

    /**
     * Ask detective.
     */
    public void askDetective() {
        for (Player player : players) {
            if (player.getRole() instanceof Detective) {
                int answer = ((Detective) player.getRole()).act(players, player);
                answers.put(player.getRole(), answer);
                return;
            }
        }
    }

    /**
     * Ask champion.
     */
    public void askChampion() {
        for (Player player : players) {
            if (player.getRole() instanceof Champion) {
                int answer = ((Champion) player.getRole()).act(players, player);
                answers.put(player.getRole(), answer);
                return;
            }
        }
    }

    /**
     * Ask psychologist.
     */
    public void askPsychologist() {
        for (Player player : players) {
            if (player.getRole() instanceof Psychologist) {
                int answer = ((Psychologist) player.getRole()).act(players, player);
                answers.put(player.getRole(), answer);
                return;
            }
        }
    }


    /**
     * Ask pitman.
     */
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

}
