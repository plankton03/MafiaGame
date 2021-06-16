package Roles;

public class Mayor extends MainRoles{

    public Mayor() {
        super("Mayor");
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
