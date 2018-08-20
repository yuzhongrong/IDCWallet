package foxidcw.android.idcw.otc.widgets.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseFragment;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import foxidcw.android.idcw.common.model.bean.OTCDotShowBean;
import foxidcw.android.idcw.common.utils.ErrorCodeUtils;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.adapter.OTCAcceptorQuoteAdapter;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.intentkey.EventTags;
import foxidcw.android.idcw.otc.iprovider.OTCGetQuoteOrdersServices;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcOrdersBean;
import foxidcw.android.idcw.otc.model.beans.OTCNewOrderNoticeAcceptantBean;
import foxidcw.android.idcw.otc.model.logic.OTCGetQuoteOrdersLogic;
import razerdp.basepopup.BasePopupWindow;

public class OTCAcceptorQuotePopup
        extends BasePopupWindow
{

    private ImageView                            mCloseIv;
    private TextView                             mInfoTv;
    private RecyclerView                         mAcceptorsQuoteRv;
    private OTCAcceptorQuoteAdapter              mOTCAcceptorQuoteAdapter;
    private Context mContext;
    private List<OTCNewOrderNoticeAcceptantBean> mProviders;
    private    BaseFragment mBaseFragment;
    public OTCAcceptorQuotePopup(Context context, BaseFragment otcRecordFragment) {
        super(context);
//        ARouter.getInstance()
//               .inject(this);
        EventBus.getDefault().register(this);
        this.mContext = context;
        mBaseFragment = otcRecordFragment;
        mOTCGetQuoteOrdersServices = new OTCGetQuoteOrdersLogic();
    }

//    @Autowired
    OTCGetQuoteOrdersServices mOTCGetQuoteOrdersServices;

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return mCloseIv;
    }

    @Override
    public View onCreatePopupView() {

        View rootView = createPopupById(R.layout.popup_otc_acceptor_quote);
        mCloseIv = rootView.findViewById(R.id.acceptor_quote_close_iv);
        mInfoTv = rootView.findViewById(R.id.acceptor_quote_info_tv);
        mAcceptorsQuoteRv = rootView.findViewById(R.id.acceptor_quote_items_rv);
        mAcceptorsQuoteRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //        mAcceptorsQuoteRv.addOnItemTouchListener(new OnItemClickListener() {
        //            @Override
        //            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
        //
        //            }
        //        });
        mProviders = new ArrayList<OTCNewOrderNoticeAcceptantBean>();
        mOTCAcceptorQuoteAdapter = new OTCAcceptorQuoteAdapter(mProviders);
        mOTCAcceptorQuoteAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mAcceptorsQuoteRv.setAdapter(mOTCAcceptorQuoteAdapter);
        mOTCAcceptorQuoteAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.provider_otc_close_iv) {

                    mOTCGetQuoteOrdersServices.reqCancelQuoteOrder(mProviders.get(position).getOrderID()).compose(mBaseFragment.bindToLifecycle()).subscribeWith(
                            new RxProgressSubscriber<Object>(mBaseFragment) {
                                @Override
                                public void onSuccess(Object data) {
                                    mProviders.remove(position);
                                    mOTCAcceptorQuoteAdapter.notifyItemRemoved(position);
                                    if(mProviders!=null && mProviders.size() == 0){
                                        dismiss();
                                    }
                                    EventBus.getDefault().post(new OTCGetOtcOrdersBean(), EventTags.TAGS_STATUS_CHANGE);
                                    int uotQuotedNum = 0;
                                    for(int i = 0; i< mProviders.size(); i++){
                                        if(mProviders.get(i).getStatus() == 8){ //统计未报价的otc订单的数量
                                            uotQuotedNum++;
                                        }
                                    }
                                    if(uotQuotedNum == 0){ //未报价的订单为当前订单隐藏小红点
                                        EventBus.getDefault().post(new OTCDotShowBean(false), EventTags.TAGS_OTC_SHOW_DOT);
                                    }

                                }
                                @Override
                                protected void onError(ResponseThrowable ex) {
                                    ToastUtil.show(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
                                }
                            });
                }
            }
        });
        mOTCAcceptorQuoteAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OTCNewOrderNoticeAcceptantBean item = mProviders.get(
                        position);

                if (item.getDirection() == 1 || item.getDirection() == 2) {
                    if (item.getDeadLineSeconds() == 0 && item.getStatus() != 6) { //倒计时为0s不跳转
                        return;
                    }
                    if (item.getStatus() == 1) { //待确认 已报价
                        ARouter.getInstance().build(OTCConstant.QUOTATION)
                               .withSerializable(OTCConstant.NOTICEORDERBEAN, item)
                               .navigation();
                        dismiss();
                    } else if (item.getStatus() == 6) { //交易中
                        ARouter.getInstance()
                               .build(OTCConstant.TRADE_DETAIL)
                               .withInt("order_id", item.getOrderID())
                               .navigation();
                        dismiss();
                    } else if (item.getStatus() == 8) { //未报价
                        ARouter.getInstance().build(OTCConstant.QUOTATION)
                               .withSerializable(OTCConstant.NOTICEORDERBEAN, item)
                               .navigation();
                        dismiss();
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void showPopupWindow() {
        super.showPopupWindow();

    }

    @Override
    public View initAnimaView() {
        return null;
    }

    public interface OnAcceptorQuoteListener {

    }

    public OTCAcceptorQuotePopup setData(OTCNewOrderNoticeAcceptantBean OTCNewOrderNoticeAcceptantBean) {
        if(mProviders!=null && mProviders.size() <5 ){
            mProviders.add(0, OTCNewOrderNoticeAcceptantBean);
            mOTCAcceptorQuoteAdapter.notifyItemInserted(0);
        }

        return this;
    }

    public OTCAcceptorQuotePopup setFirstData(List<OTCNewOrderNoticeAcceptantBean> otcNewOrderNoticeAcceptantBeans){
        mProviders = otcNewOrderNoticeAcceptantBeans;
        mOTCAcceptorQuoteAdapter.setNewData(mProviders);
        return this;
    }

    //获取列表适配器
    public OTCAcceptorQuoteAdapter getAdapter(){
        if(mOTCAcceptorQuoteAdapter != null){
            return mOTCAcceptorQuoteAdapter;
        }
        return null;
    }

}
