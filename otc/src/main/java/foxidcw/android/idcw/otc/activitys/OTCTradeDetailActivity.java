package foxidcw.android.idcw.otc.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.text.TextUtils;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.cjwsc.idcm.base.ui.pagestatemanager.PageManager;
import com.cjwsc.idcm.constant.BaseSignalConstants;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.callback.RxSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.cjwsc.idcm.signarl.impl.HubOnDataCallBackImpl;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yy.chat.IMessage;
import com.yy.chat.ImgMessageBody;
import com.yy.chat.widget.ChatInputMenu;
import com.yy.chat.widget.ChatPrimaryMenu;
import com.yy.chat.widget.MessageList;
import com.yy.chat.widget.OnItemChildActionCallBack;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.utils.ErrorCodeUtils;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.chat.ChatMessage;

import com.cjwsc.idcm.Utils.TakePhotoUtils;

import foxidcw.android.idcw.otc.intentkey.EventTags;
import foxidcw.android.idcw.otc.iprovider.OTCGetChatHistoryProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetOrderDetailServices;
import foxidcw.android.idcw.otc.model.OTCOrderStatus;
import foxidcw.android.idcw.otc.model.beans.OTCChatHistoryBean;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcOrdersBean;
import foxidcw.android.idcw.otc.model.beans.OTCStateBean;
import foxidcw.android.idcw.otc.widgets.dialog.OTCTimeUpPopup;
import foxidcw.android.idcw.otc.widgets.dialog.OTCTwoBtnTitlePopWindow;
import foxidcw.android.idcw.otc.widgets.widget.OTCTradeDetailBottomOptionLayout;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static foxidcw.android.idcw.otc.constant.OTCConstant.TRADE_DETAIL;

@Route(path = TRADE_DETAIL, name = "交易详情页面")
public class OTCTradeDetailActivity extends BaseWalletActivity implements View.OnClickListener, OTCTradeDetailBottomOptionLayout.OnEndTimerListener {

    public static final int REQUEST_CODE_LOCAL = 1;

    private View mViewToggle;
    private AppBarLayout mAppBarLayout;
    private ImageView mImgBack;
    private ImageView mIvSlide;
    private LinearLayout mMrBackLayout;
    private LinearLayout mLayoutDetailTop;
    private RelativeLayout mTitleBar;
    private MessageList mMessageList;
    private TextView mSetName;
    private ChatInputMenu mInputMenu;
    private View mRootView;
    private double currentKeyboardH;
    private boolean mExpend;

    private OTCTradeDetailBottomOptionLayout bottom_layout;

    private int orderId;

    @Autowired
    OTCGetChatHistoryProviderServices getChatHistoryProviderServices;

    @Autowired
    OTCGetOrderDetailServices getOrderDetailServices;


    private PageManager pageManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BaseSignalConstants.isAddGroup=true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ototrade_detail;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        dialog.setBackPressEnable(false);
        mViewToggle = (View) findViewById(R.id.view_toggle);
        mIvSlide = (ImageView) findViewById(R.id.iv_slide);
        mRootView = findViewById(R.id.root);
        mViewToggle.setOnClickListener(this);
        mAppBarLayout = findViewById(R.id.app_bar_layout);
        mImgBack = (ImageView) findViewById(R.id.img_back);
        mMrBackLayout = (LinearLayout) findViewById(R.id.mr_back_layout);
        mMrBackLayout.setOnClickListener(this);
        mLayoutDetailTop = findViewById(R.id.layout_detail_top);
        mTitleBar = (RelativeLayout) findViewById(R.id.title_bar);
        mMessageList = (MessageList) findViewById(R.id.message_list);
        mInputMenu = (ChatInputMenu) findViewById(R.id.input_menu);
        mSetName = (TextView) findViewById(R.id.tv_set_Name);
        mSetName.setText(R.string.otc_trade_detail);
        bottom_layout = findViewById(R.id.bottom_layout);

        //获取传过来的bean

        if (getIntent() != null && getIntent().getExtras() != null) {

            orderId = getIntent().getExtras().getInt("order_id");

        }


        //   请求获取订单详情-----> 成功后加载历史聊天记录成功后------>开启聊天signalr推送【齿轮模式环环相扣】

        pageManager = PageManager.init(findViewById(R.id.coordinator_layout), false, new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });

    }


    private String userid = "";//增加变量避免每次都去获取

    @Override
    protected String getUserId() {//进入页面初始化signalr的时候调用
        return userid = String.valueOf(LoginUtils.getLoginBean(this).getId());
    }

    @Override
    protected void onEvent() {
        observerInput();
        mInputMenu.setChatPrimaryMenuListener(new ChatPrimaryMenu.ChatPrimaryMenuListener() {
            @Override
            public void onSendBtnClicked(String content) {
//                Toast.makeText(mCtx, content, Toast.LENGTH_SHORT).show();
                sendTextMessage(content);
            }

            @Override
            public void onSendPicClicked() {
                selectPicFromLocal();
            }
        });

        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.COLLAPSED) {
                    mIvSlide.setImageResource(R.drawable.otc_slide_down);
                    mExpend = false;
                } else if (state == State.EXPANDED) {
                    mIvSlide.setImageResource(R.drawable.otc_slide_up);
                    mExpend = true;
                }
            }
        });

        mMessageList.setItemClickListener(new OnItemChildActionCallBack() {
            @Override
            public void onResendClick(IMessage message) {
                OTCTwoBtnTitlePopWindow OTCTwoBtnTitlePopWindow = new OTCTwoBtnTitlePopWindow(mCtx);
                OTCTwoBtnTitlePopWindow.setTitle(getString(R.string.otc_resend))
                        .setConfirmContent(mCtx.getString(R.string.confirm))
                        .setCancelContent(mCtx.getString(R.string.cancel))
                        .showPopupWindow();
                OTCTwoBtnTitlePopWindow.setClickListener(new OTCTwoBtnTitlePopWindow.BtnClickListener() {
                    @Override
                    public void onBtnClick(int type) {
                        if (type == 1) {
                            message.handleMsg(mCtx, mMessageList.getAdapter());
                        }
                    }
                });
            }

            @Override
            public void onBubbleClick(IMessage message, int position) {
                if (!(message.getBody() instanceof ImgMessageBody))
                    return;
                Observable.fromIterable(mMessageList.getData())
                        .filter(new Predicate<IMessage>() {
                            @Override
                            public boolean test(IMessage msg) throws Exception {
                                return msg.getBody() instanceof ImgMessageBody;
                            }
                        })
                        .toList()
                        .subscribe(new Consumer<List<IMessage>>() {
                            @Override
                            public void accept(List<IMessage> iMessages) throws Exception {
                                int pos = iMessages.indexOf(message);
                                OTCPreviewImgListActivity.preview((Activity) mCtx, false, pos, Observable.fromIterable(iMessages).map(new Function<IMessage, String>() {
                                    @Override
                                    public String apply(IMessage message) throws Exception {
                                        return ((ImgMessageBody) message.getBody()).getUrl();
                                    }
                                }).toList().blockingGet(), null);
                            }
                        });
            }

            @Override
            public void onUserAvatarClick(IMessage message) {

            }
        });


        //初始化数据

        initData();

    }


    private OTCGetOtcOrdersBean mCurrentOrderBean;

    private void initData() {
        getOrderDetailServices.requestOrderDetailService(orderId + "")
                .compose(this.bindToLifecycle())
                .subscribeWith(new RxProgressSubscriber<OTCGetOtcOrdersBean>(this) {

                    @Override
                    public void onSuccess(OTCGetOtcOrdersBean data) {
                        LogUtil.d("--------获取订单详情成功------->" + data);
                        if (data == null) return;

                        mCurrentOrderBean = data;

                        //刷新ui
                        EventBus.getDefault().post(data, EventTags.TAGS_STATUS_CHANGE);
                        //加载聊天历史+创建聊天推送
                        loadData(true);
                        pageManager.showContent();
                        refreshInputMenu();
                        bottom_layout.setVisibility(View.VISIBLE);//默认是GONE第一次进去请求订单成功显示出来

                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {

                        LogUtil.d("--------获取订单详情失败------->" + ex.getMessage());
                        ToastUtil.show("获取订单详情失败");
                        pageManager.showError();
                        mInputMenu.setVisibility(View.GONE);

                    }
                });


    }

    private void refreshInputMenu() {
        if (mCurrentOrderBean == null) return;
        switch (mCurrentOrderBean.getStatus()) {
            case OTCOrderStatus.WaitPay:
            case OTCOrderStatus.Transfered:
            case OTCOrderStatus.Paied:
            case OTCOrderStatus.Appeal:
            case OTCOrderStatus.UploadPayCertficate:
            case OTCOrderStatus.WaitApproval:
                mIvSlide.setVisibility(View.VISIBLE);
                mInputMenu.setVisibility(View.VISIBLE);
                if (mCurrentOrderBean.getDirection() == 1) {
                    mInputMenu.getChatPrimaryMenu().getEditText().setHint(R.string.otc_chat_with_seller_please);
                } else {
                    mInputMenu.getChatPrimaryMenu().getEditText().setHint(R.string.otc_chat_with_buyer_please);
                }
                break;
            default:
                mIvSlide.setVisibility(View.GONE);
                mInputMenu.setVisibility(View.GONE);
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mMessageList.getLayoutParams();
                lp.bottomMargin = 0;
                mMessageList.setLayoutParams(lp);
                break;
        }
        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) mLayoutDetailTop.getLayoutParams();
        switch (mCurrentOrderBean.getStatus()) {
            case OTCOrderStatus.Finish:
            case OTCOrderStatus.Cancel:
            case OTCOrderStatus.CustomerPayCoin:
            case OTCOrderStatus.CustomerRefund:
                layoutParams.setScrollFlags(0);
                break;
            default:
                layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                break;
        }
        mLayoutDetailTop.setLayoutParams(layoutParams);
    }


    //推送过来后 ----->获取订单详情----->post出去
    public void requestTradeDetailInfo(String orderId) {//延迟2秒操作 防止后台正在处理订单详情

        //不接受不是自己的订单
        if(Integer.parseInt(orderId)!=mCurrentOrderBean.getId())return;

        getOrderDetailServices.requestOrderDetailService(orderId + "")
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<OTCGetOtcOrdersBean>() {

                    @Override
                    public void onSuccess(OTCGetOtcOrdersBean data) {
                        LogUtil.d("--------获取订单详情成功------->" + data);
                        if (data == null) return;
                        mCurrentOrderBean = data;
                        //刷新ui

                        EventBus.getDefault().post(data, EventTags.TAGS_STATUS_CHANGE);
                        refreshInputMenu();
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {

                        LogUtil.d("--------获取订单详情失败------->" + ex.getMessage());
                        ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));

                    }
                });

    }


    private void loadData(boolean startSinalr) {
        //--->获取聊天历史--->获取signalr host地址----->启动signalr
        getChatHistoryProviderServices.requestChatHistory(orderId + "")
                .compose(this.bindToLifecycle())
                .subscribeWith(new RxProgressSubscriber<List<OTCChatHistoryBean>>(this) {
                    @Override
                    public void onSuccess(List<OTCChatHistoryBean> data) {

                        LogUtil.d("----获取聊天记录-->" + data.size());
                        String userId = LoginUtils.getLoginBean(BaseApplication.getContext()).getId() + "";
                        if (data != null) {
                            //这里需要一个对象转换成处理
                            Flowable.just(data)
                                    .observeOn(Schedulers.io())
                                    .map(new Function<List<OTCChatHistoryBean>, List<IMessage>>() {
                                        @Override
                                        public List<IMessage> apply(List<OTCChatHistoryBean> otcChatHistoryBeans) throws Exception {

                                            List<IMessage> newmessage = new ArrayList<>(otcChatHistoryBeans.size());

                                            for (OTCChatHistoryBean item : otcChatHistoryBeans) {
                                                long timeStamp = item.getTimeStamp();
                                                if (!TextUtils.isEmpty(item.getMessage())) {//文字

                                                    if (item.getChatObjectCategory() == 0) {//系统消息
                                                        int id1 = (int) Double.parseDouble(item.getUserID());
                                                        int id2 = Integer.parseInt(userId);

                                                        if (id1 == id2) {//是自己消息才去处理
                                                            ChatMessage sysMessage = ChatMessage.createSysMessage(item
                                                                    .getMessage());
                                                            newmessage.add(sysMessage);
                                                            sysMessage.setTimeStamp(timeStamp);
                                                        }

                                                    } else {
                                                        ChatMessage message;

                                                        if (userId.equals(item.getSendUserID())) {//我的消息
//
                                                            message = ChatMessage.createTextSendMessage(item.getMessage(), "");
                                                            message.setStatus(IMessage.STATUS_SUCCESS);

                                                        } else {//对方消息

                                                            message = ChatMessage.createTextReceiveMessage(item.getMessage(), "");

                                                        }
                                                        message.setTimeStamp(timeStamp);
                                                        newmessage.add(message);
                                                    }

                                                } else {//图片

                                                    ChatMessage message;
                                                    if (!userId.equals(item.getUserID())) {
                                                        message = ChatMessage.createImgReceiveMessage(item.getFileUrl(), "");

                                                    } else {
                                                        message = ChatMessage.createImgSendMessage(item.getFileUrl(), "");
                                                        message.setStatus(ChatMessage.STATUS_SUCCESS);
                                                    }
                                                    message.setTimeStamp(timeStamp);
                                                    newmessage.add(message);

                                                }


                                            }

                                            return newmessage;
                                        }
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new RxSubscriber<List<IMessage>>() {
                                        @Override
                                        public void onSuccess(List<IMessage> iMessages) {
                                            mMessageList.addData(iMessages);
                                            if (startSinalr) startSignalr();
                                        }

                                        @Override
                                        protected void onError(ResponseThrowable ex) {
                                            LogUtil.d("---转换失败---->" + ex.getMessage());
                                        }
                                    });

                        }

                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {

                        LogUtil.d("----获取聊天记录失败-->" + ex.getErrorMsg());

                    }
                });

    }

    private void startSignalr() {
        LoginStatus loginBean = LoginUtils.getLoginBean(this);
        String userid = "";
        if (loginBean != null)
            userid = loginBean.getId() + "";
        if (!TextUtils.isEmpty(userid)) userId = userid;
        initSignalr(BaseSignalConstants.SIGNAL_HOST, orderId + "", BaseSignalConstants.SIGNAL_HUB_NAME);
    }


//    private boolean isMineMsg(OTCChatHistoryBean bean){
//
//
//            int id1= (int) Double.parseDouble(bean.getUserID());
//            int id2= Integer.parseInt(userid);
//            if(id1==id2)return true;//系统消息不是发给我的不接
//            return  false;
//    }

    private void observerInput() {
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mRootView.getWindowVisibleDisplayFrame(r);
//                int statusBarH = getStatusBarHeight();//状态栏高度
                int screenH = mRootView.getRootView().getHeight();
                int keyboardH = screenH - (r.bottom - r.top);

                if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
                    return;
                }
                currentKeyboardH = keyboardH;

                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mMessageList.getLayoutParams();
                layoutParams.bottomMargin = mInputMenu.getVisibility() == View.GONE ? 0 : mInputMenu.getChatPrimaryMenu().getHeight();
                mMessageList.setLayoutParams(layoutParams);
                if (keyboardH < 300) {//说明是隐藏键盘的情况并且表情没有被展示
                    return;
                }
                refreshSelectLast();
            }
        });
    }

    private String toId;

    public void sendTextMessage(String message) {

        mMessageList.addData(ChatMessage.createTextSendMessage(message, toId));
        refreshSelectLast();
    }

    public void sendImgMessage(String path) {
        mMessageList.addData(ChatMessage.createImgSendMessage(path, toId));
        refreshSelectLast();
        //在这里添加发送消息
    }

    private void refreshSelectLast() {
        if (mExpend) mAppBarLayout.setExpanded(false);
        mMessageList.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMessageList.refreshSelectLast(false);
            }
        }, 100);
    }

    protected void selectPicFromLocal() {
        TakePhotoUtils.showDialog(this, new TakePhotoUtils.CallBack() {
            @Override
            public void onActivityResult(String path) {
                sendImgMessage(path);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_CODE_LOCAL) {
            sendImgMessage(getAbsolutePath(mCtx, data.getData()));
        }
    }

    public String getAbsolutePath(final Context context,
                                  final Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);

        String picturePath;
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

        } else {
            File file = new File(selectedImage.getPath());

            picturePath = file.getAbsolutePath();
        }
        return picturePath;
    }

    @Override
    protected BaseView getView() {
        return null;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.view_toggle) {
            mAppBarLayout.setExpanded(!mExpend);
        } else if (id == R.id.mr_back_layout) {
            onBackPressed();
        }
    }

    @Override
    public boolean isShouldHideInput(View v, MotionEvent event) {
        v = mInputMenu.getChatPrimaryMenu();
        try {
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
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }

        return false;
    }

    @Override
    public void onEndTime(OTCGetOtcOrdersBean bean) {

        if (bean.getStatus() == OTCOrderStatus.WaitPay) {
            new OTCTimeUpPopup(this)
                    .setListener(new OTCTimeUpPopup.OnKnowClickListener() {//弹框比较好如果直接请求后台还没有设置好详情 返回的还是1
                        @Override
                        public void onClick() {
                            requestTradeDetailInfo(bean.getId() + "");
                        }
                    }).setTimesUpInfo(getResources().getString(R.string.otc_str_deal_is_off))
                    .showPopupWindow();

        }
        else {
            requestTradeDetailInfo(bean.getId() + "");

        }


    }

    abstract static class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

        enum State {
            EXPANDED,
            COLLAPSED,
            IDLE
        }

        private State mCurrentState = State.IDLE;

        @Override
        public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            if (i == 0) {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED);
                }
                mCurrentState = State.EXPANDED;
            } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED);
                }
                mCurrentState = State.COLLAPSED;
            } else {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE);
                }
                mCurrentState = State.IDLE;
            }
        }

        public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
    }


    @Override
    public void subScribes() {
        //订阅聊天消息
        subscribe("otcChatMessage", new HubOnDataCallBackImpl<OTCChatHistoryBean>() {
            @Override
            public void convert(OTCChatHistoryBean receivemsg) {

                if (receivemsg != null) {
                    long timeStamp = receivemsg.getTimeStamp();
                    if (!TextUtils.isEmpty(receivemsg.getMessage())) {//显示文字


                        if (receivemsg.getChatObjectCategory() == 0) {//系统

                            int id1 = (int) Double.parseDouble(receivemsg.getUserID());
                            int id2 = Integer.parseInt(userid);
                            if (id1 != id2) return;//系统消息不是发给我的 不接

                            //接受的消息是不需要发送功能的
                            ChatMessage sysMessage = ChatMessage.createSysMessage(receivemsg.getMessage());
                            sysMessage.setTimeStamp(timeStamp);
                            mMessageList.addData(sysMessage);

                        } else if (receivemsg.getChatObjectCategory() == 1) {//对方

                            int id1 = (int) Double.parseDouble(receivemsg.getSendUserID());
                            int id2 = Integer.parseInt(userid);
                            if (id1 == id2) return;//解决自动回复问题
                            ChatMessage textReceiveMessage = ChatMessage.createTextReceiveMessage(receivemsg
                                    .getMessage(), "");
                            textReceiveMessage.setTimeStamp(timeStamp);
                            mMessageList.addData(textReceiveMessage);
                        }

                    } else {//显示图片

                        if(receivemsg!=null&&!TextUtils.isEmpty(receivemsg.getSendUserID())){

                            int id1 = (int) Double.parseDouble(receivemsg.getSendUserID());
                            int id2 = Integer.parseInt(userid);
                            if (id1 == id2) return;
                        }

                        ChatMessage imgReceiveMessage = ChatMessage.createImgReceiveMessage(receivemsg.getFileUrl(),
                                "");
                        imgReceiveMessage.setTimeStamp(timeStamp);
                        mMessageList.addData(imgReceiveMessage);
                    }
                }
            }
        });


        //订阅买方和卖方的状态操作 会动态更新这个页面
        subscribe("otcOrderStatusChange", new HubOnDataCallBackImpl<OTCStateBean>() {
            @Override
            public void convert(OTCStateBean bean) {

                //改变当前订单mCurrentOrderBean的内部值【因为mCurrentOrderBean 这个页面就这一个bean
                // 所以不用重新new】
                if (mCurrentOrderBean != null) {

                    //请求详情拿到最新bean信息
                    requestTradeDetailInfo(bean.getOrderID() + "");

                    //   mCurrentOrderBean.setStatus(bean.getStatus());
                    //    EventBus.getDefault().post(mCurrentOrderBean,EventTags.TAGS_STATUS_CHANGE);

                }

            }
        });

    }


    @Override
    protected void OnNetFail() {

    }

    @Override
    protected void OnNetSuccess() {//连接成功清理列表 重新获取聊天历史

        updateReConnectionOption();

    }


    private void updateReConnectionOption() {
        //清理列表
        mMessageList.clearAll();
        loadData(false);
    }

    @Override
    protected boolean isCheckVersion() {
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
