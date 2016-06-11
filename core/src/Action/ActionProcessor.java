package Action;

import java.util.List;

public class ActionProcessor implements IActionProcessor {

    private List<Action> toProcess;
    private List<Buff> toReturn;
    private GameSession session;


    public ActionProcessor(GameSession session){
        this.session = session;
    }

    /**
     * Getter und setter
     *
     * @param session
     */
    @Override
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
        toProcess.remove(toRemove);
    }

    /**
     * Verarbeitet alle Actions in toProcess ueber ihre execute-Methode,
     * nach jeder Action wird dabei ueberprueft ob sich fuer target oder origin der Action
     * neue Actions oder Buffs ergeben, diese werden dann generiert und hinzu gefuegt
     *
     * @return toReturn mit allen in diesem Zug generierten Buffs
     */
    @Override
    public List<Buff> execute() {
        return toReturn;
    }
}
