package GameObject;


import Action.Action;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IBase extends Remote {

    /**
     * Erstellt eine neue Unit vom uebergebenen Typ
     * @param type der Typ der gewuenschten Unit
     * @return ob der Vorgang moeglich ist(und dementsprechend ausgefuehrt wird)
     */
    public boolean createUnit(UnitType type)throws RemoteException;

    /**
     * Bricht das Erstellen der uebergebenen Einheit ab,
     * wenn diese Einheit bereits fertig erstellt ist oder nicht existiert(null) passiert nichts)
     * @param which die Einheit deren Erstellung abgebrochen werden soll
     */
    public void abortCreation(Unit which)throws RemoteException;

    /**
     * Startet den Bau des Labors
     * @return gibt an ob der Bau gestartet ist, false wenn nicht alle bedingungen erfuellt sind
     */
    public boolean buildLab()throws RemoteException;

    /**
     * Bricht den Bau des Labors ab, wenn er statt findet sonst passiert nichts
     * Der Spieler erhaelt einen Teil der Einheiten zurueck, abhaengig vom Fortschritt
     */
    public void abortLab()throws RemoteException;

    /**
     * Analog zu buildLab fuer die Kaserne
     */
    public boolean buildCaserne()throws RemoteException;

    /**
     * Analog zu abortLab fuer die Kaserne
     */
    public void abortCaserne()throws RemoteException;

    /**
     * Baut den Marktplatz
     */
    public boolean buildMarket() throws RemoteException;

    /**
     * Startet das Erforschen des uebergebenen Forschungsobjektes
     * @return true wenn der Start erfolgreich war(alle bedingungen erfuellt und noch nicht erforscht), sonst false
     */
    public boolean research(Research research)throws RemoteException;

    /**
     * Bricht die Erforschung der uebergebenen Forschung ab
     * wird die Forschung nicht erforscht passiert nichts
     */
    public void abortResearch(Research research)throws RemoteException;

    /**
     * Getter und Setter
     */
    public void setLabRoundsRemaining(int remaining)throws RemoteException;

    public int getLabRoundsRemaining()throws RemoteException;

    public void setCaserneRoundsRemaining(int remaining)throws RemoteException;

    public int getCaserneRoundsRemaining()throws RemoteException;

    public void setAvaibleUnits(List<UnitType> avaibleUnits)throws RemoteException;

    public List<UnitType> getAvaibleUnits()throws RemoteException;

    public void setResearched(List<Research> researched)throws RemoteException;

    public List<Research> getResearched()throws RemoteException;
}
