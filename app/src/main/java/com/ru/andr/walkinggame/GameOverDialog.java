package com.ru.andr.walkinggame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


/**
 * Created by Dagur on 29/10/2015.
 */
public class GameOverDialog extends DialogFragment {
    private String msg;

    public GameOverDialog(String msg){
        this.msg = msg;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getActivity(), StepCount.class);
                startActivity(i);
            }
        });
        return builder.create();
    }
}
