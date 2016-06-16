package Action;

import GameObject.GameSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActionProcessor implements IActionProcessor,Serializable {

    private List<Action> toProcess = new ArrayList<Action>();
    private List<Buff> toReturn = new ArrayList<Buff>();
    private GameSession session = null;


    public ActionProcessor(GameSession session){
        if(session == null)
            throw new IllegalArgumentException("ActionProcessor: session is null");

        this.session = session;
    }

    /**
     * Getter und setter
     *
     * @param session
     */
    public void setSession(GameSession session) {
        this.session = session;
    }

    @Override
    public GameSession getSession() {
        return session;
    }

    /**
     * Fuegt eine neue zu verarbeitende Action hinzu
     *
     * @param toAdd die hinzu zu fuegenden Action
     */
    @Override
    public void addAction(Action toAdd) {
        if(toAdd == null)
            throw new IllegalArgumentException("addAction: Action is null");

        toAdd.setParent(this);
        toProcess.add(toAdd);
    }

    /**
     * Entfernt die angegebene Action aus den zu verarbeitenden
     * wenn die action nicht vorhanden ist oder null passiert nichts
     *
     * @param toRemove die zu entfernende Action
     */
    @Override
    public void removeAction(Action toRemove) {
        if(toRemove == null)
            throw new IllegalArgumentException("removeAction: Action is null");

        toRemove.setParent(null);
        toProcess.remove(toRemove);
    }

    /**
     * Verarbeitet alle Actions in toProcess ueber ihre execute-Methode,
     * die Actions fügen ggf. über ihr Parent Attribut selbst neue Buffs
     * hinzu falls sich welche ergeben
     *
     * @return toReturn mit allen in diesem Zug generierten Buffs
     */
    @Override
    public List<Buff> execute() {
        for(Action a: toProcess){
            a.execute();
            a.setParent(null);
        }

        return toReturn;
    }
}
