package com.ru.andr.walkinggame;

//TODO: save player info online

import android.content.Context;
import android.content.Intent;

public class Player {
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

    public void addEXP(int exp){
        mEXP += exp;
        if (mEXP >= toNextLevel){
            levelUp();
        }
    }

    private void levelUp(){
        mEXP = 0;
        //level++;
        canLevelUpTimes++;
        toNextLevel = toNextLevel * 1.2f;
    }
}
