package GameObject;

import java.util.Random;

/**
 * Created by Fabi on 19.07.2016.
 */
public enum  SpriteNames {
    NORMAL_FIELD, IRON_FIELD,FOREST,MINE, MIRACLE,
    BASE_UP_LEFT,BASE_UP_RIGHT,BASE_DOWN_LEFT_CASERNE,BASE_DOWN_LEFT_EMPTY,BASE_DOWN_RIGHT_EMPTY,BASE_DOWN_RIGHT_CASERNE,BASE_DOWN_RIGHT_LAB,BASE_DOWN_RIGHT_FULL,
    CHEST_ICON, GOLD_ICON, MANA_ICON, IRON_ICON, WOOD_ICON,
    BUTTON_BG, BUTTON_WORKER, MENU_BG, BUTTON_SPEARFIGHTER, BUTTON_SWORDFIGHTER,BUTTON_GREY,BUTTON_GOLD,BUTTON_BLUE,
    MARKETPLACE, TECHTREE,
    ARCHER,ARCHERBACK, SPEARFIGHTER,SPEARFIGHTERBACK, SWORDFIGHTER,SWORDFIGHTERBACK, WORKER,WORKERBACK, HERO,HEROBACK;

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
            case ARCHERBACK:
                return folder+"archer.png";
            case SPEARFIGHTER:
                return folder+"spearfighter.png";
            case SPEARFIGHTERBACK:
                return folder+"spearfighterBack.png";
            case SWORDFIGHTER:
                return folder+"swordfighter.png";
            case SWORDFIGHTERBACK:
                return folder+"swordfighterBack.png";
            case WORKER:
                return folder+"worker.png";
            case WORKERBACK:
                return folder+"workerBack.png";
            case HERO:
                return folder+"hero.png";
            case HEROBACK:
                return folder+"heroBack.png";
            case MENU_BG:
                return folder+"menuBackground.png";
            case BUTTON_SPEARFIGHTER:
                return folder+"buttonSpearfighter.png";
            case BUTTON_SWORDFIGHTER:
                return folder+"buttonSwordfighter.png";
            case BUTTON_GREY:
                return folder+"buttonTreeGrey.png";
            case BUTTON_GOLD:
                return folder+"buttonTreeGold.png";
            case BUTTON_BLUE:
                return folder+"buttonTreeBlue.png";
            case TECHTREE:
                return folder+"treeBackground.png";
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
