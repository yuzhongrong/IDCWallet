package foxidcw.android.idcw.otc.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.BaseProgressView;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.google.gson.Gson;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.common.utils.CommonAnimUtils;
import foxidcw.android.idcw.common.utils.CommonLayoutAnimationHelper;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.constant.OTCConstant;
import foxidcw.android.idcw.otc.iprovider.OTCAddBuyCurrProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCGetCoinListProviderServices;
import foxidcw.android.idcw.otc.iprovider.OTCMoneyBagListProviderServices;
import foxidcw.android.idcw.otc.model.beans.OTCDepositMgListBean;
import foxidcw.android.idcw.otc.model.params.OTCDepositSortParams;
import foxidcw.android.idcw.otc.widgets.dialog.OTCOneBtnPopWindow;

/**
 * Created by hpz on 2018/5/4.
 */

@Route(path = OTCConstant.DEPOSITMANAGER, name = "承兑商保证金余额列表页面")
public class OTCDepositManagerActivity extends BaseWalletActivity implements View.OnClickListener {
    private LinearLayout mMrBackLayout;
    private TextView mTvSetName;
    private RelativeLayout mHeadLayout;
    private RecyclerView mRvDeposit;
    private TextView mBtnGetDeposit;
    private CommonAdapter commonAdapter;
    private View view2;
    private ItemTouchHelper itemTouchHelper;
    private List<OTCDepositMgListBean> sortCommitList = new ArrayList<>();
    private List<OTCDepositSortParams> sureSortCommitList = new ArrayList<>();
    private int moveTag = 1;

    @Autowired
    OTCGetCoinListProviderServices mGetExchangeCoinListProviderServices;

    @Autowired
    OTCAddBuyCurrProviderServices otcAddBuyCurrProviderServices;

    @Autowired
    OTCMoneyBagListProviderServices otcMoneyBagListProviderServices;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_de_manager;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        mMrBackLayout = (LinearLayout) findViewById(R.id.mr_back_layout);
        mTvSetName = (TextView) findViewById(R.id.tv_set_Name);
        mRvDeposit = (RecyclerView) findViewById(R.id.rv_deposit);
        mBtnGetDeposit = (TextView) findViewById(R.id.btn_get_deposit);
        mMrBackLayout.setOnClickListener(this);
        mBtnGetDeposit.setOnClickListener(this);

        mTvSetName.setText(getString(R.string.str_otc_deposit_manager));
        initCurrRecycler();
    }

    private void initCurrRecycler() {
        //view2 = View.inflate(mCtx, R.layout.tv_deposit_balance_layout, null);
        commonAdapter = new CommonAdapter<OTCDepositMgListBean>(R.layout.item_deposit_manager) {
            @Override
            public void commonconvert(BaseViewHolder helper, OTCDepositMgListBean item) {
                helper.setVisible(R.id.view_diver,commonAdapter.getData().size()!=1);
                GlideUtil.loadImageView(mContext, item.getLogo(), (ImageView) helper.getView(R.id.img_currency_icon));
                ((TextView) helper.getView(R.id.currency_name)).setText(item.getCoinCode().toUpperCase());
                ((TextView) helper.getView(R.id.currency_tag)).setText(item.getCoinName());

                helper.getConvertView().setOnClickListener(v -> {
                    ARouter.getInstance().build(OTCConstant.DEPOSITWATER)
                            .withString("depositWater", item.getCoinCode().toUpperCase())
                            .withInt("ID", item.getCoinId())
                            .navigation(mCtx);
                });
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvDeposit.setLayoutManager(linearLayoutManager);
        mRvDeposit.setAdapter(commonAdapter);
        LogUtil.e("commonAdapter==", commonAdapter.getData().size() + "");

    }

    ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {

        /**
         * 官方文档的说明如下：
         * o control which actions user can take on each view, you should override getMovementFlags(RecyclerView, ViewHolder)
         * and return appropriate set of direction flags. (LEFT, RIGHT, START, END, UP, DOWN).
         * 返回我们要监控的方向，上下左右，我们做的是上下拖动，要返回都是UP和DOWN
         * 关键坑爹的是下面方法返回值只有1个，也就是说只能监控一个方向。
         * 不过点入到源码里面有惊喜。源码标记方向如下：
         *  public static final int UP = 1     0001
         *  public static final int DOWN = 1 << 1; （位运算：值其实就是2）0010
         *  public static final int LEFT = 1 << 2   左 值是3
         *  public static final int RIGHT = 1 << 3  右 值是8
         */
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //也就是说返回值是组合式的
            //makeMovementFlags (int dragFlags, int swipeFlags)，看下面的解释说明
            int swipFlag = 0;
            //如果也监控左右方向的话，swipFlag=ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
            int dragflag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            //等价于：0001&0010;多点触控标记触屏手指的顺序和个数也是这样标记哦
            return makeMovementFlags(dragflag, swipFlag);

            /**
             * 备注：由getMovementFlags可以联想到setMovementFlags，不过文档么有这个方法，但是：
             * 有 makeMovementFlags (int dragFlags, int swipeFlags)
             * Convenience method to create movement flags.便捷方法创建moveMentFlag
             * For instance, if you want to let your items be drag & dropped vertically and swiped left to be dismissed,
             * you can call this method with: makeMovementFlags(UP | DOWN, LEFT);
             * 这个recyclerview的文档写的简直完美，示例代码都弄好了！！！
             * 如果你想让item上下拖动和左边滑动删除，应该这样用： makeMovementFlags(UP | DOWN, LEFT)
             */

            //拓展一下：如果只想上下的话：makeMovementFlags（UP | DOWN, 0）,标记方向的最小值1
        }


        /**
         * 官方文档的说明如下
         * If user drags an item, ItemTouchHelper will call onMove(recyclerView, dragged, target). Upon receiving this callback,
         * you should move the item from the old position (dragged.getAdapterPosition()) to new position (target.getAdapterPosition())
         * in your adapter and also call notifyItemMoved(int, int).
         * 拖动某个item的回调，在return前要更新item位置，调用notifyItemMoved（draggedPosition，targetPosition）
         * viewHolde:正在拖动item
         * target：要拖到的目标
         * @return true 表示消费事件
         */
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //直接按照文档来操作啊，这文档写得太给力了,简直完美！
            commonAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            //注意这里有个坑的，itemView 都移动了，对应的数据也要移动
            Collections.swap(commonAdapter.getData(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        //当完成拖曳手指松开的时候调用
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            //给已经完成拖曳的 item 恢复开始的背景。
            //这里我们设置的颜色尽量和你 item 在 xml 中设置的颜色保持一致
            //RequestAddCurrency();

            sortCommitList = commonAdapter.getData();

            for (int i = 0; i < sortCommitList.size(); i++) {
                OTCDepositSortParams depositSortParams = new OTCDepositSortParams();
                depositSortParams.setId(sortCommitList.get(i).getId());
                depositSortParams.setSort(i);
                sureSortCommitList.add(depositSortParams);
            }
            LogUtil.e("sortList==", new Gson().toJson(sureSortCommitList));
            requestDepositSortList();
        }

        /**
         * 谷歌官方文档说明如下：
         * 这个看了一下主要是做左右拖动的回调
         * When a View is swiped, ItemTouchHelper animates it until it goes out of bounds, then calls onSwiped(ViewHolder, int).
         * At this point, you should update your adapter (e.g. remove the item) and call related Adapter#notify event.
         * @param viewHolder
         * @param direction
         */
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //暂不处理
        }


        /**
         * 官方文档如下：返回true 当前tiem可以被拖动到目标位置后，直接”落“在target上，其他的上面的tiem跟着“落”，
         * 所以要重写这个方法，不然只是拖动的tiem在动，target tiem不动，静止的
         * Return true if the current ViewHolder can be dropped over the the target ViewHolder.
         * @param recyclerView
         * @param current
         * @param target
         * @return
         */
        @Override
        public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
            return true;
        }


        /**
         * 官方文档说明如下：
         * Returns whether ItemTouchHelper should start a drag and drop operation if an item is long pressed.
         * 是否开启长按 拖动
         *
         * @return
         */
        @Override
        public boolean isLongPressDragEnabled() {
            //return true后，可以实现长按拖动排序和拖动动画了
            return true;
        }
    };

    private void requestDepositSortList() {
        otcMoneyBagListProviderServices.requestDepositSortProvider(sureSortCommitList)
                .compose(bindToLifecycle())
                .subscribeWith(new RxProgressSubscriber<Object>((BaseProgressView) mCtx) {
                    @Override
                    public void onSuccess(Object data) {

                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {

                    }
                });
    }

    @Override
    protected void onEvent() {
        requestWalletList();//网络请求数据
    }

    private void requestWalletList() {
        mGetExchangeCoinListProviderServices.requestOTCDepositMgList()
                .compose(bindToLifecycle())
                .subscribeWith(new RxProgressSubscriber<List<OTCDepositMgListBean>>((BaseProgressView) mCtx) {
                    @Override
                    public void onSuccess(List<OTCDepositMgListBean> data) {
                        commonAdapter.setNewData(data);
                        CommonAnimUtils.playCommonAllViewAnimation(mRvDeposit, CommonLayoutAnimationHelper.getAnimationSetFromRight(), false);
                        if (commonAdapter.getData().size() == 1) return;
                        itemTouchHelper = new ItemTouchHelper(callback);
                        //2.绑定到recyclerview上面去
                        itemTouchHelper.attachToRecyclerView(mRvDeposit);
                        //setFooter(mRvDeposit);
                    }

                    @Override
                    protected void onError(ResponseThrowable ex) {

                    }
                });
    }

    //添加底部的方法
    private void setFooter(RecyclerView view) {
        commonAdapter.setFooterView(view2);
    }

    @Override
    protected BaseView getView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.mr_back_layout) {
            this.finish();
        } else if (i == R.id.btn_get_deposit) {
            requestCheckWithdraw();
            //ARouter.getInstance().build(OTCConstant.WITHDRAWDEPOSIT).navigation(mCtx);

        } else {
        }
    }

    private void requestCheckWithdraw() {
        addSubscribe(otcAddBuyCurrProviderServices.requestCheckWithdrawProvider()
                .compose(bindToLifecycle())
                .subscribeWith(new RxProgressSubscriber<Boolean>(this) {
            @Override
            protected void onError(ResponseThrowable ex) {
                if (ex.getErrorCode().equals("964200")) {
                    OTCOneBtnPopWindow otcOneBtnPopWindow = new OTCOneBtnPopWindow(mCtx);
                    otcOneBtnPopWindow.showPopupWindow();
                    otcOneBtnPopWindow.setTitle(getString(R.string.str_otc_deposit_manager_hint));
                    otcOneBtnPopWindow.getSkipSureDismiss().setOnClickListener(v -> {
                        otcOneBtnPopWindow.dismiss();
                    });
                    //ARouter.getInstance().build(OTCConstant.WITHDRAWDEPOSIT).navigation(mCtx);
                } else if (ex.getErrorCode().equals("962900")) {
                    OTCOneBtnPopWindow otcOneBtnPopWindow = new OTCOneBtnPopWindow(mCtx);
                    otcOneBtnPopWindow.showPopupWindow();
                    otcOneBtnPopWindow.setTitle(getString(R.string.str_otc_deposit_manager_dialog));
                    otcOneBtnPopWindow.getSkipSureDismiss().setOnClickListener(v -> {
                        otcOneBtnPopWindow.dismiss();
                    });
                } else {
                    ToastUtil.show(getString(R.string.server_connection_error));
                }

            }

            @Override
            public void onSuccess(Boolean checkNewPinBean) {
                ARouter.getInstance().build(OTCConstant.WITHDRAWDEPOSIT).navigation(mCtx);
            }
        }));
    }

    @Subscriber
    public void refreshManagerList(PosInfo posInfo) {
        if (posInfo == null) return;
        if (posInfo.getPos() == 167) {
            this.finish();
        }
    }
}
