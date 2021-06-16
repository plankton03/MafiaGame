package Roles;

public class SimCitizen extends SimRoles{


    public SimCitizen() {
        super("Simple Citizen");
    }

    @Override
    public boolean isMafia() {
        return false;
    }
}
