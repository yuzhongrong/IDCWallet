package com.idcg.idcw.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.model.bean.PhraseListBean;
import com.idcg.idcw.model.bean.VerfifyListBean;
import com.idcg.idcw.model.logic.BackPhraseLogic;
import com.idcg.idcw.model.params.PhraseDataReqAndResParam;
import com.idcg.idcw.presenter.impl.BackPhrasePresenterImpl;
import com.idcg.idcw.presenter.interfaces.BackPhraseContract;
import com.idcg.idcw.screen.ScreenshotSubscriber;
import com.cjwsc.idcm.Utils.LogUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import org.simple.eventbus.Subscriber;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.constant.ArouterConstants;
import foxidcw.android.idcw.common.model.bean.PosInfo;

/**
 * 作者：hxy
 * 邮箱：hua.xiangyang@shuweikeji.com
 * 版本号：1.0
 * 项目包名：FoxIDCW com.idcg.idcw.activitys
 * 备注消息：
 * 修改时间：2018/3/17 19:40
 **/
@Route(path = ArouterConstants.BACK_AGAIN_PHRASE,name = "用户备份恢复短语")
public class BackAgainPhraseActivity extends BaseWalletActivity<BackPhraseLogic, BackPhrasePresenterImpl>
        implements View.OnClickListener, BackPhraseContract.View {

    @BindView(R.id.mr_back_layout)
    LinearLayout imgBack;
    @BindView(R.id.tv_set_Name)
    TextView tvSetName;
    @BindView(R.id.img_phrase_progress)
    ImageView imgPhraseProgress;
    @BindView(R.id.tv_phrase)
    TextView tvPhrase;
    @BindView(R.id.btn_last_step)
    Button btnLastStep;
    @BindView(R.id.btn_next_step)
    Button btnNextStep;

    private List<PhraseDataReqAndResParam.RandomWordBean> phraseList = new ArrayList<>();
    private List<PhraseDataReqAndResParam.VerinfyWordBean> verinfyList = new ArrayList<>();

    private static final int INFO_AL_WHAT_UNM = 0x2432;
    private static final int INFO_ED_WHAT_UNM = 0x2422;
    private ScreenshotSubscriber mScreenshotSubscriber;
    private final int PERMISSION_REQUEST_CODE = 0x001;
    private boolean isRequireCheck = true;
    private int index = 1;
    private int z = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INFO_AL_WHAT_UNM:
                    tvPhrase.setText(phraseList.get(0).getPhrase());
                    break;
                case INFO_ED_WHAT_UNM:
                    break;
            }
        }
    };
    private Dialog dialog;
    private View inflater;
    private TextView tv_sure;
    private int tag;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_back_phrase;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        btnLastStep.setEnabled(false);
        tvSetName.setText(R.string.back_up_phrase);
        //initBanScreenDialog();
    }

    private void initBanScreenDialog() {
        try {
            dialog = new Dialog(this, R.style.shuweiDialog);
            inflater = LayoutInflater.from(this).inflate(R.layout.ban_screen_dialog, null);
            dialog.setContentView(inflater);
            dialog.setCancelable(false);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setAttributes(lp);
            tv_sure = (TextView) inflater.findViewById(R.id.tv_sure);
            tv_sure.setOnClickListener(this);
            dialog.show();
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        phraseList.clear();

        showDialog();
        mPresenter.requestPhraseData(String.valueOf(0));

        btnLastStep.setVisibility(View.INVISIBLE);
        btnNextStep.setText(R.string.next_word);
        index = 1;
        z = 0;
        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_one);
    }

    @Override
    protected void onEvent() {

    }

    @Override
    protected BaseView getView() {
        return this;
    }

    @OnClick({R.id.mr_back_layout, R.id.btn_last_step, R.id.btn_next_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mr_back_layout:
                this.finish();
                break;
            case R.id.btn_last_step:
                if (index <= 1) return;
                z = index - 2;
                index -= 1;
                List<String> stringList4 = new ArrayList<>();
                for (int i = z; i < 12; i++) {//stringList 列表换成你获取的列表数据
                    if (i < index) {
                        stringList4.add(phraseList.get(i).getPhrase());
                    }
                    if (index == 2) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_two);
                    } else if (index == 1) {
                        btnLastStep.setText(R.string.previous_word);
                        btnLastStep.setEnabled(false);
                        btnLastStep.setVisibility(View.INVISIBLE);
                        //btnLastStep.setTextColor(getResources().getColor(R.color.t));
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_one);
                    } else if (index == 3) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_three);
                    } else if (index == 4) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_four);
                    } else if (index == 5) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_five);
                    } else if (index == 6) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_six);
                    } else if (index == 7) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_seven);
                    } else if (index == 8) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_eight);
                    } else if (index == 9) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_nine);
                    } else if (index == 10) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_ten);
                    } else if (index == 11) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_eleven);
                    } else if (index == 12) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_teowlve);
                    }
                }

                LogUtil.e("=====" + stringList4.toString(), "   index" + index);
                tvPhrase.setText(stringList4.get(0));
                break;
            case R.id.btn_next_step:
                if (index == 12) {
                    // TODO
                    LogUtil.e("phraseList:===",phraseList.toString());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dataList",new PhraseListBean(phraseList));
                    bundle.putSerializable("verifyList",new VerfifyListBean(verinfyList));
                    ARouter.getInstance()
                            .build(ArouterConstants.PHRASE_CONFIRM)
                            .withBundle("number", bundle)
                            .navigation();
                }
                if (index >= 12) return;

                int j = index;
                index += 1;
                List<String> stringList1 = new ArrayList<>();
                List<String> stringList3 = new ArrayList<>();
                for (int i = j; i < 12; i++) {
                    if (i < index) {
                        stringList3.add(phraseList.get(i).getPhrase());
                    }
                    if (index == 2) {
                        btnLastStep.setText(R.string.previous_word);
                        btnLastStep.setVisibility(View.VISIBLE);
                        btnLastStep.setEnabled(true);
                        btnLastStep.setTextColor(getResources().getColor(R.color.tipper_blue_color));
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_two);
                    } else if (index == 3) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_three);
                    } else if (index == 4) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_four);
                    } else if (index == 5) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_five);
                    } else if (index == 6) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_six);
                    } else if (index == 7) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_seven);
                    } else if (index == 8) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_eight);
                    } else if (index == 9) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_nine);
                    } else if (index == 10) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_ten);
                    } else if (index == 11) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.next_word);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_eleven);
                    } else if (index == 12) {
                        btnLastStep.setText(R.string.previous_word);
                        btnNextStep.setText(R.string.tv_next_step);
                        imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_teowlve);
                    }
                }

                LogUtil.e("=====" + stringList1.toString(), "   index" + index);
                tvPhrase.setText(stringList3.get(0));

                break;
        }
    }
    @Subscriber
    public void onBackInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 50) {
                BackAgainPhraseActivity.this.finish();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }
    @Subscriber
    public void onBackAgainInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 51) {
                BackAgainPhraseActivity.this.finish();
                intentFActivity(BackAgainPhraseActivity.class);
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }
    @Subscriber
    public void onConfirmAgainInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 1212) {
                phraseList.clear();
                showDialog();
                mPresenter.requestPhraseData(String.valueOf(0));
                btnLastStep.setVisibility(View.INVISIBLE);
                index = 1;
                z = 0;
                imgPhraseProgress.setImageResource(R.mipmap.icon_phrase_one);

            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        ToastUtil.show(getString(R.string.server_connection_error));
    }

    @Override
    public void updateRequestPhraseData(PhraseDataReqAndResParam phraseDataReqAndResBean) {
        dismissDialog();
        phraseList.clear();
        verinfyList.clear();
        phraseList.addAll(phraseDataReqAndResBean.getRandomWord());
        verinfyList.addAll(phraseDataReqAndResBean.getVerinfyWord());
        Message message = mHandler.obtainMessage();
        message.obj = phraseDataReqAndResBean;
        message.what = INFO_AL_WHAT_UNM;
        mHandler.sendMessage(message);
    }

    @Override
    public void updateRequestSavePhrase(Object obj) {

    }
}
