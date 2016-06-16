package GameObject;

import Action.Buff;
import Player.Player;

import java.rmi.Remote;
import java.util.List;

public interface IUnit extends Remote {

    /**
     * Aktualisiert alle Werte der Einheit die nicht direkt bearbeitet werden
     */
    public List<Buff> update();

    /**
     * getter und setter
     */
    public void setType(UnitType type);

    public UnitType getType();

    public void setMaxHp(int maxHp);

    public int getMaxHp();

    public void setCurrentHp(int currentHp);

    public int getCurrentHp();

    public void setAtk(int atk);

    public int getAtk();

    public void setDef(int def);

    public int getDef();

    public void setMovePoints(int movePoints);

    public int getMovePoints();

    public void setRange(int range);

    public int getRange();

    public void setSpriteName(String spriteName);

    public String getSpriteName();

    public void setOwner(Player player);

    public Player getOwner();

    public void setRessources(int ressources[]);

    public int[] getRessources();

    public void setField(Field field);

    public Field getField();
}
