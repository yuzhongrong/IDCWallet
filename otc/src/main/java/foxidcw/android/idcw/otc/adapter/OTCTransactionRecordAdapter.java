package foxidcw.android.idcw.otc.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.LoginUtils;
import com.cjwsc.idcm.Utils.TimeUtils;
import com.cjwsc.idcm.Utils.Utils;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.model.bean.providerbean.LoginStatus;

import java.util.List;

import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.model.beans.OTCGetOtcOrdersBean;

/**
 *
 * @project name : FoxIDCW1.0
 * @class name :   OTCTransactionRecordAdapter
 * @package name : foxidcw.android.idcw.otc.adapter
 * @author :       MayerXu10000@gamil.com
 * @date :         2018/5/3 14:57
 * @describe :     交易记录适配器
 *
 */
public class OTCTransactionRecordAdapter
        extends CommonAdapter<OTCGetOtcOrdersBean>
{

    private final int DIGIST = 4; //固定精度 4位

    public OTCTransactionRecordAdapter(Context context,
                                       int layoutResId,
                                       @Nullable List<OTCGetOtcOrdersBean> data)
    {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    public void commonconvert(BaseViewHolder helper, OTCGetOtcOrdersBean item) {
//        helper.setVisible(R.id.view_divider, helper.getAdapterPosition()==0);
        View view = helper.getView(R.id.view_divider);
        if(helper.getAdapterPosition()!=0){
            view.setVisibility(View.GONE);
        }else{
            view.setVisibility(View.VISIBLE);
        }

        helper.setVisible(R.id.view_divider_bottom, helper.getAdapterPosition()!=getItemCount()-1);
        if (item.getDirection() == 1) { //买单
            helper.setBackgroundRes(R.id.img_transaction_type, R.mipmap.icon_otc_buy)
                  .setTextColor(R.id.tv_coin_num,
                                mContext.getResources()
                                        .getColor(R.color.c_FF6448))
                  .setText(R.id.tv_coin_num, "+" + Utils.toSubStringDegist(item.getNum(), DIGIST) + " " + item.getCoinCode().toUpperCase());
        } else {//卖单
            helper.setBackgroundRes(R.id.img_transaction_type, R.mipmap.icon_otc_sell)
                  .setTextColor(R.id.tv_coin_num,
                                mContext.getResources()
                                        .getColor(R.color.c_1FC73A))
                  .setText(R.id.tv_coin_num, "-" + Utils.toSubStringDegist(item.getNum(), DIGIST)  + " " + item.getCoinCode().toUpperCase());
        }

        LoginStatus bean = LoginUtils.getLoginBean(mContext);
        if(bean != null){
            if(bean.getUser_name().equals(item.getUserName())){
                helper.setText(R.id.tv_username, item.getAcceptantName() != null ? item.getAcceptantName() : "----");
            }else {
                helper.setText(R.id.tv_username, item.getUserName() != null ? item.getUserName() : "----");
            }
        }

//        helper.setText(R.id.tv_otc_time, item.getCreateTime().length() > 17 ? item.getCreateTime().substring(0, 16) : item.getCreateTime()); //截取掉秒
        //改用时间戳
        helper.setText(R.id.tv_otc_time, TimeUtils.getFormatTimeyyyyMMddHHmm(item.getCreateTimestamp())); //截取掉秒

        //订单状态
        switch (item.getStatus()) {
            case 0: //待报价 （没用到的状态）
                helper.setText(R.id.tv_order_statu,
                               mContext.getString(R.string.str_otc_order_statu_waiting_for_offer));
                break;
            case 1: //进行中 -(待转账 )
                helper.setText(R.id.tv_order_statu,
                               mContext.getString(R.string.str_otc_order_statu_processing));
                break;
            case 2://已转账 -("已转账")
                helper.setText(R.id.tv_order_statu,
                               mContext.getString(R.string.str_otc_order_statu_transferred));
                break;
            case 3://已转账 -("已转账") 余宗容说的
                helper.setText(R.id.tv_order_statu,
                               mContext.getString(R.string.str_otc_order_statu_transferred));
                break;
            case 4://申诉中 -("申诉中")
                helper.setText(R.id.tv_order_statu,
                               mContext.getString(R.string.str_otc_order_statu_appeal));
                break;
            case 5://上传凭证  -("待上传支付凭证")
                helper.setText(R.id.tv_order_statu,
                               mContext.getString(R.string.str_otc_order_statu_upload_certificate));
                break;
            case 6: //等待审核 -("待审核")
                helper.setText(R.id.tv_order_statu,
                               mContext.getString(R.string.str_otc_order_statu_moderated));
                break;
            case 7: //审核退回 -("客服退回")
                helper.setText(R.id.tv_order_statu,
                               mContext.getString(R.string.str_otc_order_statu_review_returned));
                break;
            case 8://审核放币 -("客服放币")
                helper.setText(R.id.tv_order_statu,
                               mContext.getString(R.string.str_otc_order_statu_currency_release));
                break;
            case 9://已取消（取消）
                helper.setText(R.id.tv_order_statu,
                               mContext.getString(R.string.str_otc_order_statu_cancelled));
                break;
            case 10: //待退币
                helper.setText(R.id.tv_order_statu,
                               mContext.getString(R.string.str_otc_order_statu_waiting_for_refundr));
                break;
            case 11: //同意退还
                helper.setText(R.id.tv_order_statu,
                               mContext.getString(R.string.str_otc_order_statu_agreed_to_refund));
                break;
            case 12://已完成 ("完成")
                helper.setText(R.id.tv_order_statu,
                               mContext.getString(R.string.str_otc_order_statu_completed));
                break;
        }

    }
}
