package com.idcg.idcw.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.model.logic.FindSureMoneyLogic;
import com.idcg.idcw.model.params.CheckUserReqParam;
import com.idcg.idcw.presenter.impl.FindSureMoneyImpl;
import com.idcg.idcw.presenter.interfaces.FindSureMoneyContract;
import com.idcg.idcw.utils.SoftHideKeyBoardUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.api.NetWorkApi;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.base.ui.view.countdownview.Utils;
import com.cjwsc.idcm.constant.AcacheKeys;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by hpz on 2018/7/10.
 */

@Route(path = ArouterConstants.RecoverAsset, name = "找回资产新页面")
public class RecoverAssetActivity extends BaseWalletActivity<FindSureMoneyLogic, FindSureMoneyImpl> implements FindSureMoneyContract.View {
    @BindView(R.id.mr_back_layout)
    LinearLayout mrBackLayout;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.rv_recover_asset)
    RecyclerView rvRecoverAsset;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.btn_find_next_step)
    TextView btnFindNextStep;
    @BindView(R.id.ll_assets)
    LinearLayout ll_assets;
    @BindView(R.id.img_no_data)
    ImageView img_no_data;
    @BindView(R.id.ll_assets_parent)
    LinearLayout ll_assets_parent;


    private BaseQuickAdapter<CheckUserReqParam, BaseViewHolder> recoverCommonAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recover_asset;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initRecoverRecycler();

        btnFindNextStep.setEnabled(false);//初始化按钮不能点击

        for (int i = 0; i < 12; i++) {//初始化默认设置12个空白的助记词位置
            CheckUserReqParam recoverBean = new CheckUserReqParam();
            recoverBean.setPhrase("");
            recoverBean.setSerial_number(i + 1);
            recoverCommonAdapter.addData(recoverBean);
        }

        keepWalletBtnNotOver(ll_assets_parent, ll_assets);
    }

    private int scrollToPosition = 0;

    /**
     * 保持按钮始终不会被覆盖
     *
     * @param root
     * @param subView
     */
    private void keepWalletBtnNotOver(final View root, final View subView) {
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();// 屏幕宽（像素，如：480px）
        final int screenHeight = getWindowManager().getDefaultDisplay().getHeight();// 屏幕高（像素，如：800p） 适配Note8
        LogUtil.e(Build.MODEL + ":screenWidth=" + screenWidth + ";screenHeight=" + screenHeight);
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                // 获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                // 若不可视区域高度大于200，则键盘显示,其实相当于键盘的高度
                if (rootInvisibleHeight > 200 && screenHeight < 2800) {
                    // 显示键盘时
                    int srollHeight = rootInvisibleHeight - (root.getHeight() - subView.getHeight()) - SoftHideKeyBoardUtil.getNavigationBarHeight(root.getContext());
                    if (srollHeight > 0) { //当键盘高度覆盖按钮时
                        root.scrollTo(0, Utils.dp2px(getApplicationContext(), 25));
                    }
                } else {
                    // 隐藏键盘时
                    root.scrollTo(0, 0);
                }
            }
        });
    }


    private void initRecoverRecycler() {
        rvRecoverAsset.setLayoutManager(new GridLayoutManager(this, 3));
        rvRecoverAsset.addItemDecoration(new SpaceItemDecoration(30));
        recoverCommonAdapter = new BaseQuickAdapter<CheckUserReqParam, BaseViewHolder>(R.layout.item_recover_phrase) {
            @Override
            protected void convert(BaseViewHolder helper, CheckUserReqParam item) {
                if (item == null) return;
                EditText recoverEdit = helper.getView(R.id.text_recover_phrase);
                helper.setText(R.id.text_recover_phrase, item.getPhrase());
                RxTextView.textChanges(recoverEdit)
                        .compose(bindToLifecycle())
                        .subscribe(charSequence -> {
                            item.setPhrase(String.valueOf(charSequence).toLowerCase());
                            recoverEdit.setFilters(new InputFilter[]{filter});
                            btnFindNextStep.setEnabled(checkNextEnable());
                        });
                recoverEdit.setHint(helper.getLayoutPosition() + 1 + "");
            }
        };
        rvRecoverAsset.setAdapter(recoverCommonAdapter);
    }

    private InputFilter filter = (source, start, end, dest, dstart, dend) -> {
        if (source.equals(" ")) return "";
        else return null;
    };

    private boolean checkNextEnable() {
//        for (CheckUserReqParam item : recoverAllList) {
//            if (TextUtils.isEmpty(item.getPhrase())) return false;
//        }
//        return true;
        return Observable.fromIterable(recoverCommonAdapter.getData())
                .all(s -> !TextUtils.isEmpty(s.getPhrase())).blockingGet();
    }

    @Override
    protected void onEvent() {
        testEnviromentFill();
    }

    private class OrderedProperties extends Properties {
        private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();

        public Enumeration<Object> keys() {
            return Collections.<Object>enumeration(keys);
        }

        public Object put(Object key, Object value) {
            keys.add(key);
            return super.put(key, value);
        }

        public synchronized Object remove(Object key) {
            keys.remove(key);
            return super.remove(key);
        }

        public Set<Object> keySet() {
            return keys;
        }

        public Set<String> stringPropertyNames() {
            Set<String> set = new LinkedHashSet<String>();
            for (Object key : this.keys) {
                set.add((String) key);
            }
            return set;

        }
    }

    /**
     * 测试环境自动填充
     */
    @SuppressLint("DefaultLocale")
    private void testEnviromentFill() {
        Observable.combineLatest(Observable.just(NetWorkApi.API_STATE != 4)
                        .filter(p -> p)
                        .map((Function<Boolean, Map<String, List<String>>>) aBoolean -> {
                            LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();
                            try {
                                RecoverAssetActivity.OrderedProperties properties = new RecoverAssetActivity.OrderedProperties();
                                properties.load(getAssets().open("test_users.properties"));
                                Set<String> enumeration = properties.stringPropertyNames();
                                for (String key : enumeration) {
                                    String value = properties.getProperty(key);
                                    LogUtil.d("FindAssetActivity", "key=" + key + "\tvalue=" + value);
                                    ArrayList<String> words = new ArrayList<>(Arrays.asList(value.split(",")));
                                    if (words.size() != 12) {
                                        Toast.makeText(mCtx, String.format("%s对应的助记词个数为:%d个，请重新输入!!!", key, words.size()), Toast.LENGTH_LONG).show();
                                    }
                                    map.put(key, words);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return map;
                        })
                        .filter(p -> p.size() > 0)
                        .doOnNext(map -> findViewById(R.id.fab).setVisibility(View.VISIBLE)),
                RxView.clicks(findViewById(R.id.fab)), (stringListMap, o) -> stringListMap)
                .flatMap(map -> {

                    RecyclerView recyclerView = new RecyclerView(mCtx);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));
                    recyclerView.addItemDecoration(new DividerItemDecoration(mCtx, DividerItemDecoration.VERTICAL));
                    CommonAdapter<String> adapter = new CommonAdapter<String>(android.R.layout
                            .simple_list_item_1, new ArrayList<>(map.keySet())) {
                        @Override
                        public void commonconvert(BaseViewHolder helper, String item) {
                            helper.setText(android.R.id.text1, item);
                        }
                    };
                    recyclerView.setAdapter(adapter);
                    Dialog dialog = new AlertDialog.Builder(mCtx).setTitle("助记词恢复").setView(recyclerView).show();
                    PublishSubject<List<String>> objectPublishSubject = PublishSubject.create();
                    adapter.setOnItemClickListener((adapter1, view, position) -> {
                        objectPublishSubject.onNext(map.get(adapter1.getItem(position)));
                        dialog.dismiss();
                    });
                    return objectPublishSubject;
                }).subscribe(list -> {

            recoverCommonAdapter.getData().clear();
            for (String s : list) {
                CheckUserReqParam checkUserReqParam = new CheckUserReqParam();
                checkUserReqParam.setPhrase(s);
                checkUserReqParam.setSerial_number(list.indexOf(s) + 1);
                recoverCommonAdapter.addData(checkUserReqParam);
            }
            LogUtil.e("list===>", new Gson().toJson(recoverCommonAdapter.getData()));
        });
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @OnClick({R.id.mr_back_layout, R.id.fab, R.id.btn_find_next_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                finish();
                break;
            case R.id.fab:
                break;
            case R.id.btn_find_next_step:
                requestCheckUser();//请求保存助记词的接口
                break;
        }
    }

    private void requestCheckUser() {
        mPresenter.ReqCheckUser(recoverCommonAdapter.getData());
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        if (throwable.getErrorCode().equals("105")) {
            ToastUtil.show(getString(R.string.str_error_phrase));
        } else if (throwable.getErrorCode().equals("610")) {
            img_no_data.setVisibility(View.VISIBLE);
            ll_assets.setVisibility(View.GONE);
        } else if (throwable.getErrorCode().equals("0")) {
            ToastUtil.show(getString(R.string.app_verify_error));
        } else {
            ToastUtil.show(getString(R.string.server_connection_error));
        }
    }

    @Override
    public void updateReqCheckUser(LoginStatus loginStatus) {
        LogUtil.e("updateReqCheckUser=", loginStatus.toString());
        loginStatus.setPosMain(2);
        ACacheUtil.get(mCtx).put(AcacheKeys.loginbean, loginStatus);
        ACacheUtil.get(mCtx).put("find", "find");
        Bundle bundle = new Bundle();
        bundle.putString("recovery", "recovery");
        ARouter.getInstance()
                .build(ArouterConstants.SETWALLETPIN)
                .withBundle("recoverySuccess", bundle)
                .navigation(mCtx);
        finish();
    }

    @Override
    protected boolean checkToPay() {
        return false;
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = mSpace;
            outRect.right = mSpace;
        }

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }
    }

}
