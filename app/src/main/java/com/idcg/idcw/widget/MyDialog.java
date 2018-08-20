package com.idcg.idcw.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by hpz on 2018/2/6.
 */

public class MyDialog extends Dialog {
    public MyDialog(@NonNull Context context) {
        super(context);
    }

    public MyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MyDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void dissmissDialog(DissMissCustomerDialog action){

        if(this.isShowing()){
            dismiss();
            action.dissMissAction();

        }


    }


    public interface DissMissCustomerDialog{

        public void dissMissAction();
    }
    //end

}
