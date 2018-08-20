package com.idcg.idcw.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.idcg.idcw.R;
import com.idcg.idcw.adapter.LanguageSeletorAdapter;
import com.idcg.idcw.app.WalletApplication;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cjwsc.idcm.language.LanguageSettingBean;
import com.cjwsc.idcm.language.LanguageUtil;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.base.AppManager;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.Locale;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/17 16:46
 **/
@Route(path = ArouterConstants.LANGUAGE_AC, name = "语言界面")
public class LanguageActivity extends BaseWalletActivity {

    @BindView(R.id.mr_back_layout)
    LinearLayout imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.rv_language_list)
    RecyclerView mLanguageSelet;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartRefreshLayout;
    private LanguageSeletorAdapter mLanguageSeletorAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_language;
    }

    @Override
    protected void onInitView(Bundle bundle) {

        tvSetName.setText(R.string.title_language);
        smartRefreshLayout.setEnableLoadmore(false);
        smartRefreshLayout.setEnableRefresh(false);
        mLanguageSeletorAdapter = new LanguageSeletorAdapter(LanguageUtil.mArrayList);
        mLanguageSelet.setLayoutManager(new LinearLayoutManager(this));
        mLanguageSelet.setAdapter(mLanguageSeletorAdapter);
        LanguageSettingBean languageSettingBean = (LanguageSettingBean) ACacheUtil.get(this).getAsObject(AcacheKeys.LANGUAGELOCALE);
        if (languageSettingBean != null) {
            Locale locale = languageSettingBean.getLocale();
            int position = LanguageUtil.getPositionForLocale(locale);
            if (mCtx.getResources().getConfiguration().locale.toString().equals("zh_TW") ||
                    mCtx.getResources().getConfiguration().locale.toString().equals("zh_MO_#Hant") ||
                    mCtx.getResources().getConfiguration().locale.toString().equals("zh_HK_#Hant") ||
                    mCtx.getResources().getConfiguration().locale.toString().equals("zh_TW_#Hant") ||
                    mCtx.getResources().getConfiguration().locale.toString().equals("zh_HK") ||
                    mCtx.getResources().getConfiguration().locale.toString().equals("zh_MO")) {//zh_TW_#Hant
                mLanguageSeletorAdapter.setCurrentPosition(2);
                return;
            }
            mLanguageSeletorAdapter.setCurrentPosition(position);
        }

    }

    @Override
    protected void onEvent() {
        mLanguageSelet.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                mLanguageSeletorAdapter.setCurrentPosition(position);
                LanguageSettingBean languageSettingBean = LanguageUtil.mArrayList.get(position);
                if (languageSettingBean != null) {
                    ACacheUtil.get(WalletApplication.getInstance()).put(AcacheKeys.LANGUAGELOCALE,
                            new LanguageSettingBean(languageSettingBean.getName(), languageSettingBean.getLocale()));
                }
                //设置应用语言类型
                LanguageUtil.initAppLanguage(WalletApplication.getContext());
                AppManager.getInstance().finishAllActivity();
                Intent intent = new Intent(LanguageActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("language", "language");
                intent.putExtra("Language", bundle);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.mr_back_layout, R.id.tv_set_Name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                this.finish();
                break;
            case R.id.tv_set_Name:
                break;
        }
    }

    @Override
    protected void checkAppVersion() {
    }

    @Override
    protected BaseView getView() {
        return null;
    }
}
