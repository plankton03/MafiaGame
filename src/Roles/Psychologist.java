package Roles;

public class Psychologist extends MainRoles{

    public Psychologist() {
        super("Psychologist");
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
