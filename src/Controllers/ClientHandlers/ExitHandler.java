package Controllers.ClientHandlers;

import Game.Game;
import Player.Player;

import java.io.IOException;

public class ExitHandler extends Thread{

    private Player thePlayer;
    private Game game;
//    private int answer;

    public ExitHandler(Player thePlayer, Game game) {
        this.thePlayer = thePlayer;
        this.game = game;
    }


    @Override
    public void run() {
        try {
            thePlayer.getWriter().writeUTF("Please select one of the following options to continue the game");
            thePlayer.getWriter().writeUTF("1. Watch the rest of the game\n2. Exit");

            while (true){
                String ans = thePlayer.getReader().readUTF();
                if (isInteger(ans) && isValidAnswer(Integer.parseInt(ans))){
                    int answer =Integer.parseInt(ans);
                    if (answer == 1)
                        break;
                    else
                    {
                        game.getDeadPlayers().remove(thePlayer);
                        thePlayer.getWriter().writeUTF("Exit");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public boolean isValidAnswer(int answer) {
        if (answer == 1 || answer == 2)
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
