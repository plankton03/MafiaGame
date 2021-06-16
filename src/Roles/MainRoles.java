package Roles;


public abstract class MainRoles implements Role {

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
}
