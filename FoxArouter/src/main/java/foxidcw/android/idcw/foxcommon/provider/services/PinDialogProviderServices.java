package foxidcw.android.idcw.foxcommon.provider.services;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface PinDialogProviderServices extends IProvider{
    public void showPinDialog(Context context,PinResultStr str);
    public void dissPinDialog();

    public interface PinResultStr{

        void onPinResultStr(String result);

    }

}
