package cn.com.epsoft.keyboard.iprovider.impl;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.com.epsoft.keyboard.basic.PinDialogManager;
import foxidcw.android.idcw.foxcommon.ProviderPath;
import foxidcw.android.idcw.foxcommon.provider.services.PinDialogProviderServices;

@Route(path = ProviderPath.path_PinDialogProviderServices, name = "暴露pin弹框服务")
public class PinDialogProviderImpl implements PinDialogProviderServices {



    @Override
    public void showPinDialog(Context context,PinResultStr str) {
        PinDialogManager.getInstance(context).showPinDialogForProvider(str);
    }
    @Override
    public void dissPinDialog() {
    }
    @Override
    public void init(Context context) {

    }
}
