package foxidcw.android.idcw.common.utils;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import foxidcw.android.idcw.common.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.iprovider.CheckVersionProviderServices;
import foxidcw.android.idcw.common.model.bean.CheckAppVersionBean;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * Created by admin-2 on 2018/4/15.
 */

public class DialogVersionUtil {

    private static DialogVersionUtil instance;
    private Dialog dialogActivity;

    @Autowired
    CheckVersionProviderServices mCheckVersionServices = ARouter.getInstance().navigation(CheckVersionProviderServices.class);

    private double local;
    private double server;

    public DialogVersionUtil() {
        ARouter.getInstance().inject(this);
    }


    public void checkVersion(BaseWalletActivity mActivity) {
        //登录页面不需要弹检测版本更新
        mActivity.addSubscribe(mCheckVersionServices.CheckVersionProvider().subscribeWith(new RxSubscriber<CheckAppVersionBean>() {
            @Override
            public void onSuccess(CheckAppVersionBean checkAppVersionBean) {
                if (checkAppVersionBean.getLatestVersion() == null) return;
                if (StringUtils.getVersionName(mActivity).contains(".")) {
                    String str = StringUtils.getVersionName(mActivity).replace(".", "");
                    StringBuilder stringBuilder = new StringBuilder(str);
                    String localStr = stringBuilder.insert(1, ".").toString();
                    local = Double.parseDouble(localStr);
                }
                if (checkAppVersionBean.getLatestVersion().contains(".")) {
                    String str = checkAppVersionBean.getLatestVersion().replace(".", "");
                    StringBuilder stringBuilder = new StringBuilder(str);
                    String serverStr = stringBuilder.insert(1, ".").toString();
                    server = Double.parseDouble(serverStr);
                }

                if (local < server && checkAppVersionBean.isIsShowUpdate()) {
                    createDialog(mActivity, checkAppVersionBean);
                }
            }

            @Override
            protected void onError(ResponseThrowable ex) {

            }
        }));
    }


    private void createDialog(BaseWalletActivity mActivity, CheckAppVersionBean checkAppVersionBean) {
        if (dialogActivity != null && dialogActivity.isShowing()) return;
        dialogActivity = new Dialog(mActivity, R.style.shuweiDialog);
        dialogActivity.setCancelable(false);
        View inflaterExit = LayoutInflater.from(mActivity).inflate(R.layout.activity_base_check_dialog, null);
        dialogActivity.setContentView(inflaterExit);
        TextView tv_activity_cancel = (TextView) inflaterExit.findViewById(R.id.tv_activity_cancel);
        TextView tv_activity_update = (TextView) inflaterExit.findViewById(R.id.tv_activity_update);
        if (checkAppVersionBean.isIs_enabled()){
            tv_activity_cancel.setVisibility(View.GONE);
        }else {
            tv_activity_cancel.setVisibility(View.VISIBLE);
        }
        tv_activity_cancel.setOnClickListener(v -> {
            if (dialogActivity != null && dialogActivity.isShowing()) {
                dialogActivity.dismiss();
                dialogActivity = null;
            }
        });
        tv_activity_update.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            if (TextUtils.isEmpty(checkAppVersionBean.getUpdateUrl())) return;
            Uri content_url = Uri.parse(checkAppVersionBean.getUpdateUrl());
            intent.setData(content_url);
            startActivity(intent);
            if (dialogActivity != null && dialogActivity.isShowing()) {
                dialogActivity.dismiss();
                dialogActivity = null;
            }
        });
        Window dialogWindow = dialogActivity.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setAttributes(lp);
        if (mActivity != null) dialogActivity.show();
    }

    public void clearDialog() {
        if (dialogActivity != null) {
            dialogActivity.dismiss();
            dialogActivity = null;
        }
    }
}
