package Roles;

public class Mayor extends MainRoles{

    @Override
    public String nightQuestion() {
        return null;
    }

    @Override
    public boolean isMafia() {
        return false;
    }
}
