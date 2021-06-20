package Roles;

/**
 * The type Sim citizen.
 *
 * @author : Fatemeh Abdi
 */
public class SimCitizen extends SimRoles {


    /**
     * Instantiates a new Sim citizen.
     */
    public SimCitizen() {
        super("Simple Citizen");
    }

    @Override
    public boolean isMafia() {
        return false;
    }
}
