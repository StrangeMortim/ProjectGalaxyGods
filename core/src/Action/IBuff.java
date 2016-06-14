package Action;


import GameObject.GameSession;
import GameObject.Research;
import GameObject.Unit;
import GameObject.UnitType;

public interface IBuff {

    /**
     * Getter und Setter
     */
    public void setPermanent(Boolean permanent);

    public Boolean getPermanent();

    public void setRoundsLeft(int rounds);

    public int getRoundsLeft();

    public void setAtk(int atk);

    public int getAtk();

    public void setDef(int def);

    public int getDef();

    public void setHp(int hp);

    public int getHp();

    public void setRange(int range);

    public int getRange();

    public void setMovePoints(int movePoints);

    public int getMovePoints();

    public void setGameSession(GameSession session);

    public GameSession getGameSession();

    public void setSource(Research source);

    public Research getSource();

    public boolean appliesForUnit(UnitType unit);

    public Buff getPersonalCopy(Unit u);
}
