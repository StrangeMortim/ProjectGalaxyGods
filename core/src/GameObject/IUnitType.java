package GameObject;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUnitType extends Remote {

    /**
     * Ermöglicht Zugriff auf die verschiedenen UnitTypes ueber einen String
     * @param name der Name des gesuchten Typs
     * @return der zugehoerige Typ
     */
    public static UnitType getTypeByName(String name)throws RemoteException {
        /*TODO*/
        return null;
    };

    /**
     * Getter fuer die jeweiligen werte der spezifischen einheit
     */
    public int getMaxHp()throws RemoteException;
    public int getAtk()throws RemoteException;
    public int getDef()throws RemoteException;
    public int getMovePoints()throws RemoteException;
    public int getRange()throws RemoteException;
    public String getSpriteName()throws RemoteException;
    public int getRecruitingTime()throws RemoteException;
    public int[] getRessourceCost()throws RemoteException;
}
