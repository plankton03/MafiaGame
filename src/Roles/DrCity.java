package Roles;

public class DrCity extends MainRoles{

    public DrCity() {
        super("City Doctor");
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
