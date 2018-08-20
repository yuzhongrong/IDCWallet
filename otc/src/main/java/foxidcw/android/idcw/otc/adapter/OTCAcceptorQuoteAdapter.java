package foxidcw.android.idcw.otc.adapter;

import android.annotation.SuppressLint;
import android.util.SparseArray;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.widget.DonutProgress;

import java.util.List;

import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.adapter.viewholder.OTCTimerViewHolder;
import foxidcw.android.idcw.otc.model.beans.OTCNewOrderNoticeAcceptantBean;
import io.reactivex.disposables.Disposable;

public class OTCAcceptorQuoteAdapter extends BaseQuickAdapter<OTCNewOrderNoticeAcceptantBean, OTCTimerViewHolder> {

    private Disposable mSubscribe;
    //退出activity时关闭所有定时器，避免造成资源泄漏。
    private SparseArray<Disposable> countDownMap;
    private final int DIGIST = 4; //固定精度 4位

    public OTCAcceptorQuoteAdapter(List<OTCNewOrderNoticeAcceptantBean> datas) {
        super(R.layout.pop_otc_provider_popwindow_layout, datas);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void convert(OTCTimerViewHolder helper, OTCNewOrderNoticeAcceptantBean item) {

        if (item != null) {
            //用户名
            helper.setText(R.id.provider_otc_username_tv, item.getUserName()).addOnClickListener(R.id.provider_otc_close_iv);
            //倒计时
            if (item.getStatus() == 6) { //交易中不倒计时也不隐藏
                helper.setVisible(R.id.provider_otc_progress_iv, false);
            } else {
                helper.setVisible(R.id.provider_otc_progress_iv, true);
            }
            DonutProgress progress = helper.getView(R.id.provider_otc_progress_iv);
            final int maxDeadLineSecond = item.getMaxDeadLineSeconds();
            progress.setMax(maxDeadLineSecond); //倒计时的满值
            progress.setProgress(maxDeadLineSecond);
            //LogUtil.e("222222 + --?>     " + (maxDeadLineSecond - item.getDeadLineSeconds()));
            progress.setDonut_progress(String.valueOf((maxDeadLineSecond - item.getDeadLineSeconds())));
            progress.setText(String.valueOf(item.getDeadLineSeconds()) + "s");
            //将前一个缓存清除
            if (helper.mSubscribe != null) {
                helper.mSubscribe.dispose();
            }
            //数量
            if (item.getDirection() == 1) {
                helper.setTextColor(R.id.provider_otc_quantity_tv, mContext.getResources().getColor(R.color.color_2968B9)).setText(R.id.provider_otc_quantity_tv, mContext.getResources().getString(R.string.str_otc_single_buy) + " " + Utils.toSubStringDegist(item.getNum(), DIGIST) + item.getCoinCode().toUpperCase());
            } else {
                helper.setTextColor(R.id.provider_otc_quantity_tv, mContext.getResources().getColor(R.color.color_fe8730)).setText(R.id.provider_otc_quantity_tv, mContext.getResources().getString(R.string.str_otc_single_sell) + " " + Utils.toSubStringDegist(item.getNum(), DIGIST) + item.getCoinCode().toUpperCase());
            }

            //判断是否已报价
            //未报价才有删除按钮
            int status = item.getStatus();
            helper.getView(R.id.provider_otc_close_iv).setVisibility(View.GONE);
            switch (status) {
                case 1: //待确认 已报价
                    helper.setVisible(R.id.provider_otc_unquote_iv, false).setText(R.id.provider_otc_state_tv, mContext.getString(R.string.str_otc_quoted));
                    break;
                case 6: //交易中，请及时跟进
                    helper.setVisible(R.id.provider_otc_unquote_iv, false).setText(R.id.provider_otc_state_tv, mContext.getString(R.string.str_otc_in_the_transaction));
                    break;
                case 8: //通知承兑商 未报价
                    helper.setVisible(R.id.provider_otc_unquote_iv, true).setText(R.id.provider_otc_state_tv, mContext.getString(R.string.str_otc_no_quoted));
                    helper.getView(R.id.provider_otc_close_iv).setVisibility(View.VISIBLE);
                    break;
            }


            //点击条目跳转
            /*helper.getConvertView().setOnClickListener(v -> {
                if (item.getDirection() == 1 || item.getDirection() == 2) {
                    if (item.getDeadLineSeconds() == 0 && item.getStatus() != 6) { //倒计时为0s不跳转
                        return;
                    }
                    if (item.getStatus() == 1) { //待确认 已报价
                        ARouter.getInstance().build(OTCConstant.QUOTATION)
                                .withSerializable(OTCConstant.NOTICEORDERBEAN, item)
                                .navigation();
                    } else if (item.getStatus() == 6) { //交易中
                        ARouter.getInstance()
                                .build(OTCConstant.TRADE_DETAIL)
                                .withInt("order_id", item.getOrderID())
                                .navigation();
                    } else if (item.getStatus() == 8) { //未报价
                        ARouter.getInstance().build(OTCConstant.QUOTATION)
                                .withSerializable(OTCConstant.NOTICEORDERBEAN, item)
                                .navigation();
                    }
                }
            });*/

        }

    }


}
