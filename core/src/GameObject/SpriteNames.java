package GameObject;

import java.util.Random;

/**
 * Created by Fabi on 19.07.2016.
 */
public enum  SpriteNames {
    NORMAL_FIELD, IRON_FIELD,FOREST,MINE, MIRACLE,
    BASE_UP_LEFT,BASE_UP_RIGHT,BASE_DOWN_LEFT_CASERNE,BASE_DOWN_LEFT_EMPTY,BASE_DOWN_RIGHT_EMPTY,BASE_DOWN_RIGHT_CASERNE,BASE_DOWN_RIGHT_LAB,BASE_DOWN_RIGHT_FULL,
    CHEST_ICON, GOLD_ICON, MANA_ICON, IRON_ICON, WOOD_ICON,
    BUTTON_BG, BUTTON_WORKER, MENU_BG,
    MARKETPLACE, TECHTREE,
    ARCHER, SPEARFIGHTER, SWORDFIGHTER, WORKER, HERO;

    public String getSpriteName(){
        String folder = "assets/sprites/";
        switch (this){
            case NORMAL_FIELD:
                return folder+"normal" + new Random().nextInt(Constants.NUMBER_NORMAL_FIELDS) + ".png";
            case IRON_FIELD:
                return folder+"ironNoMine"+new Random().nextInt(Constants.NUMBER_MINE_FIELDS)+".png";
            case FOREST:
                return folder+"forest.png";
            case MIRACLE:
                return folder+"miracle.png";
            case BASE_UP_LEFT:
                return folder+"baseFullLeft.png";
            case BASE_UP_RIGHT:
                return folder+"baseFullRight.png";
            case BASE_DOWN_LEFT_CASERNE:
                return folder+"baseDownLeftCaserne.png";
            case BASE_DOWN_LEFT_EMPTY:
                return folder+"baseDownLeftEmpty.png";
            case BASE_DOWN_RIGHT_CASERNE:
                return folder+"baseDownRightCaserne.png";
            case BASE_DOWN_RIGHT_EMPTY:
                return folder+"baseDownRightEmpty.png";
            case BASE_DOWN_RIGHT_FULL:
                return folder+"baseDownRightFull.png";
            case BASE_DOWN_RIGHT_LAB:
                return folder+"baseDownRightLabor.png";
            case BUTTON_BG:
                return folder+"buttonBackground.png";
            case BUTTON_WORKER:
                return folder+"buttonArbeiter.png";
            case CHEST_ICON:
                return folder+"chest.png";
            case GOLD_ICON:
                return folder+"gold.png";
            case MANA_ICON:
                return folder+"mana.png";
            case IRON_ICON:
                return folder+"iron.png";
            case WOOD_ICON:
                return folder+"wood.png";
            case MARKETPLACE:
                return folder+"marketplace.png";
            case ARCHER:
                return folder+"archer.png";
            case SPEARFIGHTER:
                return folder+"spearfighter.png";
            case SWORDFIGHTER:
                return folder+"swordfighter.png";
            case WORKER:
                return folder+"worker.png";
            case HERO:
                return folder+"hero.png";
            case MENU_BG:
                return folder+"menuBackground.png";
            default:
                return "";
        }
    }

    public String getSpecificName(int spec){
        String folder = "assets/sprites/";
        switch (this){
            case NORMAL_FIELD:
                return folder+"normal"+spec+".png";
            case IRON_FIELD:
                return folder+"ironNoMine"+spec+".png";
            default:
                return this.getSpriteName();
        }
    }
}
