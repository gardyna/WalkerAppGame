package com.ru.andr.walkinggame;

//TODO: save player info online

public class Player {
    private int mEXP;
    private int STR;
    private int level;

    private float toNextLevel;

    public Player(){
        mEXP = 0;
        STR = 0;
        toNextLevel = 10;
        level = 1;
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

    public void addEXP(int exp){
        mEXP += exp;
        if (mEXP >= toNextLevel){
            level++;
            mEXP = 0;
            toNextLevel = toNextLevel * 1.2f;
        }
    }
}
