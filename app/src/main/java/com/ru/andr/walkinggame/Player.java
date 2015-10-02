package com.ru.andr.walkinggame;

//TODO: save player info online

import android.content.Context;
import android.content.SharedPreferences;

public class Player {
    //region valueKeys
    private static final String strKey = "mySTR";
    private static final String spdKey = "mySPD";
    private static final String expKey = "myEXP";
    private static final String nextExpKey = "myExpToLevel";
    private static final String levelKey = "myLevel";
    private static final String timesCanLeveKey = "myTimesCanLevel";
    private static final String PREFS_FILE = "myPrefs";
    //endregion

    private int mEXP;
    private int level;
    private int STR;
    private int SPD;

    private Context mContext;

    private float toNextLevel;
    private int canLevelUpTimes;

    private Player(Context c){
        mContext = c;
    }
    // region Getters
    public int getCanLevelUpTimes(){
        return canLevelUpTimes;
    }

    public int getStrength(){
        return STR;
    }

    public int getSpeed(){
        return SPD;
    }

    public int getEXP(){
        return mEXP;
    }

    public int getLevel(){
        return level;
    }

    public int getExpToNextLevel(){
        return (int)toNextLevel;
    }
    // endregion

    //region incrementers
    public void incStrength(){
        if(canLevelUpTimes > 0){
            STR++;
            canLevelUpTimes--;
        }
        Save();
    }

    public void incSpeed(){
        if(canLevelUpTimes > 0){
            SPD++;
            canLevelUpTimes--;
        }
        Save();
    }

    //endregion
    public void addEXP(int exp){
        mEXP += exp;
        if (mEXP >= toNextLevel){
            levelUp();
        }
        Save();
    }

    private void levelUp(){
        mEXP = 0;
        canLevelUpTimes++;
        level++;
        toNextLevel = toNextLevel * 1.2f;
        Save();
    }

    public void Save(){
        SharedPreferences.Editor sp = mContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE).edit();
        sp.putInt(levelKey, level);
        sp.putInt(strKey, STR);
        sp.putInt(spdKey, SPD);
        sp.putInt(expKey, mEXP);
        sp.putInt(timesCanLeveKey, canLevelUpTimes);
        sp.putFloat(nextExpKey, toNextLevel);
        sp.apply();
    }

    public static Player getPlayer(Context c){
        Player p = new Player(c);
        SharedPreferences sp = c.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        // TODO: set all default to 1 when development is finished
        p.STR               = sp.getInt(strKey, 3);
        p.SPD               = sp.getInt(spdKey, 2);
        p.canLevelUpTimes   = sp.getInt(timesCanLeveKey, 0);
        p.level             = sp.getInt(levelKey, 1);
        p.mEXP              = sp.getInt(expKey, 0);
        p.toNextLevel       = sp.getFloat(nextExpKey, 10);
        return p;
    }
}
