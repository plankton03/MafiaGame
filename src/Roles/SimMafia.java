package Roles;

public class SimMafia extends SimRoles{

    public SimMafia() {
        super("Simple Mafia");
    }

    @Override
    public boolean isMafia() {
        return true;
    }
}
