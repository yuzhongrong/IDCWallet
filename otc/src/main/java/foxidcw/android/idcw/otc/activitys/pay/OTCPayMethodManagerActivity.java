package foxidcw.android.idcw.otc.activitys.pay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.base.ui.widget.RecycleViewDivider;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.activitys.pay.adapter.OTCPayMethodManagerAdapter;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCPaymentMethodManagerPageServices;
import foxidcw.android.idcw.otc.model.beans.OTCRemoveBean;
import foxidcw.android.idcw.otc.model.params.OTCAddOrEditReqParams;
import foxidcw.android.idcw.otc.widgets.dialog.OTCDeleteCurrPayPopWindow;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static foxidcw.android.idcw.otc.constant.OTCConstant.PAY_METHOD_ADD_OR_EDIT;

@Route(path = OTCConstant.PAY_METHOD_MANAGER, name = "支付管理界面")
public class OTCPayMethodManagerActivity extends OTCPayTitleActivity {

    private static final int ADD_OR_EDIT_REQUEST_CODE = 104;
    private SwipeMenuRecyclerView mAllCardsRv;
    private OTCPayMethodManagerAdapter mAdapter;
    private List<OTCAddOrEditReqParams> mAllPayLists = new ArrayList<>();
    private LinearLayout mEmptyViewLL;

    @Autowired
    OTCPaymentMethodManagerPageServices mOTCPaymentMethodManagerPageServices;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otc_pay_method_manager);
        ARouter.getInstance().inject(this);

        mAllCardsRv = (SwipeMenuRecyclerView) $(R.id.pay_method_all_cards_rl);
        mEmptyViewLL = (LinearLayout) $(R.id.pay_method_empty_container_ll);

        mAdapter = new OTCPayMethodManagerAdapter(mAllPayLists);
        // 测试提交 22222
        mAllCardsRv.setLayoutManager(new LinearLayoutManager(mCtx));
        RecycleViewDivider divider = new RecycleViewDivider(mCtx, LinearLayoutManager.VERTICAL,
                (int) getResources().getDimension(R.dimen.dp12), getResources().getColor(R.color.transparent));
        mAllCardsRv.addItemDecoration(divider);
        mAllCardsRv.setSwipeMenuCreator(mMenuCreater);
        mAllCardsRv.setSwipeItemClickListener(swipeItemClickListener);
        mAllCardsRv.setSwipeMenuItemClickListener(swipeMenuItemClickListener);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(MATCH_PARENT, 12);
        View view = new View(mCtx);
        view.setLayoutParams(params);
        mAdapter.addHeaderView(view);
        mAllCardsRv.setAdapter(mAdapter);

        getAllPayMode();
    }


    @SuppressLint("CheckResult")
    private void getAllPayMode() {
        mOTCPaymentMethodManagerPageServices.getPaymentModeList()
                .compose(bindToLifecycle())
                .subscribeWith(new RxProgressSubscriber<List<OTCAddOrEditReqParams>>(this) {
                    @Override
                    public void onSuccess(List<OTCAddOrEditReqParams> otcAddOrEditReqParams) {
                        mAdapter.setNewData(otcAddOrEditReqParams);
                        mAdapter.notifyDataSetChanged();
                        //CommonAnimUtils.playCommonAllViewAnimation(mAllCardsRv, CommonLayoutAnimationHelper.getAnimationSetFromRight(), false);
                        setEmptyUI();
                        setResult(2, new Intent().putExtra("result", "refreshList"));
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {
                        setEmptyUI();
                    }
                });
    }

    private void setEmptyUI() {
        if (mAdapter.getData().size() <= 0) {
            mEmptyViewLL.setVisibility(View.VISIBLE);
            mAllCardsRv.setVisibility(View.GONE);
        } else {
            mEmptyViewLL.setVisibility(View.GONE);
            mAllCardsRv.setVisibility(View.VISIBLE);
        }
    }

    private SwipeMenuItemClickListener swipeMenuItemClickListener = new SwipeMenuItemClickListener() {
        @SuppressLint("CheckResult")
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            //menuBridge.closeMenu();
            new OTCDeleteCurrPayPopWindow(mCtx)
                    .setTitle(mCtx.getResources().getString(R.string.str_otc_sure_to_delete_payment_method))
                    .setContentTextSize(14)
                    .setCancelContent(mCtx.getResources().getString(R.string.cancel))
                    .setConfirmContent(mCtx.getResources().getString(R.string.tv_sure))
                    .setClickListener(type -> {
                        if (type == 1) {
                            int currentPosition = menuBridge.getAdapterPosition() - 1;
                            OTCAddOrEditReqParams item = mAdapter.getItem(currentPosition);
                            mOTCPaymentMethodManagerPageServices.
                                    paymentModeRemove(new OTCRemoveBean(item.getID()))
                                    .compose(bindToLifecycle())
                                    .subscribeWith(new RxProgressSubscriber<Object>(OTCPayMethodManagerActivity.this) {
                                        @Override
                                        public void onSuccess(Object data) {
                                            ToastUtil.show(mCtx.getResources().getString(R.string.str_otc_delete_payment_method_success));
                                            mAdapter.remove(currentPosition);
                                            mAdapter.notifyDataSetChanged();
                                            menuBridge.closeMenu();
                                            setEmptyUI();
                                        }

                                        @Override
                                        protected void onError(ResponseThrowable ex) {
                                            super.onError(ex);
                                            ToastUtil.show(ex.getErrorMsg());
                                        }
                                    });
                        }
                    })
                    .showPopupWindow();
        }
    };

    /**
     * 侧滑按钮点击事件
     */
    private SwipeItemClickListener swipeItemClickListener = new SwipeItemClickListener() {
        @Override
        public void onItemClick(View itemView, int position) {
            OTCAddOrEditReqParams item = mAdapter.getItem(position - 1);
            ARouter.getInstance()
                    .build(PAY_METHOD_ADD_OR_EDIT)
                    .withSerializable("payment_method_bean", item)
                    .navigation(OTCPayMethodManagerActivity.this, ADD_OR_EDIT_REQUEST_CODE);
        }
    };

    private SwipeMenuCreator mMenuCreater = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            SwipeMenuItem deleteItem = new SwipeMenuItem(mCtx); // 各种文字和图标属性设置。
            deleteItem.setText(getString(R.string.str_item_del));
            deleteItem.setTextColor(getResources().getColor(R.color.white));
            deleteItem.setBackground(R.drawable.otc_swipe_menu_del_bg);
            deleteItem.setHeight((int) getResources().getDimension(R.dimen.dp78));

            deleteItem.setWidth((int) getResources().getDimension(R.dimen.dp80));
            swipeRightMenu.addMenuItem(deleteItem); // 在Item左侧添加一个菜单。
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_OR_EDIT_REQUEST_CODE) {
            if (data != null) {
                getAllPayMode();
            }
        }
    }

    @Override
    protected String getTitleText() {
        return mCtx.getResources().getString(R.string.str_otc_set_pay_manager);
    }

    @Override
    protected int getRightRes() {
        return R.drawable.otc_icon_add;
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    protected void onRightIconClick() {
        ARouter.getInstance().build(PAY_METHOD_ADD_OR_EDIT).navigation(this, ADD_OR_EDIT_REQUEST_CODE);
    }

    @Override
    protected void onLeftIconClick() {
        Intent intent = new Intent();
        intent.putExtra("size", mAdapter != null ? mAdapter.getData().size() : 0);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        onLeftIconClick();
    }
}
