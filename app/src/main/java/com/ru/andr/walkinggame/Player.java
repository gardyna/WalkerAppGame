package com.ru.andr.walkinggame;

//TODO: save player info online

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;

public class Player {
    private String name;
    private boolean status;

    private int mEXP;
    private int level;
    private int STR;
    private int SPD;
    private int INT;
    private int AGL;


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

    public int getAgility(){
        return AGL;
    }

    public int getInteligence(){
        return INT;
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

    public void incIntelligence(){
        if(canLevelUpTimes > 0){
            INT++;
            canLevelUpTimes--;
        }
        Save();
    }

    public void incAgility(){
        if(canLevelUpTimes > 0){
            AGL++;
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
        SharedPreferences.Editor sp = mContext.getSharedPreferences(Constants.PREFS_FILE, Context.MODE_PRIVATE).edit();
        // leveling info
        sp.putInt(Constants.expKey, mEXP);
        sp.putInt(Constants.timesCanLeveKey, canLevelUpTimes);
        sp.putFloat(Constants.nextExpKey, toNextLevel);
        sp.putInt(Constants.levelKey, level);
        // stats
        sp.putInt(Constants.strKey, STR);
        sp.putInt(Constants.intKey, SPD);
        sp.putInt(Constants.intKey, INT);
        sp.putInt(Constants.aglKey, AGL);
        // save
        sp.apply();
    }

    public static Player getPlayer(Context c){
        Player p = new Player(c);
        SharedPreferences sp = c.getSharedPreferences(Constants.PREFS_FILE, Context.MODE_PRIVATE);
        AccountManager manager = (AccountManager) c.getSystemService(c.ACCOUNT_SERVICE);
        for (Account a: manager.getAccounts()) {
            if (a.type.equalsIgnoreCase("com.google")){
                p.name = a.name;
                break;
            }
        }
        if (p.name == null){
            p.name = "Joe/Jane";
        }

        // TODO: set all default to 1 when development is finished
        // stats
        p.STR               = sp.getInt(Constants.strKey, 3);
        p.SPD               = sp.getInt(Constants.spdKey, 2);
        p.INT               = sp.getInt(Constants.intKey, 3);
        p.AGL               = sp.getInt(Constants.aglKey, 1);
        // leveling info
        p.canLevelUpTimes   = sp.getInt(Constants.timesCanLeveKey, 0);
        p.level             = sp.getInt(Constants.levelKey, 1);
        p.mEXP              = sp.getInt(Constants.expKey, 0);
        p.toNextLevel       = sp.getFloat(Constants.nextExpKey, 10);

        return p;
    }

    public Player(String name, boolean stat){
        this.name = name;
        this.status = stat;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isStatusOnline() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
}
