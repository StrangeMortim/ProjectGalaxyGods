package GameObject;

/**
 * Contains all Information about the Paths for the different assets
 *
 * @author Fabi
 */
public enum  SpriteNames {
    NORMAL_FIELD,NORMAL_FIELD_2,NORMAL_FIELD_3, IRON_FIELD_2, IRON_FIELD,FOREST,MINE, MIRACLE,
    BASE_UP_LEFT,BASE_UP_RIGHT,BASE_DOWN_LEFT_CASERNE,BASE_DOWN_LEFT_EMPTY,BASE_DOWN_RIGHT_EMPTY,BASE_DOWN_RIGHT_CASERNE,BASE_DOWN_RIGHT_LAB,BASE_DOWN_RIGHT_FULL,
    CHEST_ICON, GOLD_ICON, MANA_ICON, IRON_ICON, WOOD_ICON,
    BUTTON_BG, BUTTON_WORKER, MENU_BG, BUTTON_SPEARFIGHTER, BUTTON_SWORDFIGHTER,BUTTON_GREY,BUTTON_GOLD,BUTTON_BLUE,BUTTON_ARCHER,
    MARKETPLACE, TECHTREE,TEAMBOX_OPEN,
    ARCHER,ARCHERBACK, SPEARFIGHTER,SPEARFIGHTERBACK, SWORDFIGHTER,SWORDFIGHTERBACK, WORKER,WORKERBACK, HERO,HEROBACK;

    /**
     * @return the paths to the corresponding assets, can directly be used for Gdx.files.internal()
     */
    public String getSpriteName(){
        String folder = "assets/sprites/";
        switch (this){
            case NORMAL_FIELD:
                return folder+"normal0.png";
            case NORMAL_FIELD_2:
                return folder+"normal1.png";
            case NORMAL_FIELD_3:
                return folder+"normal2.png";
            case IRON_FIELD:
                return folder+"ironNoMine0.png";
            case IRON_FIELD_2:
                return folder+"ironNoMine1.png";
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
            case BUTTON_ARCHER:
                return folder+"buttonArcher.png";
            case MINE:
                return folder+"mine.png";
            default:
                return "";
        }
    }

    /**
     * @return the Index for the prepared Texture in GameScreen
     */
    public int getSpriteIndex(){
        switch (this){
            case NORMAL_FIELD:
                return 0;
            case NORMAL_FIELD_2:
                return 1;
            case NORMAL_FIELD_3:
                return 2;
            case IRON_FIELD:
                return 3;
            case IRON_FIELD_2:
                return 4;
            case FOREST:
                return 5;
            case MIRACLE:
                return 6;
            case BASE_UP_LEFT:
                return 7;
            case BASE_UP_RIGHT:
                return 8;
            case BASE_DOWN_LEFT_CASERNE:
                return 9;
            case BASE_DOWN_LEFT_EMPTY:
                return 10;
            case BASE_DOWN_RIGHT_CASERNE:
                return 11;
            case BASE_DOWN_RIGHT_EMPTY:
                return 12;
            case BASE_DOWN_RIGHT_FULL:
                return 13;
            case BASE_DOWN_RIGHT_LAB:
                return 14;
            case BUTTON_BG:
                return 15;
            case BUTTON_WORKER:
                return 16;
            case CHEST_ICON:
                return 17;
            case GOLD_ICON:
                return 18;
            case MANA_ICON:
                return 19;
            case IRON_ICON:
                return 20;
            case WOOD_ICON:
                return 21;
            case MARKETPLACE:
                return 22;
            case ARCHER:
                return 23;
            case ARCHERBACK:
                return 24;
            case SPEARFIGHTER:
                return 25;
            case SPEARFIGHTERBACK:
                return 26;
            case SWORDFIGHTER:
                return 27;
            case SWORDFIGHTERBACK:
                return 28;
            case WORKER:
                return 29;
            case WORKERBACK:
                return 30;
            case HERO:
                return 31;
            case HEROBACK:
                return 32;
            case MENU_BG:
                return 33;
            case BUTTON_SPEARFIGHTER:
                return 34;
            case BUTTON_SWORDFIGHTER:
                return 35;
            case BUTTON_GREY:
                return 36;
            case BUTTON_GOLD:
                return 37;
            case BUTTON_BLUE:
                return 38;
            case TECHTREE:
                return 39;
            case TEAMBOX_OPEN:
                return 40;
            case BUTTON_ARCHER:
                return 41;
            case MINE:
                return 42;
            default:
                return 0;
        }
    }

    /**
     * Reverse function for getSpriteIndex
     * @param index for which value the index is searched
     * @return the corresponding enum Value
     */
    public static SpriteNames getValueForIndex(int index){
        switch (index){
            case 0:
                return NORMAL_FIELD;
            case 1:
                return NORMAL_FIELD_2;
            case 2:
                return NORMAL_FIELD_3;
            case 3:
                return IRON_FIELD;
            case 4:
                return IRON_FIELD_2;
            case 5:
                return FOREST;
            case 6:
                return MIRACLE;
            case 7:
                return BASE_UP_LEFT;
            case 8:
                return BASE_UP_RIGHT;
            case 9:
                return BASE_DOWN_LEFT_CASERNE;
            case 10:
                return BASE_DOWN_LEFT_EMPTY;
            case 11:
                return BASE_DOWN_RIGHT_CASERNE;
            case 12:
                return BASE_DOWN_RIGHT_EMPTY;
            case 13:
                return BASE_DOWN_RIGHT_FULL;
            case 14:
                return BASE_DOWN_RIGHT_LAB;
            case 15:
                return BUTTON_BG;
            case 16:
                return BUTTON_WORKER;
            case 17:
                return CHEST_ICON;
            case 18:
                return GOLD_ICON;
            case 19:
                return MANA_ICON;
            case 20:
                return IRON_ICON;
            case 21:
                return WOOD_ICON;
            case 22:
                return MARKETPLACE;
            case 23:
                return ARCHER;
            case 24:
                return ARCHERBACK;
            case 25:
                return SPEARFIGHTER;
            case 26:
                return SPEARFIGHTERBACK;
            case 27:
                return SWORDFIGHTER;
            case 28:
                return SWORDFIGHTERBACK;
            case 29:
                return WORKER;
            case 30:
                return WORKERBACK;
            case 31:
                return HERO;
            case 32:
                return HEROBACK;
            case 33:
                return MENU_BG;
            case 34:
                return BUTTON_SPEARFIGHTER;
            case 35:
                return BUTTON_SWORDFIGHTER;
            case 36:
                return BUTTON_GREY;
            case 37:
                return BUTTON_GOLD;
            case 38:
                return BUTTON_BLUE;
            case 39:
                return TECHTREE;
            case 40:
                return TEAMBOX_OPEN;
            case 41:
                return BUTTON_ARCHER;
            case 42:
                return MINE;
            default:
                return NORMAL_FIELD;
        }
    }

    /**
     * @return how many sprites there are currently
     */
    public static int getSpriteAmount(){
        return 43;
    }


    /**
     * Deprecated
     * returned a specific assets paths, for the assets that have variations(normal fields and mine fields)
     * @param spec which number of the variation
     * @return   the asset path
     */
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
