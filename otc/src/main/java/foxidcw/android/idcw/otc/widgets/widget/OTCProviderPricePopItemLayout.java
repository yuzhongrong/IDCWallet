package foxidcw.android.idcw.otc.widgets.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.simple.eventbus.EventBus;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.model.beans.OTCProviderBean;

/**
 * 设计理念：垃圾自回收模式
 * Created by yuzhongrong on 2018/5/4.
 * 此页面是交易详情订单显示页面控制订单显示包括{买家和卖家}
 */

public class OTCProviderPricePopItemLayout extends LinearLayout implements View.OnClickListener {

    private View root;
    private TextView username;//对方名称
    private TextView state;//订单状态[已报价，未报价]
    private TextView number;//买入还是卖数量
    private ImageView close;

    public OTCProviderPricePopItemLayout(Context context) {
        super(context);
        EventBus.getDefault().register(this);
        initView();
    }

    public OTCProviderPricePopItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        //解析全部布局
        root = LayoutInflater.from(getContext()).inflate(R.layout.pop_otc_provider_popwindow_item, this, true);
        //解析子布局
        username = $(R.id.provider_otc_username_tv);
        state = $(R.id.provider_otc_state_tv);
        number = $(R.id.provider_otc_quantity_tv);
        close = $(R.id.provider_otc_close_iv);
        close.setOnClickListener(this);
    }


    //对外提供方法
    public void setOrderInfo(OTCProviderBean bean) {

        if (bean != null) updateUi(bean);


    }


    private void updateUi(OTCProviderBean bean) {


    }


    public <T extends View> T $(@IdRes int resId) {
        return (T) root.findViewById(resId);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.provider_otc_close_iv) {//

        }


    }
}
