package Roles;

/**
 * The type Sim mafia.
 *
 * @author : Fatemeh Abdi
 */
public class SimMafia extends SimRoles {

    /**
     * Instantiates a new Sim mafia.
     */
    public SimMafia() {
        super("Simple Mafia");
    }

    @Override
    public boolean isMafia() {
        return true;
    }
}
