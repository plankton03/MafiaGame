package Roles;


public abstract class MainRoles implements Role {
    public MainRoles() {
        nightChoice = -1;
    }

    private int nightChoice;


    public int getNightChoice() {
        return nightChoice;
    }

    public void setNightChoice(int nightChoice) {
        this.nightChoice = nightChoice;
    }

    public abstract String nightQuestion();


}
