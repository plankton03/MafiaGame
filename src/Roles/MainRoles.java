package Roles;


import Player.Player;

import java.io.IOException;

public abstract class MainRoles implements Role {
    protected int answer;

    public MainRoles(String role) {
        this.role = role;
        nightChoice = -1;
    }

    private int nightChoice;
    private String role;


    public int getNightChoice() {
        return nightChoice;
    }

    public void setNightChoice(int nightChoice) {
        this.nightChoice = nightChoice;
    }

    public abstract String nightQuestion();

    @Override
    public String getRole() {
        return role;
    }

//    public abstract int act( Player thePlayer);


    public boolean isValidAnswer(int answer, int startRange, int endRange) {
        if (answer >= startRange || answer <= endRange)
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

    public int getAnswer(Player thePlayer, int sRange, int eRange) throws IOException {
        while (true) {
            String ans = thePlayer.getReader().readUTF();
            if (isInteger(ans)) {
                answer = Integer.parseInt(ans);
                if (isValidAnswer(answer, sRange, eRange)) {
                    return answer;
                }
            }
            thePlayer.getWriter().writeUTF("The input entered is invalid :( Please try again ...");
        }
    }


}
