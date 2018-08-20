package foxidcw.android.idcw.otc.widgets.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.adapter.OTCSelectPaymentMethodAdapter;
import foxidcw.android.idcw.otc.model.beans.OTCGetPaymentModeManagementResBean;
import razerdp.basepopup.BasePopupWindow;

public class OTCSelectPaymentMethodPopup extends BasePopupWindow implements View.OnClickListener {

    private TextView mPopupPaymentTitleTv; // 标题
    private ImageView mPopupPaymentCloseIv; // 关闭
    private RecyclerView mPopupPaymentItemsRv; // 列表
    private OTCSelectPaymentMethodAdapter mOTCSelectPaymentMethodAdapter; // 适配器
    private Button mPopupPaymentSureBtn; // 确定
    private OnPaymentMethodSelectListener mOnPaymentMethodSelectListener; // 回调事件
    private boolean isMultiSelect = false; // 是否是多选
    private boolean oldSelectCanCancel = true; // 历史选择的是否可以取消
    private boolean newSelectCanCancel = true; // 新添加的能否取消
    private boolean isBtnDefaultGray = true; // 按钮是否为默认灰色
    private boolean isEnable = false; // 按钮图标是否可点击
    private HashSet<Integer> mOldSelect = new HashSet<>(); // 历史选中条目
    private HashSet<Integer> mNewSelect = new HashSet<>(); // 新选择的条目
    private boolean isShowSureBtn = true;

    public OTCSelectPaymentMethodPopup(Context context) {
        super(context);
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return mPopupPaymentCloseIv;
    }

    @Override
    public View onCreatePopupView() {
        View rootView = createPopupById(R.layout.popup_otc_selete_payment);
        mPopupPaymentTitleTv = rootView.findViewById(R.id.popup_select_payment_title_tv);
        mPopupPaymentCloseIv = rootView.findViewById(R.id.popup_select_payment_close_iv);
        mPopupPaymentItemsRv = rootView.findViewById(R.id.popup_select_payment_items_rv);
        mPopupPaymentItemsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mOTCSelectPaymentMethodAdapter = new OTCSelectPaymentMethodAdapter();
        mPopupPaymentItemsRv.setAdapter(mOTCSelectPaymentMethodAdapter);
        mPopupPaymentItemsRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse item = (OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse) adapter.getItem(position);
                // 多选
                if (isMultiSelect) {
                    // 判断旧的是否可以取消
                    if (oldSelectCanCancel && mOldSelect.contains(item.getID())) {
                        // 如果设置旧的不可以取消  并且旧的里面包含它 那么不做处理
                        return;
                    } else {
                        // 如果是可以多选的  并且不是默认选中的那么可以自由操作
                        mOTCSelectPaymentMethodAdapter.addSelect(item.getID());
                    }
                    mOTCSelectPaymentMethodAdapter.notifyDataSetChanged();
                    changeButtonBg();
                } else {
                    if (isShowSureBtn) {
                        // 单选直接可以选中和取消
                        mOTCSelectPaymentMethodAdapter.getSelects().clear();
                        mOTCSelectPaymentMethodAdapter.addSelect(item.getID());

                        mOTCSelectPaymentMethodAdapter.notifyDataSetChanged();
                        changeButtonBg();
                    } else {
                        /**
                         * 单选的点击直接返回
                         */
                        dismiss();
                        if (null != mOnPaymentMethodSelectListener) {
                            ArrayList<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> arrayList = new ArrayList<>();
                            arrayList.add(item);
                            mOnPaymentMethodSelectListener.onPaymentMethodSelected(arrayList);
                        }
                    }
                }
            }
        });
        mPopupPaymentSureBtn = rootView.findViewById(R.id.popup_select_payment_confirm_btn);
        mPopupPaymentSureBtn.setOnClickListener(this);
        return rootView;
    }

    /**
     * 显示弹框并且设置数据
     *
     * @param datas
     * @param popupTitle
     */
    public void showSelectPaymentWithData(List<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> datas, String popupTitle) {
        if (!TextUtils.isEmpty(popupTitle)) {
            mPopupPaymentTitleTv.setText(popupTitle);
        }

        if (datas != null) {
            //超过四行，限制高度
            if (datas.size() > 3)
                //320是由item_layout计算出来 10*6(四排中间的间隔)+10*2(上下间隔)+62*4(四排高度)
                mPopupPaymentItemsRv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getContext().getResources().getDimensionPixelSize(R.dimen.dp1) * 200));
            mOTCSelectPaymentMethodAdapter.setNewData(datas);
        }
        changeButtonBg();
        showPopupWindow();
        // 设置简单动画
        //mPopupPaymentItemsRv.setLayoutAnimation(OTCGridBottomInAnimUtils.initAnimationController());
    }

    /**
     * 检测并改变按钮的颜色
     */
    private void changeButtonBg() {
        // 单选的时候隐藏按钮  多选的时候 显示按钮
        mPopupPaymentSureBtn.setVisibility(isShowSureBtn ? View.VISIBLE : View.GONE);
        mPopupPaymentSureBtn.setEnabled(mOTCSelectPaymentMethodAdapter.getSelects().size() > 0);
    }

    /**
     * 显示弹框并且设置默认选中
     *
     * @param datas
     * @param popupTitle
     */
    public void showSelectPaymentWithData(List<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> datas, String popupTitle, HashSet<Integer> selects) {
        mOldSelect.clear();
        if (null != selects) {
            mOldSelect.addAll(selects);
            mOTCSelectPaymentMethodAdapter.setmMultiSelects(selects);
        }
        showSelectPaymentWithData(datas, popupTitle);
    }

    // 设置点击事件
    public void setOnPaymentMethodSelectListener(OnPaymentMethodSelectListener onPaymentMethodSelectListener) {
        this.mOnPaymentMethodSelectListener = onPaymentMethodSelectListener;
    }

    public OTCSelectPaymentMethodPopup setMultiEnable(boolean isMultiSelect) {
        this.isMultiSelect = isMultiSelect;
        return this;
    }

    public OTCSelectPaymentMethodPopup setShowSureButtonEnable(boolean isShowSureBtn) {
        this.isShowSureBtn = isShowSureBtn;
        return this;
    }

    @Override
    public View initAnimaView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.popup_select_payment_confirm_btn) {
            dismiss();
            if (null != mOnPaymentMethodSelectListener) {
                mOnPaymentMethodSelectListener.onPaymentMethodSelected(getCurrentSelected(mOTCSelectPaymentMethodAdapter.getSelects()));
            }
        }
    }

    private ArrayList<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> getCurrentSelected(HashSet<Integer> selects) {
        ArrayList<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> results = new ArrayList<>();
        for (OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse result : mOTCSelectPaymentMethodAdapter.getData()) {
            if (selects.contains(result.getID())) {
                results.add(result);
            }
        }
        return results;
    }

    public interface OnPaymentMethodSelectListener {
        void onPaymentMethodSelected(ArrayList<OTCGetPaymentModeManagementResBean.PaytypeLinkageResponse> selectItems);
    }
}
