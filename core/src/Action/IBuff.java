package Action;


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
}
