package com.ru.andr.walkinggame;

//TODO: save player info online

import android.content.Context;
import android.content.SharedPreferences;

public class Player {
    //region valueKeys
    private static final String strKey = "mySTR";
    private static final String expKey = "myEXP";
    private static final String nextExpKey = "myExpToLevel";
    private static final String levelKey = "myLevel";
    private static final String timesCanLeveKey = "myTimesCanLevel";
    private static final String PREFS_FILE = "myPrefs";
    //endregion

    private int mEXP;
    private int level;
    private int STR;

    private Context mContext;

    private float toNextLevel;
    private int canLevelUpTimes;

    public Player(Context c){
        // save and read values from permanent memory
        canLevelUpTimes = 0;
        mContext = c;
        mEXP = 0;
        toNextLevel = 10;
        STR = 1;
        level = 1;
    }
    // region Getters
    public int getCanLevelUpTimes(){
        return canLevelUpTimes;
    }

    public int getSTR(){
        return STR;
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
    public void incSTR(){
        if(canLevelUpTimes > 0){
            STR++;
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
        toNextLevel = toNextLevel * 1.2f;
        Save();
    }

    public void Save(){
        SharedPreferences.Editor sp = mContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE).edit();
        sp.putInt(levelKey, level);
        sp.putInt(strKey, STR);
        sp.putInt(expKey, mEXP);
        sp.putInt(timesCanLeveKey, canLevelUpTimes);
        sp.putFloat(nextExpKey, toNextLevel);
        sp.apply();
    }

    public static Player getPlayer(Context c){
        Player p = new Player(c);
        SharedPreferences sp = c.getSharedPreferences(PREFS_FILE, c.MODE_PRIVATE);
        p.STR               = sp.getInt(strKey, 1);
        p.canLevelUpTimes   = sp.getInt(timesCanLeveKey, 0);
        p.level             = sp.getInt(levelKey, 1);
        p.mEXP              = sp.getInt(expKey, 0);
        p.toNextLevel       = sp.getFloat(nextExpKey, 10);
        return p;
    }
}
