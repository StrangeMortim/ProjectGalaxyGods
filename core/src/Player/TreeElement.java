package Player;

import GameObject.Constants;

import java.rmi.RemoteException;

/**
 * Created by Fabi on 22.07.2016.
 */
public enum TreeElement implements ITreeElement {
    STEEL1, STEEL2, STEEL3, STEEL4, STEEL5,
    MAGIC1, MAGIC2, MAGIC3, MAGIC4, MAGIC5,
    CULTURE1, CULTURE2, CULTURE3, CULTURE4, CULTURE5;

    @Override
    public String getTreePath(){
        switch (this) {
            case STEEL1:
            case STEEL2:
            case STEEL3:
            case STEEL4:
            case STEEL5:
                return "steel";
            case MAGIC1:
            case MAGIC2:
            case MAGIC3:
            case MAGIC4:
            case MAGIC5:
                return "magic";
            case CULTURE1:
            case CULTURE2:
            case CULTURE3:
            case CULTURE4:
            case CULTURE5:
                return "culture";
            default:
                return "";
        }
    }

    @Override
    public int getPreRequisiteIndex() {
        switch (this) {
            case STEEL1:
                return -1;
            case STEEL2:
                return 0;
            case STEEL3:
                return 1;
            case STEEL4:
                return 2;
            case STEEL5:
                return 3;
            case MAGIC1:
                return -1;
            case MAGIC2:
                return 0;
            case MAGIC3:
                return 1;
            case MAGIC4:
                return 2;
            case MAGIC5:
                return 3;
            case CULTURE1:
                return -1;
            case CULTURE2:
                return 0;
            case CULTURE3:
                return 1;
            case CULTURE4:
                return 2;
            case CULTURE5:
                return 3;
            default:
                return -1;
        }
    }

    @Override
    public int[] getRessourceCosts(){
        switch (this) {
            case STEEL1:
                return new int[]{0, 0, 0, 0};
            case STEEL2:
                return new int[]{0, 0, 0, 0};
            case STEEL3:
                return new int[]{0, 0, 0, 0};
            case STEEL4:
                return new int[]{0, 0, 0, 0};
            case STEEL5:
                return new int[]{0, 0, 0, 0};
            case MAGIC1:
                return new int[]{0, 0, 0, 0};
            case MAGIC2:
                return new int[]{0, 0, 0, 0};
            case MAGIC3:
                return new int[]{0, 0, 0, 0};
            case MAGIC4:
                return new int[]{0, 0, 0, 0};
            case MAGIC5:
                return new int[]{0, 0, 0, 0};
            case CULTURE1:
                return new int[]{0, 0, 0, 0};
            case CULTURE2:
                return new int[]{0, 0, 0, 0};
            case CULTURE3:
                return new int[]{0, 0, 0, 0};
            case CULTURE4:
                return new int[]{0, 0, 0, 0};
            case CULTURE5:
                return new int[]{0, 0, 0, 0};
            default:
                return new int[4];
        }
    }

    @Override
    public void activate(Player player){
        if (player == null)
            throw new IllegalArgumentException("Player is null");
        try {
            switch (this) {
                case STEEL1:
                    player.getTechTree().getSteel()[0] = true;
                    activateSteelDefault(player);
                    System.out.println(player.getAccount().getName() + ": erreicht Stahl Stufe 1");
                    break;
                case STEEL2:
                    player.getTechTree().getSteel()[1] = true;
                    activateSteelDefault(player);
                    System.out.println(player.getAccount().getName() + ": erreicht Stahl Stufe 2");
                    break;
                case STEEL3:
                    player.getTechTree().getSteel()[2] = true;
                    activateSteelDefault(player);
                    System.out.println(player.getAccount().getName() + ": erreicht Stahl Stufe 3");
                    break;
                case STEEL4:
                    player.getTechTree().getSteel()[3] = true;
                    activateSteelDefault(player);
                    System.out.println(player.getAccount().getName() + ": erreicht Stahl Stufe 4");
                    break;
                case STEEL5:
                    player.getTechTree().getSteel()[4] = true;
                    activateSteelDefault(player);
                    player.getTechTree().setSteelFull(true);
                    System.out.println(player.getAccount().getName() + ": erreicht Stahl Stufe 5");
                    break;
                case MAGIC1:
                    player.getTechTree().getMagic()[0] = true;
                    activateMagicDefault(player);
                    System.out.println(player.getAccount().getName() + ": erreicht Magie Stufe 1");
                    break;
                case MAGIC2:
                    player.getTechTree().getMagic()[1] = true;
                    activateMagicDefault(player);
                    System.out.println(player.getAccount().getName() + ": erreicht Magie Stufe 2");
                    break;
                case MAGIC3:
                    player.getTechTree().getMagic()[2] = true;
                    activateMagicDefault(player);
                    System.out.println(player.getAccount().getName() + ": erreicht Magie Stufe 3");
                    break;
                case MAGIC4:
                    player.getTechTree().getMagic()[3] = true;
                    activateMagicDefault(player);
                    System.out.println(player.getAccount().getName() + ": erreicht Magie Stufe 4");
                    break;
                case MAGIC5:
                    player.getTechTree().getMagic()[4] = true;
                    activateMagicDefault(player);
                    player.getTechTree().setMagicFull(true);
                    System.out.println(player.getAccount().getName() + ": erreicht Magie Stufe 5");
                    break;
                case CULTURE1:
                    player.getTechTree().getCulture()[0] = true;
                    activateCultureDefault(player);
                    System.out.println(player.getAccount().getName() + ": erreicht Culture Stufe 1");
                    break;
                case CULTURE2:
                    player.getTechTree().getCulture()[1] = true;
                    activateCultureDefault(player);
                    System.out.println(player.getAccount().getName() + ": erreicht Culture Stufe 2");
                    break;
                case CULTURE3:
                    player.getTechTree().getCulture()[2] = true;
                    activateCultureDefault(player);
                    System.out.println(player.getAccount().getName() + ": erreicht Culture Stufe 3");
                    break;
                case CULTURE4:
                    player.getTechTree().getCulture()[3] = true;
                    activateCultureDefault(player);
                    System.out.println(player.getAccount().getName() + ": erreicht Culture Stufe 4");
                    break;
                case CULTURE5:
                    player.getTechTree().getCulture()[4] = true;
                    activateCultureDefault(player);
                    player.getTechTree().setCultureFull(true);
                    System.out.println(player.getAccount().getName() + ": erreicht Culture Stufe 5");
                    break;
                default:
                    break;
            }
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }


    private void activateSteelDefault(Player p){
        p.getRessourceBoni()[Constants.WOOD] += Constants.WOOD_BONI_VALUE;
        p.getRessourceBoni()[Constants.IRON] += Constants.IRON_BONI_VALUE;
    }

    private void activateMagicDefault(Player p){
        p.getRessourceBoni()[Constants.MANA] += Constants.MANA_BONI_VALUE;
    }

    private void activateCultureDefault(Player p){
        p.getRessourceBoni()[Constants.GOLD] += Constants.GOLD_BONI_VALUE;
    }
}
