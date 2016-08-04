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
    MARKETPLACE, TECHTREE,TEAMBOX_OPEN,
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
            case TEAMBOX_OPEN:
                return folder+"chestOpen.png";
            default:
                return "";
        }
    }

    public int getSpriteIndex(){
        switch (this){
            case NORMAL_FIELD:
                return 0;
            case IRON_FIELD:
                return 1;
            case FOREST:
                return 2;
            case MIRACLE:
                return 3;
            case BASE_UP_LEFT:
                return 4;
            case BASE_UP_RIGHT:
                return 5;
            case BASE_DOWN_LEFT_CASERNE:
                return 6;
            case BASE_DOWN_LEFT_EMPTY:
                return 7;
            case BASE_DOWN_RIGHT_CASERNE:
                return 8;
            case BASE_DOWN_RIGHT_EMPTY:
                return 9;
            case BASE_DOWN_RIGHT_FULL:
                return 10;
            case BASE_DOWN_RIGHT_LAB:
                return 11;
            case BUTTON_BG:
                return 12;
            case BUTTON_WORKER:
                return 13;
            case CHEST_ICON:
                return 14;
            case GOLD_ICON:
                return 15;
            case MANA_ICON:
                return 16;
            case IRON_ICON:
                return 17;
            case WOOD_ICON:
                return 18;
            case MARKETPLACE:
                return 19;
            case ARCHER:
                return 20;
            case ARCHERBACK:
                return 21;
            case SPEARFIGHTER:
                return 22;
            case SPEARFIGHTERBACK:
                return 23;
            case SWORDFIGHTER:
                return 24;
            case SWORDFIGHTERBACK:
                return 25;
            case WORKER:
                return 26;
            case WORKERBACK:
                return 27;
            case HERO:
                return 28;
            case HEROBACK:
                return 29;
            case MENU_BG:
                return 30;
            case BUTTON_SPEARFIGHTER:
                return 31;
            case BUTTON_SWORDFIGHTER:
                return 32;
            case BUTTON_GREY:
                return 33;
            case BUTTON_GOLD:
                return 34;
            case BUTTON_BLUE:
                return 35;
            case TECHTREE:
                return 36;
            case TEAMBOX_OPEN:
                return 37;
            default:
                return 0;
        }
    }

    public static SpriteNames getValueForIndex(int index){
        switch (index){
            case 0:
                return NORMAL_FIELD;
            case 1:
                return IRON_FIELD;
            case 2:
                return FOREST;
            case 3:
                return MIRACLE;
            case 4:
                return BASE_UP_LEFT;
            case 5:
                return BASE_UP_RIGHT;
            case 6:
                return BASE_DOWN_LEFT_CASERNE;
            case 7:
                return BASE_DOWN_LEFT_EMPTY;
            case 8:
                return BASE_DOWN_RIGHT_CASERNE;
            case 9:
                return BASE_DOWN_RIGHT_EMPTY;
            case 10:
                return BASE_DOWN_RIGHT_FULL;
            case 11:
                return BASE_DOWN_RIGHT_LAB;
            case 12:
                return BUTTON_BG;
            case 13:
                return BUTTON_WORKER;
            case 14:
                return CHEST_ICON;
            case 15:
                return GOLD_ICON;
            case 16:
                return MANA_ICON;
            case 17:
                return IRON_ICON;
            case 18:
                return WOOD_ICON;
            case 19:
                return MARKETPLACE;
            case 20:
                return ARCHER;
            case 21:
                return ARCHERBACK;
            case 22:
                return SPEARFIGHTER;
            case 23:
                return SPEARFIGHTERBACK;
            case 24:
                return SWORDFIGHTER;
            case 25:
                return SWORDFIGHTERBACK;
            case 26:
                return WORKER;
            case 27:
                return WORKERBACK;
            case 28:
                return HERO;
            case 29:
                return HEROBACK;
            case 30:
                return MENU_BG;
            case 31:
                return BUTTON_SPEARFIGHTER;
            case 32:
                return BUTTON_SWORDFIGHTER;
            case 33:
                return BUTTON_GREY;
            case 34:
                return BUTTON_GOLD;
            case 35:
                return BUTTON_BLUE;
            case 36:
                return TECHTREE;
            case 37:
                return TEAMBOX_OPEN;
            default:
                return NORMAL_FIELD;
        }
    }

    public static int getSpriteAmount(){
        return 38;
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
