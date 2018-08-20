package cn.com.epsoft.keyboard.basic;
import android.content.Context;

import foxidcw.android.idcw.foxcommon.provider.services.PinDialogProviderServices;

public class PinDialogManager  {

    private static PinDialogManager mInstance;
    private static NewPinDialog mNewPinDialog;
    private static Context  mContext;

    private PinDialogManager() {

    }

    public static PinDialogManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (PinDialogManager.class) {
                if (mInstance == null) {
                    mInstance = new PinDialogManager();
                }
            }
        }
        mContext=context;//不同页面不同上下文
        return mInstance;//同一个单例对象
    }

//    public void shouPinDialog() {
//        if (mNewPinDialog == null){
//            mNewPinDialog = new NewPinDialog(mContext);
//            mNewPinDialog.show();
//        }
//    }
//
    public void dismissPinDialog() {
        if (mNewPinDialog != null) {
            mNewPinDialog.dismiss();
            mNewPinDialog=null;
        }
    }



    public void showPinDialogForProvider(PinDialogProviderServices.PinResultStr result) {
      //  dismissPinDialog();//不管如何先关闭之前的对话框
        mNewPinDialog = new NewPinDialog(mContext);
         mNewPinDialog.showForProvider(result);
    }


}
