package foxidcw.android.idcw.common.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cjwsc.idcm.Utils.LogUtil;

import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.TimeUtils;
import com.cjwsc.idcm.base.BaseModel;
import com.cjwsc.idcm.base.BasePresenter;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.signarl.BaseSignalrActivity;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.List;

import foxidcw.android.idcw.common.R;
import foxidcw.android.idcw.common.utils.DialogVersionUtil;
import foxidcw.android.idcw.common.utils.ErrorCodeUtils;
import foxidcw.android.idcw.common.utils.IDCWPayJumpUtils;
import foxidcw.android.idcw.common.utils.StatusBarUtils;
import foxidcw.android.idcw.foxcommon.Constants.Constants;


/**
 * Created by admin-2 on 2018/3/14.
 */
public abstract class BaseWalletActivity<M extends BaseModel, P extends BasePresenter> extends BaseSignalrActivity<M, P> {

    //用来控制应用前后台切换的逻辑
    protected boolean isCurrentRunningForeground = true;

    /**
     * 记录处于前台的Activity
     */
    private static BaseWalletActivity mForegroundActivity = null;
    private String currentTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initTheme();
        ErrorCodeUtils.initErrorCode(this);
        super.onCreate(savedInstanceState);

    }

    protected boolean isCheckVersion() {
        return true;
    }


    protected void initTheme() {
        StatusBarUtils.with(this).init();
    }

    @Override
    protected void onResume() {
        initClassHeader();
        super.onResume();
    }

    private void initClassHeader() {
        ClassicsHeader.REFRESH_HEADER_PULLDOWN = getResources().getString(R.string.str_head_pulldown);//"下拉可以刷新";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = getResources().getString(R.string.str_header_refreshing);//"正在刷新...";
        ClassicsHeader.REFRESH_HEADER_RELEASE = getResources().getString(R.string.str_head_release);//"释放立即刷新";
        ClassicsHeader.REFRESH_HEADER_FINISH = getResources().getString(R.string.str_header_finish);//"刷新完成";
        ClassicsHeader.REFRESH_HEADER_FAILED = getResources().getString(R.string.str_header_failed);//"刷新失败";
        ClassicsFooter.REFRESH_FOOTER_ALLLOADED = getResources().getString(R.string.str_header_all_finish);//"全部加载完成";
        ClassicsFooter.REFRESH_FOOTER_LOADING = getResources().getString(R.string.footer_loading);//"正在加载...";
        ClassicsFooter.REFRESH_FOOTER_FINISH = getResources().getString(R.string.footer_finish);//"加载完成";
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        /**
         * 验证是否需要跳转
         */
        if (checkToPay() && IDCWPayJumpUtils.checkHasCacheParamAndLogined()) {
            IDCWPayJumpUtils.IDCWPayJumpWithType(this, IDCWPayJumpUtils.getCacheParamType(), true);
        }
    }

    /**
     * 校验当前是否需要跳转到pay界面
     *
     * @return
     */
    protected boolean checkToPay() {
        return true;
    }

    protected DialogVersionUtil dialogVersionUtil;

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e("activity==lifecycle", "=====>start");
        if (!isCurrentRunningForeground && isCheckVersion()) {
            if (!(this.getClass().getSimpleName().equals("GuideActivity"))) {
                if (dialogVersionUtil == null) dialogVersionUtil = new DialogVersionUtil();
                dialogVersionUtil.checkVersion(this);
            }
        }

        if (!isCurrentRunningForeground) {
            if (!(this.getClass().getSimpleName().equals("StartActivity"))) {
                LoginStatus loginStatus = (LoginStatus) ACacheUtil.get(this).getAsObject(AcacheKeys.loginbean);
                if (loginStatus != null) {
                    currentTime = TimeUtils.getTime();
                    if (!TextUtils.isEmpty(ACacheUtil.get(mCtx).getAsString(loginStatus.getId() + AcacheKeys.cache1))) {
                        int diff = (int) TimeUtils.getFromEndTime(ACacheUtil.get(mCtx).getAsString(loginStatus.getId() + AcacheKeys.cache1), currentTime);
                        LogUtil.e("diff==", diff + "");
                        if (diff > 600) {
                            LogUtil.e("diff==", diff + "");
                            if (!TextUtils.isEmpty(loginStatus.getTicket()) && ACacheUtil.get(mCtx).getAsBoolean(loginStatus.getDevice_id() + AcacheKeys.SAVEISVAILDPIN, false)) {
                                ARouter.getInstance().build(Constants.PIN_START)
                                        .withString("pinStart", "pinStart")
                                        .navigation(this);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e("activity==lifecycle", "=====>stop");
        isCurrentRunningForeground = isRunningForeground();
        if (!isCurrentRunningForeground) {
            LogUtil.e("=========onStop=", "=========onStop==========");
            LoginStatus loginStatus = (LoginStatus) ACacheUtil.get(this).getAsObject(AcacheKeys.loginbean);
            if (loginStatus != null) {
                currentTime = TimeUtils.getTime();//切换到后台的时候我获取当前时间
                ACacheUtil.get(mCtx).put(loginStatus.getId() + AcacheKeys.cache1, currentTime);
            }
            ACacheUtil.get(mCtx).put("pinPass", "pinPass");//指纹验证切换到后台的时候我保存一个标志
        }
    }

    public boolean isRunningForeground() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(this.getApplicationInfo().processName)) {
                    LogUtil.e("TAG", "EntryActivity isRunningForeGround");
                    return true;
                }
            }
        }
        LogUtil.e("TAG", "EntryActivity isRunningBackGround");
        return false;
    }

    /**
     * 界面跳转
     *
     * @param tarActivity
     */
    protected void intentFActivity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(this, tarActivity);
        startActivity(intent);
    }

    //点击空白区域隐藏键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideInput(v, ev)) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        clearCurrentFocus(v);
                    }
                }
                return super.dispatchTouchEvent(ev);
            }
            // 必不可少，否则所有的组件都不会有TouchEvent了
            if (getWindow().superDispatchTouchEvent(ev)) {
                return true;
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
        return onTouchEvent(ev);
    }

    protected void clearCurrentFocus(View view) {

    }


    public boolean isShouldHideInput(View v, MotionEvent event) {
        try {
            if (v != null && (v instanceof EditText)) {
                int[] leftTop = {0, 0};
                //获取输入框当前的location位置
                v.getLocationInWindow(leftTop);
                int left = leftTop[0];
                int top = leftTop[1];
                int bottom = top + v.getHeight();
                int right = left + v.getWidth();
                if (event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom) {
                    // 点击的是输入框区域，保留点击EditText的事件
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogVersionUtil != null) dialogVersionUtil.clearDialog();
    }

    protected void checkAppVersion() {
    }
}
