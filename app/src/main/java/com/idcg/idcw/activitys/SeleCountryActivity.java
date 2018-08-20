package com.idcg.idcw.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;

import foxidcw.android.idcw.common.utils.AppLanguageUtils;
import foxidcw.android.idcw.common.utils.SpUtils;

import com.idcg.idcw.widget.SearchEditText;
import com.cjwsc.idcm.Utils.AssetsUtils;
import com.cjwsc.idcm.Utils.SystemBarTintManager;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.callback.GetAssetsCallBack;
import com.cjwsc.idcm.model.bean.CountryCodeBean;
import com.cjwsc.idcm.recylcerview.BaseRecItemClickedAdapter;
import com.idcg.idcw.adapter.CountryCodeAdapter;
import com.cjwsc.idcm.widget.quickSelect.IndexBar;
import com.cjwsc.idcm.widget.quickSelect.suspension.SuspensionDecoration;
import com.google.gson.Gson;


import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import static com.cjwsc.idcm.Utils.ThemeUtils.transparencyBar;


/**
 * Created by hpz on 2018/1/4.
 */

@Route(path = ArouterConstants.SELECOUNTRY,name = "国家选择")
public class SeleCountryActivity extends BaseWalletActivity {
    private RecyclerView mRv;
    private LinearLayoutManager mManager;
    private TextView mTvSideBarHint;
    private IndexBar mIndexBar;
    private CountryCodeAdapter mCountryCodeAdapter;
    private SuspensionDecoration mDecoration;
    private TextView tvSetName;
    private LinearLayout imgBack;
    private SearchEditText query;
    private String mLanguagePosition;
    private String mLanguageCode;

    private List<CountryCodeBean.DataBeanX.DataBean> mArrayList;
    private List<CountryCodeBean.DataBeanX.DataBean> mClearList = new ArrayList<>();
    private List<String> dataStringList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_code;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//当背景为白色的时候，顶部状态栏的字体显示为黑体
//                this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            }
            setStatusBarColor(SeleCountryActivity.this, R.color.color_title);
            StatusBarLightMode(SeleCountryActivity.this, 3);
            //tvSetName = (TextView) findViewById(R.id.tv_set_Name);
            query = (SearchEditText) findViewById(R.id.query);
            query.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    search();
                }
            });
            //tvSetName.setText(R.string.title_select_country);
            imgBack = (LinearLayout) findViewById(R.id.mr_back_layout);
            imgBack.setOnClickListener(v -> {
                SeleCountryActivity.this.finish();
            });
            mRv = (RecyclerView) findViewById(R.id.rv);
            mRv.setLayoutManager(mManager = new LinearLayoutManager(this));
            mArrayList = new ArrayList<>();
            //使用indexBar
            mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);//HintTextView
            mIndexBar = (IndexBar) findViewById(R.id.indexBar);//IndexBar
            mCountryCodeAdapter = new CountryCodeAdapter(SeleCountryActivity.this, mArrayList, R.layout.item_country_code);
            mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                    .setNeedRealIndex(true)//设置需要真实的索引
                    .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    public static void setStatusBarColor(Activity activity, int colorId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
//      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
            transparencyBar(activity);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(colorId);
        }
    }

    public static void StatusBarLightMode(Activity activity, int type) {
        if (type == 1) {
            // MIUISetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 2) {
            //FlymeSetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    @Override
    protected void onEvent() {
        LogUtil.e("onEvent==>", AppLanguageUtils.getLanguageLocalCode(mCtx));
        String locale = Locale.getDefault().getLanguage();
        if (locale.equals("zh")) {
            mLanguagePosition = SpUtils.getStringData("language_position", "1");
        }else {
            mLanguagePosition = SpUtils.getStringData("language_position", "0");
        }
        int layoutPosition = Integer.parseInt(mLanguagePosition);
        if (AppLanguageUtils.getLanguageLocalCode(mCtx).equals("0")) {
            mLanguageCode = "en";

        } else if (AppLanguageUtils.getLanguageLocalCode(mCtx).equals("1")) {

            mLanguageCode = "cn";

        } else if (AppLanguageUtils.getLanguageLocalCode(mCtx).equals("8")) {
            mLanguageCode = "fi";

        } else if (AppLanguageUtils.getLanguageLocalCode(mCtx).equals("3")) {
            mLanguageCode = "nl";

        } else if (AppLanguageUtils.getLanguageLocalCode(mCtx).equals("4")) {
            mLanguageCode = "ko";//荷兰

        } else if (AppLanguageUtils.getLanguageLocalCode(mCtx).equals("5")) {
            mLanguageCode = "fr";

        } else if (AppLanguageUtils.getLanguageLocalCode(mCtx).equals("6")) {
            mLanguageCode = "vi";
        } else if (AppLanguageUtils.getLanguageLocalCode(mCtx).equals("7")) {
            mLanguageCode = "es";
        } else if (AppLanguageUtils.getLanguageLocalCode(mCtx).equals("2")) {
            mLanguageCode = "ja";
        }

        AssetsUtils.fromAssestJson(this, "country_" + mLanguageCode + ".json", new GetAssetsCallBack() {
            @Override
            public void success(String result) {
                Gson gson = new Gson();
                CountryCodeBean countryCodeBean = gson.fromJson(result, CountryCodeBean.class);
                Observable.just(countryCodeBean.getData())
                        .map(new Function<List<CountryCodeBean.DataBeanX>, List<CountryCodeBean.DataBeanX.DataBean>>() {
                            @Override
                            public List<CountryCodeBean.DataBeanX.DataBean> apply(List<CountryCodeBean.DataBeanX> dataBeanXES) throws Exception {
                                List<CountryCodeBean.DataBeanX.DataBean> dataBeanList = new ArrayList<>();
                                CountryCodeBean.DataBeanX dataBeanX = dataBeanXES.get(0);
                                //设置热门
                                for (int i = 0; i < dataBeanX.getData().size(); i++) {
                                    CountryCodeBean.DataBeanX.DataBean dataBean = dataBeanX.getData().get(i);
                                    dataBean.setTop(true);
                                    dataBean.setBaseIndexTag("☆");//dataBeanX.getKey()
                                    dataBeanList.add(dataBean);
                                }


                                for (int i  = 1;i<dataBeanXES.size();i++){
                                    CountryCodeBean.DataBeanX dataBean1 = dataBeanXES.get(i);
                                    String keyCode = dataBean1.getKey();
                                    for (int j = 0; j < dataBean1.getData().size(); j++) {
                                        dataBean1.getData().get(j).setBaseIndexTag(dataBean1.getKey());

                                    }
                                    dataStringList.add(keyCode);
                                    LogUtil.e("dataStringList>",new Gson().toJson(dataStringList));
                                }


                                for (int i = 1; i < dataBeanXES.size(); i++) {
                                    dataBeanList.addAll(dataBeanXES.get(i).getData());
                                }
                                return dataBeanList;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<CountryCodeBean.DataBeanX.DataBean>>() {
                            @Override
                            public void accept(List<CountryCodeBean.DataBeanX.DataBean> dataBeans) throws Exception {
                                mArrayList.addAll(dataBeans);
                                mClearList.addAll(dataBeans);
                                mCountryCodeAdapter = new CountryCodeAdapter(SeleCountryActivity.this, mArrayList, R.layout.item_country_code);
                                mRv.setAdapter(mCountryCodeAdapter);
                                mCountryCodeAdapter.notifyDataSetChanged();
                                mRv.addItemDecoration(mDecoration = new SuspensionDecoration(mCtx, mArrayList));
                                mIndexBar.setSourceDatasAlreadySorted(true)
                                        .setmSourceDatas(mArrayList)
                                        .invalidate();
                                mDecoration.setmDatas(mArrayList);


//                                mIndexBar.setmIndexDatas(dataStringList);
//                                mIndexBar.requestLayout();

                                mCountryCodeAdapter.setOnItemClickListener(new BaseRecItemClickedAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View itemView, int layoutPosition) {
                                        EventBus.getDefault().post(mArrayList.get(layoutPosition));
                                        finish();
                                    }
                                });
                            }
                        });
            }

            @Override
            public void error(String error) {

            }
        });

    }

    private void search() {
        String data = query.getText().toString().toLowerCase().trim();
        mArrayList.clear();
        for (int i = 0; i < mClearList.size(); i++) {
            CountryCodeBean.DataBeanX.DataBean workerInfo = mClearList.get(i);
            if (workerInfo.getAreacode().contains(data) || workerInfo.getName().toLowerCase().contains(data)) {
                mArrayList.add(workerInfo);
            }
        }
        mCountryCodeAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    protected BaseView getView() {
        return null;
    }
    @Override
    protected boolean checkToPay() {
        return false;
    }

    @Override
    protected void checkAppVersion() {
        //DialogVersionUtil.getInstance(this).checkVersion();
    }


}
