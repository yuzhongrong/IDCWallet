package foxidcw.android.idcw.otc.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.simple.eventbus.EventBus;

import java.util.List;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.common.utils.CommonAnimUtils;
import foxidcw.android.idcw.common.utils.CommonLayoutAnimationHelper;
import foxidcw.android.idcw.common.utils.StringUtils;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCGetCoinListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCDepositWaterBean;
import foxidcw.android.idcw.otc.model.params.OTCDepositWaterParams;

/**
 * Created by hpz on 2018/5/5.
 */

@Route(path = OTCConstant.DEPOSITWATER, name = "保证金流水详情页面")
public class OTCDepositWaterActivity extends BaseWalletActivity implements View.OnClickListener {
    private LinearLayout mMrBackLayout;
    /**
     * title
     */
    private TextView mTvSetName;
    private RecyclerView mRvDepositWater;
    @Autowired(name = "depositWater")
    String depositWater = "BTC";
    @Autowired(name = "ID")
    int id = 1;
    @Autowired(name = "waterTag")
    int waterTag = 1;
    private CommonAdapter commonAdapter;
    private int pageIndex = 1;
    private int pageSize = 12;

    @Autowired
    OTCGetCoinListProviderServices otcGetCoinListProviderServices;
    private SmartRefreshLayout mSmartrefreshlayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deposit_water;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        mMrBackLayout = (LinearLayout) findViewById(R.id.mr_back_layout);
        mTvSetName = (TextView) findViewById(R.id.tv_set_Name);
        mRvDepositWater = (RecyclerView) findViewById(R.id.rv_deposit_water);
        mSmartrefreshlayout = (SmartRefreshLayout) findViewById(R.id.smartrefreshlayout);
        mTvSetName.setText(depositWater.toUpperCase() + getString(R.string.str_otc_deposit_recharge_water));
        mMrBackLayout.setOnClickListener(this);
        initCurrRecycler();
    }

    private void initCurrRecycler() {
        commonAdapter = new CommonAdapter<OTCDepositWaterBean>(R.layout.item_deposit_water_list) {
            @Override
            public void commonconvert(BaseViewHolder helper, OTCDepositWaterBean item) {
//                String reqResult = "123,456,789";
//                String[] str1 = reqResult.split(",");
//                LogUtil.e("截取==",str1[0]+"=="+str1[1]+"=="+str1[2]+"=="+str1.length);
                switch (item.getBookTypeCode()) {
                    case "In"://充值
                        ((TextView) helper.getView(R.id.tv_order_describe)).setText(getString(R.string.str_otc_recharge));
                        ((TextView) helper.getView(R.id.tv_order_free)).setText("+" + StringUtils.subZeroAndDot(Utils.toSubStringDegist(item.getChangeBalance(), 20)) + " " + item.getCoinCode().toUpperCase() + "");
                        ((TextView) helper.getView(R.id.tv_order_free)).setTextColor(getResources().getColor(R.color.c_FF6348));
                        ((TextView) helper.getView(R.id.tv_order_time)).setText(item.getCreateTime());
                        ((TextView) helper.getView(R.id.tv_order_balance)).setText(getString(R.string.can_send_balance) + Utils.toSubStringDegist(item.getBalance(), 4) + "" + " " + item.getCoinCode().toUpperCase());
                        ((LinearLayout) helper.getView(R.id.ll_about_order_layout)).setVisibility(View.GONE);
                        GlideUtil.loadImageView(mContext, item.getBookTypeLogo(), (ImageView) helper.getView(R.id.img_title));
                        break;
                    case "Out"://提取
                        ((TextView) helper.getView(R.id.tv_order_describe)).setText(getString(R.string.str_otc_withdraw));
                        ((TextView) helper.getView(R.id.tv_order_free)).setText(StringUtils.subZeroAndDot(Utils.toSubStringDegist(item.getChangeBalance(), 20)) + " " + item.getCoinCode().toUpperCase() + "");
                        ((TextView) helper.getView(R.id.tv_order_free)).setTextColor(getResources().getColor(R.color.c_1FC73A));
                        ((TextView) helper.getView(R.id.tv_order_time)).setText(item.getCreateTime());
                        ((TextView) helper.getView(R.id.tv_order_balance)).setText(getString(R.string.can_send_balance) + Utils.toSubStringDegist(item.getBalance(), 4) + "" + " " + item.getCoinCode().toUpperCase());
                        ((LinearLayout) helper.getView(R.id.ll_about_order_layout)).setVisibility(View.GONE);
                        GlideUtil.loadImageView(mContext, item.getBookTypeLogo(), (ImageView) helper.getView(R.id.img_title));
                        break;
                    case "Appeal"://申诉违约金
                        if (item.getRelateOrderNo()==null)return;
                        ((TextView) helper.getView(R.id.tv_order_describe)).setText(getString(R.string.str_otc_apply_damages));
                        ((TextView) helper.getView(R.id.tv_order_free)).setText(StringUtils.subZeroAndDot(Utils.toSubStringDegist(item.getChangeBalance(), 20)) + " " + item.getCoinCode().toUpperCase() + "");
                        ((TextView) helper.getView(R.id.tv_order_free)).setTextColor(getResources().getColor(R.color.c_1FC73A));
                        ((TextView) helper.getView(R.id.tv_order_time)).setText(item.getCreateTime());
                        ((TextView) helper.getView(R.id.tv_order_balance)).setText(getString(R.string.can_send_balance) + Utils.toSubStringDegist(item.getBalance(), 4) + "" + " " + item.getCoinCode().toUpperCase());
                        ((LinearLayout) helper.getView(R.id.ll_about_order_layout)).setVisibility(View.VISIBLE);
                        GlideUtil.loadImageView(mContext, item.getBookTypeLogo(), (ImageView) helper.getView(R.id.img_title));
                        if ((!TextUtils.isEmpty(item.getRelateOrderNo())
                                && item.getRelateOrderNo().contains(","))
                                &&(!TextUtils.isEmpty(item.getRelateOrderId())
                                && item.getRelateOrderId().contains(","))) {//如果订单号不为空并且有2个或者3个以上并且订单id不为空并且有2个或者3个以上
                            String[] strSplit = item.getRelateOrderNo().split(",");
                            String[] strSplitId = item.getRelateOrderId().split(",");
                            switch (strSplit.length) {
                                case 2:
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.GONE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setText(strSplit[0]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setText(strSplit[1]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[0]))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[1]))
                                                .navigation(mCtx);
                                    });
                                    break;
                                case 3:
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setText(strSplit[0]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setText(strSplit[1]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setText(strSplit[2]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[0]))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[1]))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[2]))
                                                .navigation(mCtx);
                                    });
                                    break;
                            }
                        } else if (((!TextUtils.isEmpty(item.getRelateOrderNo()) && item.getRelateOrderNo().contains(","))&&(TextUtils.isEmpty(item.getRelateOrderId())))
                                ||((!TextUtils.isEmpty(item.getRelateOrderNo()) && item.getRelateOrderNo().contains(","))&&(!TextUtils.isEmpty(item.getRelateOrderId())))) {
                            //如果订单号不为空并且有2个或者3个以上并且订单id是空的
                            //如果订单号不为空并且有2个或者3个以上但是订单id不为空，只有一个的
                            String[] strSplit = item.getRelateOrderNo().split(",");
                            switch (strSplit.length) {
                                case 2:
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.GONE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setText(strSplit[0]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setText(strSplit[1]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    break;
                                case 3:
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setText(strSplit[0]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setText(strSplit[1]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setText(strSplit[2]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    break;
                            }
                        }else if (!TextUtils.isEmpty(item.getRelateOrderNo())
                                ||TextUtils.isEmpty(item.getRelateOrderId())){//如果订单id不为空，以上条件不成立说明只有一个订单
                            String OrderNum = item.getRelateOrderNo();
                            String OrderNumId = item.getRelateOrderId();
                            ((TextView) helper.getView(R.id.ll_about_order_number)).setText(OrderNum);
                            ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                            ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.GONE);
                            ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.GONE);
                            ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                if (item.getRelateOrderId() == null) return;
                                ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                        .withInt("order_id", Integer.parseInt(OrderNumId))
                                        .navigation(mCtx);
                            });

                        }


                        break;
                    case "CancelOrder":
                        if (item.getRelateOrderNo()==null)return;
                        ((TextView) helper.getView(R.id.tv_order_describe)).setText(getString(R.string.str_otc_cancel_order_damages));
                        ((TextView) helper.getView(R.id.tv_order_free)).setText(StringUtils.subZeroAndDot(Utils.toSubStringDegist(item.getChangeBalance(), 20)) + " " + item.getCoinCode().toUpperCase() + "");
                        ((TextView) helper.getView(R.id.tv_order_free)).setTextColor(getResources().getColor(R.color.c_1FC73A));
                        ((TextView) helper.getView(R.id.tv_order_time)).setText(item.getCreateTime());
                        ((TextView) helper.getView(R.id.tv_order_balance)).setText(getString(R.string.can_send_balance) + Utils.toSubStringDegist(item.getBalance(), 4) + "" + " " + item.getCoinCode().toUpperCase());
                        ((LinearLayout) helper.getView(R.id.ll_about_order_layout)).setVisibility(View.VISIBLE);
                        GlideUtil.loadImageView(mContext, item.getBookTypeLogo(), (ImageView) helper.getView(R.id.img_title));
                        if (((!TextUtils.isEmpty(item.getRelateOrderNo())
                                && item.getRelateOrderNo().contains(","))
                                &&(!TextUtils.isEmpty(item.getRelateOrderId())
                                && item.getRelateOrderId().contains(",")))) {
                            String[] strSplit = item.getRelateOrderNo().split(",");
                            String[] strSplitId = item.getRelateOrderId().split(",");
                            switch (strSplit.length) {
                                case 2:
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.GONE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setText(strSplit[0]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setText(strSplit[1]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[0]))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[1]))
                                                .navigation(mCtx);
                                    });
                                    break;
                                case 3:
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setText(strSplit[0]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setText(strSplit[1]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setText(strSplit[2]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[0]))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[1]))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[2]))
                                                .navigation(mCtx);
                                    });
                                    break;
                            }
                        } else if (((!TextUtils.isEmpty(item.getRelateOrderNo()) && item.getRelateOrderNo().contains(","))&&(TextUtils.isEmpty(item.getRelateOrderId())))
                                ||((!TextUtils.isEmpty(item.getRelateOrderNo()) && item.getRelateOrderNo().contains(","))&&(!TextUtils.isEmpty(item.getRelateOrderId())))) {
                            String[] strSplit = item.getRelateOrderNo().split(",");
                            switch (strSplit.length) {
                                case 2:
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.GONE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setText(strSplit[0]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setText(strSplit[1]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    break;
                                case 3:
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setText(strSplit[0]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setText(strSplit[1]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setText(strSplit[2]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    break;
                            }
                        } else if (!TextUtils.isEmpty(item.getRelateOrderNo())||TextUtils.isEmpty(item.getRelateOrderId())){
                            String OrderNum = item.getRelateOrderNo();
                            String OrderNumId = item.getRelateOrderId();
                            ((TextView) helper.getView(R.id.ll_about_order_number)).setText(OrderNum);
                            ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                            ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.GONE);
                            ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.GONE);
                            ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                if (item.getRelateOrderId() == null) return;
                                ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                        .withInt("order_id", Integer.parseInt(OrderNumId))
                                        .navigation(mCtx);
                            });
                        }
                        break;
                    case "Buy":
                        if (item.getRelateOrderNo()==null)return;
                        ((TextView) helper.getView(R.id.tv_order_describe)).setText(getString(R.string.str_otc_buy_free));
                        ((TextView) helper.getView(R.id.tv_order_free)).setText(StringUtils.subZeroAndDot(Utils.toSubStringDegist(item.getChangeBalance(), 20)) + " " + item.getCoinCode().toUpperCase() + "");
                        ((TextView) helper.getView(R.id.tv_order_free)).setTextColor(getResources().getColor(R.color.c_1FC73A));
                        ((TextView) helper.getView(R.id.tv_order_time)).setText(item.getCreateTime());
                        ((TextView) helper.getView(R.id.tv_order_balance)).setText(getString(R.string.can_send_balance) + Utils.toSubStringDegist(item.getBalance(), 4) + "" + " " + item.getCoinCode().toUpperCase());
                        ((LinearLayout) helper.getView(R.id.ll_about_order_layout)).setVisibility(View.VISIBLE);
                        GlideUtil.loadImageView(mContext, item.getBookTypeLogo(), (ImageView) helper.getView(R.id.img_title));
                        if ((!TextUtils.isEmpty(item.getRelateOrderNo()) && item.getRelateOrderNo().contains(","))&&(!TextUtils.isEmpty(item.getRelateOrderId()) && item.getRelateOrderId().contains(","))) {
                            String[] strSplit = item.getRelateOrderNo().split(",");
                            String[] strSplitId = item.getRelateOrderNo().split(",");
                            switch (strSplit.length) {
                                case 2:
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.GONE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setText(strSplit[0]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setText(strSplit[1]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[0]))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[1]))
                                                .navigation(mCtx);
                                    });
                                    break;
                                case 3:
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setText(strSplit[0]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setText(strSplit[1]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setText(strSplit[2]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[0]))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[1]))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id",Integer.parseInt(strSplitId[2]))
                                                .navigation(mCtx);
                                    });
                                    break;
                            }
                        } else if (((!TextUtils.isEmpty(item.getRelateOrderNo()) && item.getRelateOrderNo().contains(","))&&(TextUtils.isEmpty(item.getRelateOrderId())))
                                ||((!TextUtils.isEmpty(item.getRelateOrderNo()) && item.getRelateOrderNo().contains(","))&&(!TextUtils.isEmpty(item.getRelateOrderId())))) {
                            String[] strSplit = item.getRelateOrderNo().split(",");
                            switch (strSplit.length) {
                                case 2:
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.GONE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setText(strSplit[0]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setText(strSplit[1]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    break;
                                case 3:
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setText(strSplit[0]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setText(strSplit[1]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setText(strSplit[2]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    break;
                            }
                        }else if (!TextUtils.isEmpty(item.getRelateOrderNo())||TextUtils.isEmpty(item.getRelateOrderId())){
                            String OrderNum = item.getRelateOrderNo();
                            String OrderNumId = item.getRelateOrderId();
                            ((TextView) helper.getView(R.id.ll_about_order_number)).setText(OrderNum);
                            ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                            ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.GONE);
                            ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.GONE);
                            ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                if (item.getRelateOrderId() == null) return;
                                ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                        .withInt("order_id", Integer.parseInt(OrderNumId))
                                        .navigation(mCtx);
                            });
                        }
                        break;
                    case "Sell":
                        if (item.getRelateOrderNo()==null)return;
                        ((TextView) helper.getView(R.id.tv_order_describe)).setText(getString(R.string.str_otc_sell_free));
                        ((TextView) helper.getView(R.id.tv_order_free)).setText(StringUtils.subZeroAndDot(Utils.toSubStringDegist(item.getChangeBalance(), 20)) + " " + item.getCoinCode().toUpperCase() + "");
                        ((TextView) helper.getView(R.id.tv_order_free)).setTextColor(getResources().getColor(R.color.c_1FC73A));
                        ((TextView) helper.getView(R.id.tv_order_time)).setText(item.getCreateTime());
                        ((TextView) helper.getView(R.id.tv_order_balance)).setText(getString(R.string.can_send_balance) + Utils.toSubStringDegist(item.getBalance(), 4) + "" + " " + item.getCoinCode().toUpperCase());
                        ((LinearLayout) helper.getView(R.id.ll_about_order_layout)).setVisibility(View.VISIBLE);
                        GlideUtil.loadImageView(mContext, item.getBookTypeLogo(), (ImageView) helper.getView(R.id.img_title));
                        if ((!TextUtils.isEmpty(item.getRelateOrderNo()) && item.getRelateOrderNo().contains(","))&&(!TextUtils.isEmpty(item.getRelateOrderId()) && item.getRelateOrderId().contains(","))) {
                            String[] strSplit = item.getRelateOrderNo().split(",");
                            String[] strSplitId = item.getRelateOrderId().split(",");
                            switch (strSplit.length) {
                                case 2:
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.GONE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setText(strSplit[0]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setText(strSplit[1]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[0]))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[1]))
                                                .navigation(mCtx);
                                    });
                                    break;
                                case 3:
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setText(strSplit[0]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setText(strSplit[1]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setText(strSplit[2]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[0]))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[1]))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(strSplitId[2]))
                                                .navigation(mCtx);
                                    });

                                    break;
                            }
                        } else if (((!TextUtils.isEmpty(item.getRelateOrderNo()) && item.getRelateOrderNo().contains(","))&&(TextUtils.isEmpty(item.getRelateOrderId())))
                                ||((!TextUtils.isEmpty(item.getRelateOrderNo()) && item.getRelateOrderNo().contains(","))&&(!TextUtils.isEmpty(item.getRelateOrderId())))) {
                            String[] strSplit = item.getRelateOrderNo().split(",");
                            switch (strSplit.length) {
                                case 2:
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.GONE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setText(strSplit[0]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setText(strSplit[1]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    break;
                                case 3:
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.VISIBLE);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setText(strSplit[0]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setText(strSplit[1]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setText(strSplit[2]);
                                    ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_two)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    ((TextView) helper.getView(R.id.ll_about_order_number_three)).setOnClickListener(v -> {
                                        if (item.getRelateOrderId() == null) return;
                                        ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                                .withInt("order_id", Integer.parseInt(item.getRelateOrderId()))
                                                .navigation(mCtx);
                                    });
                                    break;
                            }
                        }else if (!TextUtils.isEmpty(item.getRelateOrderNo())||TextUtils.isEmpty(item.getRelateOrderId())){
                            String OrderNum = item.getRelateOrderNo();
                            String OrderNumId = item.getRelateOrderId();
                            ((TextView) helper.getView(R.id.ll_about_order_number)).setText(OrderNum);
                            ((TextView) helper.getView(R.id.ll_about_order_number)).setVisibility(View.VISIBLE);
                            ((TextView) helper.getView(R.id.ll_about_order_number_two)).setVisibility(View.GONE);
                            ((TextView) helper.getView(R.id.ll_about_order_number_three)).setVisibility(View.GONE);
                            ((TextView) helper.getView(R.id.ll_about_order_number)).setOnClickListener(v -> {
                                if (item.getRelateOrderId() == null) return;
                                ARouter.getInstance().build(OTCConstant.TRADE_DETAIL)
                                        .withInt("order_id", Integer.parseInt(OrderNumId))
                                        .navigation(mCtx);
                            });
                        }
                        break;
                }

//                helper.getConvertView().setOnClickListener(v -> {
//
//                });
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvDepositWater.setLayoutManager(linearLayoutManager);
        mRvDepositWater.setAdapter(commonAdapter);
        commonAdapter.setEmptyView(R.layout.recyclerview_water_empty_layout, mRvDepositWater);

        mSmartrefreshlayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageIndex += 1;
                requestDepositWater(pageIndex, pageSize, true, false);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                requestDepositWater(pageIndex, pageSize, false, false);
            }
        });

    }

    @Override
    protected void onEvent() {
        pageIndex = 1;
        requestDepositWater(pageIndex, pageSize, false, true);
    }

    private void requestDepositWater(int pageIndex, int pageSize, boolean isloadmore, boolean isShowDialog) {
        LoginStatus bean = LoginUtils.getLoginBean(this);
        otcGetCoinListProviderServices.requestOTCDepositWaterList(new OTCDepositWaterParams(pageIndex, pageSize, id, bean.getId()))
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeWith(new RxProgressSubscriber<List<OTCDepositWaterBean>>(OTCDepositWaterActivity.this, isShowDialog) {
                    @Override
                    public void onSuccess(List<OTCDepositWaterBean> data) {
                        //commonAdapter.setNewData(data);
                        if (data != null && data.size() > 0) {
                            if (mSmartrefreshlayout == null) return;
                            mSmartrefreshlayout.resetNoMoreData();
                            if (isloadmore) {//加载更多
                                mSmartrefreshlayout.finishLoadmore();
                                commonAdapter.addData(data);
                            } else {//重新加载数据
                                mSmartrefreshlayout.finishRefresh();
                                commonAdapter.setNewData(data);
                                CommonAnimUtils.playCommonAllViewAnimation(mRvDepositWater, CommonLayoutAnimationHelper.getAnimationSetFromRight(), false);
                            }
                        } else {
                            onUpdateSmartRefresh(mSmartrefreshlayout);
                            mSmartrefreshlayout.finishLoadmoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
                        }
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        ToastUtil.show(getString(R.string.server_connection_error));
                        onUpdateSmartRefresh(mSmartrefreshlayout);
                        mSmartrefreshlayout.finishLoadmoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
                    }
                });
    }

    private void onUpdateSmartRefresh(SmartRefreshLayout smartRefreshLayout) {
        if (smartRefreshLayout != null) {
            if (smartRefreshLayout.isRefreshing()) {
                smartRefreshLayout.finishRefresh();
            } else if (smartRefreshLayout.isLoading()) {
                smartRefreshLayout.finishLoadmore();
            }
        }
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.mr_back_layout) {
            if (waterTag ==3){
                EventBus.getDefault().post(new PosInfo(167));
                this.finish();
            }else {
                this.finish();
            }
        } else {
        }
    }

}
