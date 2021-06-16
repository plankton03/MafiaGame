package Roles;

public class GotFather extends MainRoles{

    public GotFather() {
        super("GotFather");
    }

    @Override
    public String nightQuestion() {
        return null;
    }

    @Override
    public boolean isMafia() {
        return true;
    }
}
