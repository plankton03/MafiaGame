package Roles;

public class Detective extends MainRoles{

    public Detective() {
        super("Detective");
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
