package Roles;

public class Pitman extends MainRoles{

    public Pitman() {
        super("Pitman");
    }

    @Override
    public String nightQuestion() {
        return null;
    }

    @Override
    public boolean isMafia() {
        return false;
    }
}
