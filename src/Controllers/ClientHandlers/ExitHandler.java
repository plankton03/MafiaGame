package Controllers.ClientHandlers;

import Design.Color;
import Game.Game;
import Player.Player;

import java.io.IOException;

/**
 * The type Exit handler.
 *
 * @author : Fatemeh Abdi
 */
public class ExitHandler extends Thread {

    private Player thePlayer;
    private Game game;

    /**
     * Instantiates a new Exit handler.
     *
     * @param thePlayer the the player
     * @param game      the game
     */
    public ExitHandler(Player thePlayer, Game game) {
        this.thePlayer = thePlayer;
        this.game = game;
    }


    @Override
    public void run() {
        try {
            thePlayer.getWriter().writeUTF(Color.PURPLE_UNDERLINED + "Please select one of the following options to" +
                    " continue the game" + Color.RESET);
            thePlayer.getWriter().writeUTF(Color.PURPLE + "1. Watch the rest of the game\n2. Exit" + Color.RESET);

            while (true) {
                try {
                    String ans = thePlayer.getReader().readUTF();
                    if (isInteger(ans) && isValidAnswer(Integer.parseInt(ans))) {
                        int answer = Integer.parseInt(ans);
                        if (answer == 1) {
                            game.getDeadPlayers().add(thePlayer);
                            break;

                        } else {
                            thePlayer.getWriter().writeUTF("Exit");
                            break;
                        }
                    }
                } catch (IOException e) {
                    thePlayer.getWriter().writeUTF("Exit");
                    break;
                }
            }
        } catch (IOException e) {
            return;
        }
    }

    /**
     * Is valid answer boolean.
     *
     * @param answer the answer
     * @return the boolean
     */
    public boolean isValidAnswer(int answer) {
        if (answer == 1 || answer == 2)
            return true;
        return false;
    }

    /**
     * Is integer boolean.
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
