package com.idcg.idcw.activitys;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;

import foxidcw.android.idcw.common.base.BaseWalletActivity;

import com.idcg.idcw.model.logic.FindSureMoneyLogic;
import com.idcg.idcw.model.params.CheckUserReqParam;
import com.idcg.idcw.presenter.impl.FindSureMoneyImpl;
import com.idcg.idcw.presenter.interfaces.FindSureMoneyContract;
import com.idcg.idcw.utils.SoftHideKeyBoardUtil;
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
import com.jakewharton.rxbinding2.view.RxView;


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
import butterknife.ButterKnife;
import butterknife.OnClick;

import foxidcw.android.idcw.common.constant.ArouterConstants;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by hpz on 2018/3/27.
 */

@Route(path = ArouterConstants.FindAsset, name = "找回资产新页面")
public class FindAssetActivity extends BaseWalletActivity<FindSureMoneyLogic, FindSureMoneyImpl> implements TextWatcher, FindSureMoneyContract.View {
    @BindView(R.id.mr_back_layout)
    LinearLayout mrBackLayout;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.ed_find_one)
    EditText edFindOne;
    @BindView(R.id.view_one)
    View viewOne;
    @BindView(R.id.ll_find_one)
    LinearLayout llFindOne;
    @BindView(R.id.ed_find_two)
    EditText edFindTwo;
    @BindView(R.id.view_two)
    View viewTwo;
    @BindView(R.id.ll_find_two)
    LinearLayout llFindTwo;
    @BindView(R.id.ed_find_three)
    EditText edFindThree;
    @BindView(R.id.view_three)
    View viewThree;
    @BindView(R.id.ll_find_three)
    LinearLayout llFindThree;
    @BindView(R.id.ed_find_four)
    EditText edFindFour;
    @BindView(R.id.view_four)
    View viewFour;
    @BindView(R.id.ll_find_four)
    LinearLayout llFindFour;
    @BindView(R.id.ed_find_five)
    EditText edFindFive;
    @BindView(R.id.view_five)
    View viewFive;
    @BindView(R.id.ll_find_five)
    LinearLayout llFindFive;
    @BindView(R.id.ed_find_six)
    EditText edFindSix;
    @BindView(R.id.view_six)
    View viewSix;
    @BindView(R.id.ll_find_six)
    LinearLayout llFindSix;
    @BindView(R.id.ed_find_seven)
    EditText edFindSeven;
    @BindView(R.id.view_seven)
    View viewSeven;
    @BindView(R.id.ll_find_seven)
    LinearLayout llFindSeven;
    @BindView(R.id.ed_find_eight)
    EditText edFindEight;
    @BindView(R.id.view_eight)
    View viewEight;
    @BindView(R.id.ll_find_eight)
    LinearLayout llFindEight;
    @BindView(R.id.ed_find_nine)
    EditText edFindNine;
    @BindView(R.id.view_nine)
    View viewNine;
    @BindView(R.id.ll_find_nine)
    LinearLayout llFindNine;
    @BindView(R.id.ed_find_ten)
    EditText edFindTen;
    @BindView(R.id.view_ten)
    View viewTen;
    @BindView(R.id.ll_find_ten)
    LinearLayout llFindTen;
    @BindView(R.id.ed_find_eleven)
    EditText edFindEleven;
    @BindView(R.id.view_eleven)
    View viewEleven;
    @BindView(R.id.ll_find_eleven)
    LinearLayout llFindEleven;
    @BindView(R.id.ed_find_twelve)
    EditText edFindTwelve;
    @BindView(R.id.view_twelve)
    View viewTwelve;
    @BindView(R.id.ll_find_twelve)
    LinearLayout llFindTwelve;
    @BindView(R.id.ll_find_last)
    LinearLayout llFindLast;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.btn_find_next_step)
    TextView btnFindNextStep;
    @BindView(R.id.rl_twelve_phrase)
    RelativeLayout rlTwelvePhrase;
    @BindView(R.id.ll_recovery_phrase)
    LinearLayout llRecoveryPhrase;
    @BindView(R.id.ll_no_phrase)
    LinearLayout llNoPhrase;

    private List<CheckUserReqParam> phraseList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_asset;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        btnFindNextStep.setEnabled(false);//初始化按钮不能点击
        edFindOne.addTextChangedListener(this);
        edFindTwo.addTextChangedListener(this);
        edFindThree.addTextChangedListener(this);
        edFindFour.addTextChangedListener(this);
        edFindFive.addTextChangedListener(this);
        edFindSix.addTextChangedListener(this);
        edFindSeven.addTextChangedListener(this);
        edFindEight.addTextChangedListener(this);
        edFindNine.addTextChangedListener(this);
        edFindTen.addTextChangedListener(this);
        edFindEleven.addTextChangedListener(this);
        edFindTwelve.addTextChangedListener(this);

        edFindOne.setFilters(new InputFilter[]{filter});
        edFindTwo.setFilters(new InputFilter[]{filter});
        edFindThree.setFilters(new InputFilter[]{filter});
        edFindFour.setFilters(new InputFilter[]{filter});
        edFindFive.setFilters(new InputFilter[]{filter});
        edFindSix.setFilters(new InputFilter[]{filter});
        edFindSeven.setFilters(new InputFilter[]{filter});
        edFindEight.setFilters(new InputFilter[]{filter});
        edFindNine.setFilters(new InputFilter[]{filter});
        edFindTen.setFilters(new InputFilter[]{filter});
        edFindEleven.setFilters(new InputFilter[]{filter});
        edFindTwelve.setFilters(new InputFilter[]{filter});

        keepWalletBtnNotOver(rlTwelvePhrase, llRecoveryPhrase);
    }

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

    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ")) return "";
            else return null;
        }
    };

    @Override
    protected void onEvent() {
        testEnviromentFill();
    }

    private class OrderedProperties extends Properties{
        private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();

        public Enumeration<Object> keys() {
            return Collections.<Object> enumeration(keys);
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
    private void testEnviromentFill() {
        Observable.combineLatest(Observable.just(NetWorkApi.API_STATE != 4)
                        .filter(p -> p)
                        .map((Function<Boolean, Map<String, List<String>>>) aBoolean -> {
                            LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();
                            try {
                                OrderedProperties properties = new OrderedProperties();
                                properties.load(getAssets().open("test_users.properties"));
                                Set<String> enumeration = properties.stringPropertyNames();
                                for (String key : enumeration) {
                                    String value = properties.getProperty(key);
                                    LogUtil.d("FindAssetActivity", "key=" + key + "\tvalue=" + value);
                                    ArrayList<String> words = new ArrayList<>(Arrays.asList(value.split(",")));
                                    if(words.size()!=12){
                                        Toast.makeText(mCtx, String.format("%s对应的助记词个数为:%d个，请重新输入!!!", key, words
                                                .size()), Toast.LENGTH_LONG).show();
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
            edFindOne.setText(list.get(0));
            edFindTwo.setText(list.get(1));
            edFindThree.setText(list.get(2));
            edFindFour.setText(list.get(3));
            edFindFive.setText(list.get(4));
            edFindSix.setText(list.get(5));
            edFindSeven.setText(list.get(6));
            edFindEight.setText(list.get(7));
            edFindNine.setText(list.get(8));
            edFindTen.setText(list.get(9));
            edFindEleven.setText(list.get(10));
            edFindTwelve.setText(list.get(11));
        });
    }

    private void RequestCheckUser() {
        mPresenter.ReqCheckUser(phraseList);
    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        btnFindNextStep.setEnabled(false);//在这里重复设置，以保证清除任意EditText中的内容，按钮重新变回不可点击状态
        btnFindNextStep.setBackgroundResource(R.drawable.item_gray_black);
        btnFindNextStep.setTextColor(getResources().getColor(R.color.gray_90));
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!(edFindSeven.getText().toString().equals("") || edFindEight.getText().toString().equals("") || edFindNine.getText().toString().equals("") || edFindTen.getText().toString().equals("")
                || edFindEleven.getText().toString().equals("") || edFindTwelve.getText().toString().equals("") || edFindOne.getText().toString().equals("") || edFindTwo.getText().toString().equals("")
                || edFindThree.getText().toString().equals("") || edFindFour.getText().toString().equals("") || edFindFive.getText().toString().equals("") || edFindSix.getText().toString().equals(""))) {
            btnFindNextStep.setEnabled(true);
            btnFindNextStep.setBackgroundResource(R.drawable.sw_ripple_btn_bg);
            btnFindNextStep.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        phraseList.clear();
        if (throwable.getErrorCode().equals("105")) {
            ToastUtil.show(getString(R.string.str_error_phrase));
        } else if (throwable.getErrorCode().equals("610")) {
            llNoPhrase.setVisibility(View.VISIBLE);
            llRecoveryPhrase.setVisibility(View.GONE);
        } else if (throwable.getErrorCode().equals("0")) {
            ToastUtil.show(getString(R.string.app_verify_error));
        } else {
            ToastUtil.show(getString(R.string.server_connection_error));
        }
    }

    @Override
    public void updateReqCheckUser(LoginStatus loginStatus) {//恢复资产之后保存posMain为2并且带着一个表示recovery到设置pin页面
        LogUtil.e("updateReqCheckUser=", loginStatus.toString());
        loginStatus.setPosMain(2);
        ACacheUtil.get(FindAssetActivity.this).put(AcacheKeys.loginbean, loginStatus);
        ACacheUtil.get(FindAssetActivity.this).put("find", "find");
        Bundle bundle = new Bundle();
        bundle.putString("recovery", "recovery");
        ARouter.getInstance()
                .build(ArouterConstants.SETWALLETPIN)
                .withBundle("recoverySuccess", bundle)
                .navigation(mCtx);
        FindAssetActivity.this.finish();
    }


    @OnClick({R.id.mr_back_layout, R.id.btn_find_next_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                this.finish();
                break;
            case R.id.btn_find_next_step:
                CheckUserReqParam phraseInfo1 = new CheckUserReqParam();
                phraseInfo1.setSerial_number(1);
                phraseInfo1.setPhrase(edFindOne.getText().toString().trim().toLowerCase());
                CheckUserReqParam phraseInfo2 = new CheckUserReqParam();
                phraseInfo2.setSerial_number(2);
                phraseInfo2.setPhrase(edFindTwo.getText().toString().trim().toLowerCase());
                CheckUserReqParam phraseInfo3 = new CheckUserReqParam();
                phraseInfo3.setSerial_number(3);
                phraseInfo3.setPhrase(edFindThree.getText().toString().trim().toLowerCase());
                CheckUserReqParam phraseInfo4 = new CheckUserReqParam();
                phraseInfo4.setSerial_number(4);
                phraseInfo4.setPhrase(edFindFour.getText().toString().trim().toLowerCase());
                CheckUserReqParam phraseInfo5 = new CheckUserReqParam();
                phraseInfo5.setSerial_number(5);
                phraseInfo5.setPhrase(edFindFive.getText().toString().trim().toLowerCase());
                CheckUserReqParam phraseInfo6 = new CheckUserReqParam();
                phraseInfo6.setSerial_number(6);
                phraseInfo6.setPhrase(edFindSix.getText().toString().trim().toLowerCase());
                CheckUserReqParam phraseInfo7 = new CheckUserReqParam();
                phraseInfo7.setSerial_number(7);
                phraseInfo7.setPhrase(edFindSeven.getText().toString().trim().toLowerCase());
                CheckUserReqParam phraseInfo8 = new CheckUserReqParam();
                phraseInfo8.setSerial_number(8);
                phraseInfo8.setPhrase(edFindEight.getText().toString().trim().toLowerCase());
                CheckUserReqParam phraseInfo9 = new CheckUserReqParam();
                phraseInfo9.setSerial_number(9);
                phraseInfo9.setPhrase(edFindNine.getText().toString().trim().toLowerCase());
                CheckUserReqParam phraseInfo10 = new CheckUserReqParam();
                phraseInfo10.setSerial_number(10);
                phraseInfo10.setPhrase(edFindTen.getText().toString().trim().toLowerCase());
                CheckUserReqParam phraseInfo11 = new CheckUserReqParam();
                phraseInfo11.setSerial_number(11);
                phraseInfo11.setPhrase(edFindEleven.getText().toString().trim().toLowerCase());
                CheckUserReqParam phraseInfo12 = new CheckUserReqParam();
                phraseInfo12.setSerial_number(12);
                phraseInfo12.setPhrase(edFindTwelve.getText().toString().trim().toLowerCase());
                phraseList.add(phraseInfo1);
                phraseList.add(phraseInfo2);
                phraseList.add(phraseInfo3);
                phraseList.add(phraseInfo4);
                phraseList.add(phraseInfo5);
                phraseList.add(phraseInfo6);
                phraseList.add(phraseInfo7);
                phraseList.add(phraseInfo8);
                phraseList.add(phraseInfo9);
                phraseList.add(phraseInfo10);
                phraseList.add(phraseInfo11);
                phraseList.add(phraseInfo12);
                RequestCheckUser();//请求保存助记词的接口
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
