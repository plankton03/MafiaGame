package Roles;

/**
 * The type Sim roles.
 *
 * @author : Fatemeh Abdi
 */
public abstract class SimRoles implements Role {

    private String role;
    private boolean isSilent = false;

    @Override
    public boolean isSilent() {
        return isSilent;
    }

    public void setSilent(boolean silent) {
        isSilent = silent;
    }

    /**
     * Instantiates a new Sim roles.
     *
     * @param role the role
     */
    protected SimRoles(String role) {
        this.role = role;
    }

    @Override
    public abstract boolean isMafia();

    @Override
    public String getRole() {
        return role;
    }
}
