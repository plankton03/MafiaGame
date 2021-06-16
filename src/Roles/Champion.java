package Roles;

public class Champion extends MainRoles{

    public Champion() {
        super("Champion");
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
