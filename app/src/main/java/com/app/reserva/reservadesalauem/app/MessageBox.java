package com.app.reserva.reservadesalauem.app;

import android.app.AlertDialog;
import android.content.Context;

import com.app.reserva.reservadesalauem.R;

/**
 * Created by Mamoru on 12/01/2016.
 */
public class MessageBox {
    // mensagens

    public static void showInfo(Context ctx, String title, String msg){
        show(ctx,title,msg,android.R.drawable.ic_dialog_info);
    }

    public static void showAlert(Context ctx, String title, String msg){
        show(ctx,title,msg,android.R.drawable.ic_dialog_alert);
    }

    public static void show(Context ctx, String title, String msg){
        show(ctx,title,msg,0);
    }

    public static void show(Context ctx, String title, String msg, int idIcon){
        AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
        dlg.setIcon(idIcon);
        dlg.setTitle(title);
        dlg.setMessage(msg);
        dlg.setNeutralButton(ctx.getString(R.string.msg_ok), null);
        dlg.show();
    }
}
