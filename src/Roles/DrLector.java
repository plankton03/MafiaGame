package Roles;

public class DrLector extends MainRoles{

    public DrLector() {
        super("Doctor Lector");
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
