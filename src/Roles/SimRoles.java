package Roles;

public abstract class SimRoles implements Role{

    private String role;
    private boolean isSilent = false;

    @Override
    public boolean isSilent() {
        return isSilent;
    }

    public void setSilent(boolean silent) {
        isSilent = silent;
    }

    protected SimRoles(String role) {
        this.role = role;
    }

    @Override
    public  abstract boolean isMafia() ;

    @Override
    public String getRole() {
        return role;
    }
}
