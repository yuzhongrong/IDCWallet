package com.idcg.idcw.adapter;

import android.content.Context;

import com.idcg.idcw.R;
import com.cjwsc.idcm.model.bean.CountryCodeBean;
import com.cjwsc.idcm.recylcerview.BaseRecItemClickedAdapter;
import com.cjwsc.idcm.recylcerview.RecViewHolder;

import java.util.List;


/**
 * Created by hpz on 2018/1/4.
 */

public class CountryCodeAdapter extends BaseRecItemClickedAdapter<CountryCodeBean.DataBeanX.DataBean> {
    /**
     * 构造函数
     *  @param context
     * @param datas
     * @param layoutId
     */
    public CountryCodeAdapter(Context context, List<CountryCodeBean.DataBeanX.DataBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    protected void convert(RecViewHolder holder, CountryCodeBean.DataBeanX.DataBean bean, int position) {
        holder.setText(R.id.tv_country_name,bean.getName());
        holder.setText(R.id.tv_count_code,bean.getAreacode());
    }
}
