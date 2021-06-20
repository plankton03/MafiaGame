package Roles;


import Design.Color;
import Player.Player;

import java.io.IOException;
import java.util.ConcurrentModificationException;

/**
 * The type Main roles.
 *
 * @author : Fatemeh Abdi
 */
public abstract class MainRoles implements Role {
    /**
     * The Answer.
     */
    protected int answer;
    private boolean isSilent = false;

    /**
     * Instantiates a new Main roles.
     *
     * @param role the role
     */
    public MainRoles(String role) {
        this.role = role;
        nightChoice = -1;
    }

    private int nightChoice;
    private String role;

    @Override
    public boolean isSilent() {
        return isSilent;
    }

    public void setSilent(boolean silent) {
        isSilent = silent;
    }

    @Override
    public String getRole() {
        return role;
    }

    /**
     * Is valid answer boolean.
     *
     * @param answer     the answer
     * @param startRange the start range
     * @param endRange   the end range
     * @return the boolean
     */
    public boolean isValidAnswer(int answer, int startRange, int endRange) {
        if (answer >= startRange && answer <= endRange)
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

    /**
     * Gets answer.
     *
     * @param thePlayer the the player
     * @param sRange    the s range
     * @param eRange    the e range
     * @return the answer
     * @throws IOException the io exception
     */
    public int getAnswer(Player thePlayer, int sRange, int eRange) throws IOException {
        while (true) {
            try {
                String ans = thePlayer.getReader().readUTF();
                if (isInteger(ans)) {
                    answer = Integer.parseInt(ans);
                    if (isValidAnswer(answer, sRange, eRange)) {
                        thePlayer.getWriter().writeUTF(Color.CYAN_UNDERLINED + "Your answer was received." + Color.RESET);
                        return answer;
                    } else {
                        thePlayer.getWriter().writeUTF(Color.PURPLE_UNDERLINED +
                                "The input entered is invalid :( Please try again ..." + Color.RESET);
                    }
                } else {
                    thePlayer.getWriter().writeUTF(Color.PURPLE_UNDERLINED +
                            "The input entered is invalid :( Please try again ..." + Color.RESET);
                }
            } catch (IOException e) {
                System.out.println(thePlayer.getName() + " can't answer.");
                return 0;
            }
        }
    }
}
