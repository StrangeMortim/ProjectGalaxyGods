package GameObject;


import Action.Action;

import java.util.List;

public interface IBase {

    /**
     * Erstellt eine neue Unit vom uebergebenen Typ
     * @param type der Typ der gewuenschten Unit
     * @return ob der Vorgang moeglich ist(und dementsprechend ausgefuehrt wird)
     */
    public Action createUnit(UnitType type);

    /**
     * Bricht das Erstellen der uebergebenen Einheit ab,
     * wenn diese Einheit bereits fertig erstellt ist oder nicht existiert(null) passiert nichts)
     * @param which die Einheit deren Erstellung abgebrochen werden soll
     */
    public void abortCreation(Unit which);

    /**
     * Startet den Bau des Labors
     * @return gibt an ob der Bau gestartet ist, false wenn nicht alle bedingungen erfuellt sind
     */
    public boolean buildLab();

    /**
     * Bricht den Bau des Labors ab, wenn er statt findet sonst passiert nichts
     * Der Spieler erhaelt einen Teil der Einheiten zurueck, abhaengig vom Fortschritt
     */
    public void abortLab();

    /**
     * Analog zu buildLab fuer die Kaserne
     */
    public boolean buildCaserne();

    /**
     * Analog zu abortLab fuer die Kaserne
     */
    public void abortCaserne();

    /**
     * Startet das Erforschen des uebergebenen Forschungsobjektes
     * @return true wenn der Start erfolgreich war(alle bedingungen erfuellt und noch nicht erforscht), sonst false
     */
    public boolean research(Research research);

    /**
     * Bricht die Erforschung der uebergebenen Forschung ab
     * wird die Forschung nicht erforscht passiert nichts
     */
    public void abortResearch(Research research);

    /**
     * Getter und Setter
     */
    public void setLabRoundsRemaining(int remaining);

    public int getLabRoundsRemaining();

    public void setCaserneRoundsRemaining(int remaining);

    public int getCaserneRoundsRemaining();

    public void setAvaibleUnits(List<UnitType> avaibleUnits);

    public List<UnitType> getAvaibleUnits();

    public void setResearched(List<Research> researched);

    public List<Research> getResearched();
}
