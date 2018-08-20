package foxidcw.android.idcw.otc.widgets.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjwsc.idcm.Utils.Utils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.Locale;

import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.chat.ChatMessage;
import foxidcw.android.idcw.otc.intentkey.EventTags;
import foxidcw.android.idcw.otc.model.OTCOrderStatus;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcOrdersBean;

/**
 * 设计理念：垃圾自回收模式
 * Created by yuzhongrong on 2018/5/4.
 * 此页面是交易详情订单显示页面控制订单显示包括{买家和卖家}
 */

public class OTCTradeDetailOrderLayout extends LinearLayout {

    private View root;
    private TextView ordervalue;//订单号
    private TextView createtime;//订单创建时间
    private TextView buy_or_sell;//买入还是卖出标题
    private TextView number;//买入或者卖出币的数量
    private TextView exchange_type_name;//收款还是付款
    private TextView exchange_type_value;//收款还是付款的金额
    private TextView people;//买家还是卖家
    private TextView people_name;//买家还是卖家
    private TextView reference_number;//收付款参考号标题
    private TextView reference_number_value;//收付款参考号值

    //定义每个item 布局的root  目前为了控制其显示和隐藏
    private RelativeLayout root1;
    private RelativeLayout root2;
    private RelativeLayout root3;
    private RelativeLayout root4;

    public OTCTradeDetailOrderLayout(Context context) {
        super(context);
        EventBus.getDefault().register(this);
        initView();
    }

    public OTCTradeDetailOrderLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        //解析全部布局
        root = LayoutInflater.from(getContext()).inflate(R.layout.activity_trade_info, this, true);
        //解析子布局
        ordervalue = $(R.id.ordervalue);
        createtime = $(R.id.createtime);
        buy_or_sell = $(R.id.buy_or_sell);
        number = $(R.id.number);
        exchange_type_name = $(R.id.exchange_type_name);
        exchange_type_value = $(R.id.exchange_type_value);
        people = $(R.id.people);
        people_name = $(R.id.people_name);
        reference_number = $(R.id.reference_number);
        reference_number_value = $(R.id.reference_number_value);
        root1 = $(R.id.root1);
        root2 = $(R.id.root2);
        root3 = $(R.id.root3);
        root4 = $(R.id.root4);

    }

//
//    //对外提供方法
//    public void setOrderInfo(OTCOrderInfoBean orderInfo) {
//
//        if (orderInfo != null) updateUi(orderInfo);
//
//
//    }
//
//    private void updateUi(OTCOrderInfoBean orderInfoBean) {
//
//        //买家还是卖家
//        if(orderInfoBean!=null&&orderInfoBean.getType()==1){//买家
//            switch (orderInfoBean.getState()){//买家下 分各种状态 ui显示不一样
//                case 1://订单状态1
//                break;
//                case 2://订单状态2
//                    break;
//                case 3://订单状态3
//                    break;
//                case 4://订单状态4
//                    break;
//            }
//
//        }else if(orderInfoBean!=null&&orderInfoBean.getType()==2){//卖家
//
//            switch (orderInfoBean.getState()){//买家下 分各种状态 ui显示不一样
//                case 1://订单状态1
//                    break;
//                case 2://订单状态2
//                    break;
//                case 3://订单状态3
//                    break;
//                case 4://订单状态4
//                    break;
//            }
//
//        }
//
//    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
//        OTCGetOtcOrdersBean otcGetOtcOrdersBean = new OTCGetOtcOrdersBean();
//        otcGetOtcOrdersBean.setStatus(0);
//        otcGetOtcOrdersBean.setDirection(2);
//        onOrderChanged(otcGetOtcOrdersBean);
    }

    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    static final String pattern = "(?<time>\\d{2}:\\d{2}):\\d{2}$";
    @Subscriber(tag = EventTags.TAGS_STATUS_CHANGE)
    public void onOrderChanged(OTCGetOtcOrdersBean bean) {
        ordervalue.setText(bean.getOrderNo());
//        String time = RegexUtils.getReplaceAll(bean.getCreateTime(), pattern, "${time}");
        createtime.setText(ChatMessage.getFormatTime(bean.getCreateTimestamp()));
        boolean isBuy = bean.getDirection() == 1;

        buy_or_sell.setText(isBuy ? getContext().getString(R.string.otc_buy_in) : getContext().getString(R.string.otc_sell_out));
        number.setText(String.format("%s %s", String.valueOf(Utils.toSubStringDegist(bean.getNum(), 4)), bean.getCoinCode().toUpperCase(Locale.ENGLISH)));

        exchange_type_name.setText(isBuy ? getContext().getString(R.string.otc_payment) : getContext().getString(R.string.otc_gathering));
        exchange_type_value.setText(String.format("%s %s", Utils.toSubStringDegist(bean.getAmount(), 2), bean.getLocalCurrencyName().toUpperCase(Locale.ENGLISH)));


        int status = bean.getStatus();
        if (status == OTCOrderStatus.Finish
                || (status == OTCOrderStatus.Cancel && isBuy)
                || (status == OTCOrderStatus.CustomerPayCoin && isBuy)
                || (status == OTCOrderStatus.CustomerRefund && isBuy)) {
            root3.setVisibility(VISIBLE);
            people.setText(isBuy ? getContext().getString(R.string.otc_seller) : getContext().getString(R.string.otc_buyer));
            people_name.setText(isBuy ? bean.getAcceptantName() : bean.getUserName());
        } else {
            root3.setVisibility(GONE);
        }

        if (isBuy) {
            if (status == OTCOrderStatus.Finish) {
                root4.setVisibility(VISIBLE);
                reference_number_value.setText(bean.getPayCertificateNO());
            } else {
                root4.setVisibility(GONE);
            }
        } else {
            if (status == OTCOrderStatus.Cancel) {
                root4.setVisibility(GONE);
            } else {
                reference_number_value.setText(bean.getPayCertificateNO());
                root4.setVisibility(VISIBLE);
            }
        }
    }


    public <T extends View> T $(@IdRes int resId) {
        return (T) root.findViewById(resId);
    }

}
