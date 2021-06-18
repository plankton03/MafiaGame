package Roles;

public interface Role{
//    private boolean isReadyToEnd

    boolean isMafia();
    boolean isSilent();
    String getRole();
    void setSilent(boolean silent);

    // TODO : 1. act dare
    //  2 . act khodesho ba zaman misaze va javabo mide biroon
    //  3. handle zaman ba acte
    //  4. javab nabood khodesh okeyesh mikone
    //  5. validation ham hamintor
    //  6. khodesho start mikoneh
    //  7. baraye exit ye halati beza

}
