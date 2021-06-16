package Roles;

public abstract class SimRoles implements Role{

    private String role;

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
