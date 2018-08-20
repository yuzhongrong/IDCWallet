package com.idcg.idcw.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import com.cjwsc.idcm.Utils.LogUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.idcg.idcw.R;
import com.idcg.idcw.iprovider.GetAddAllCurrencyProviderServices;
import com.idcg.idcw.model.bean.AddDataBean;
import com.idcg.idcw.model.logic.AddCurrencyLogic;
import com.idcg.idcw.model.params.AddCurrencyIsShow;
import com.idcg.idcw.presenter.impl.AddCurrencyPresenterImpl;
import com.idcg.idcw.presenter.interfaces.AddCurrencyContract;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjwsc.idcm.Utils.ACacheUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.adapter.CommonAdapter;
import com.cjwsc.idcm.base.BaseView;
import com.cjwsc.idcm.net.callback.RxProgressSubscriber;
import com.cjwsc.idcm.net.exception.ResponseThrowable;
import com.google.gson.Gson;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import foxidcw.android.idcw.common.base.BaseWalletFragment;
import foxidcw.android.idcw.common.model.bean.PosInfo;
import foxidcw.android.idcw.common.utils.CommonLayoutAnimationHelper;

/**
 * Created by hpz on 2018/6/4.
 */

public class OptionalCurrFragment extends BaseWalletFragment<AddCurrencyLogic, AddCurrencyPresenterImpl>
        implements AddCurrencyContract.View {

    @BindView(R.id.rv_add_currency)
    RecyclerView rvAddCurrency;
    Unbinder unbinder;

    private String mTitle;
    private ItemTouchHelper itemTouchHelper;
    private List<AddDataBean> assetList = new ArrayList<>();
    private List<AddDataBean> assetNewList = new ArrayList<>();
    private List<AddCurrencyIsShow> phraseList = new ArrayList<>();
    private CommonAdapter commonAdapter;

    @Autowired
    GetAddAllCurrencyProviderServices mAddCurrencyServices;

    public static OptionalCurrFragment getInstance(String title) {
        OptionalCurrFragment self = new OptionalCurrFragment();
        self.mTitle = title;
        return self;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_optional_curr;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        ARouter.getInstance().inject(this);
        initCurrList();//初始化adapter
    }

    private void initCurrList() {
        commonAdapter = new CommonAdapter<AddDataBean>(R.layout.item_optional_curr) {
            @Override
            public void commonconvert(BaseViewHolder helper, AddDataBean item) {
                Glide.with(mContext).load(item.getLogo_url()).into((ImageView) helper.getView(R.id.img_currency_icon));
                ((TextView) helper.getView(R.id.currency_name)).setText(item.getCurrency().toUpperCase());
                ((TextView) helper.getView(R.id.currency_tag)).setText(item.getCurrencyName());

                helper.getConvertView().setOnClickListener(v -> {

                });
            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAddCurrency.setLayoutManager(linearLayoutManager);
        rvAddCurrency.setAdapter(commonAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        //2.绑定到recyclerview上面去
        itemTouchHelper.attachToRecyclerView(rvAddCurrency);
    }

    ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {

        /**
         * 官方文档的说明如下：
         * o control which actions user can take on each view, you should override getMovementFlags(RecyclerView, ViewHolder)
         * and return appropriate set of direction flags. (LEFT, RIGHT, START, END, UP, DOWN).
         * 返回我们要监控的方向，上下左右，我们做的是上下拖动，要返回都是UP和DOWN
         * 关键坑爹的是下面方法返回值只有1个，也就是说只能监控一个方向。
         * 不过点入到源码里面有惊喜。源码标记方向如下：
         * public static final int UP = 1     0001
         * public static final int DOWN = 1 << 1; （位运算：值其实就是2）0010
         * public static final int LEFT = 1 << 2   左 值是3
         * public static final int RIGHT = 1 << 3  右 值是8
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
         *
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
            RequestAddCurrency();//网络请求同步到服务器
        }

        /**
         * 谷歌官方文档说明如下：
         * 这个看了一下主要是做左右拖动的回调
         * When a View is swiped, ItemTouchHelper animates it until it goes out of bounds, then calls onSwiped(ViewHolder, int).
         * At this point, you should update your adapter (e.g. remove the item) and call related Adapter#notify event.
         *
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
         *
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

    @Override
    protected void onEvent() {

    }

    private void RequestAddCurrency() {
        phraseList.clear();
        try {
            assetNewList = commonAdapter.getData();
            for (AddDataBean s : assetNewList) {
                AddCurrencyIsShow currencyIsShow = new AddCurrencyIsShow();
                currencyIsShow.setSortIndex(s.getSortIndex());
                currencyIsShow.setId(s.getId());
                currencyIsShow.setIsShow(s.isIsShow());
                phraseList.add(currencyIsShow);
            }
            for (int i = 0; i < phraseList.size(); i++) {
                phraseList.get(i).setSortIndex(i + 1);
            }
            mAddCurrencyServices.requestAddCurrencyProvider(phraseList)
                    .compose(bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribeWith(new RxProgressSubscriber<Object>(this) {
                @Override
                protected void onError(ResponseThrowable ex) {
                    if (isAdded()) {
                        ToastUtil.show(getResources().getString(R.string.server_connection_error));
                    }
                }

                @Override
                public void onSuccess(Object object) {
                    EventBus.getDefault().post(new PosInfo(169));
                }
            });
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    protected BaseView getViewImp() {
        return this;
    }

    @Override
    protected void lazyFetchData() {
        showDialog();
        mPresenter.requestAddData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void playLayoutAnimation(Animation animationSetFromRight) {
        playLayoutAnimation(animationSetFromRight, false);
    }

    /**
     * 播放RecyclerView动画
     *
     * @param animation
     * @param isReverse
     */
    public void playLayoutAnimation(Animation animation, boolean isReverse) {
        try {
            LayoutAnimationController controller = new LayoutAnimationController(animation);
            controller.setDelay(0.1f);
            controller.setOrder(isReverse ? LayoutAnimationController.ORDER_REVERSE : LayoutAnimationController.ORDER_NORMAL);

            rvAddCurrency.setLayoutAnimation(controller);
            rvAddCurrency.getAdapter().notifyDataSetChanged();
            rvAddCurrency.scheduleLayoutAnimation();
        } catch (Exception ex) {

        }
    }


    @Override
    public void showErrorWithStatus(ResponseThrowable throwable) {
        dismissDialog();
        if (throwable.getErrorCode().equals("120")) {
        } else if (throwable.getErrorCode().equals("100")) {
            return;
        } else {
            if (isAdded()) {
                ToastUtil.show(getResources().getString(R.string.server_connection_error));
            }
        }
    }

    @Override
    public void updateRequestAddData(List<AddDataBean> params) {
        dismissDialog();
        assetList.clear();
        for (AddDataBean s : params) {
            if (s.isIsShow()) {
                AddDataBean currencyIsShow = new AddDataBean();
                currencyIsShow.setSortIndex(s.getSortIndex());
                currencyIsShow.setId(s.getId());
                currencyIsShow.setIsShow(s.isIsShow());
                currencyIsShow.setCurrency(s.getCurrency());
                currencyIsShow.setCurrencyName(s.getCurrencyName());
                currencyIsShow.setLogo_url(s.getLogo_url());
                assetList.add(currencyIsShow);
            }
        }

        commonAdapter.setNewData(assetList);
        if (TextUtils.isEmpty(ACacheUtil.get(mContext).getAsString("refreshSelect"))){
            playLayoutAnimation(CommonLayoutAnimationHelper.getAnimationSetFromRight());
        }
        LogUtil.e("assetList==",new Gson().toJson(assetList));
    }

    @Override
    public void updateRequestAddCurrency(Object object) {

    }

    @Subscriber
    public void onRefreshCurrInfo(PosInfo posInfo) {
        try {
            if (posInfo == null) return;
            if (posInfo.getPos() == 170) {
                showDialog();
                mPresenter.requestAddData();
            }
        } catch (Exception ex) {
            LogUtil.e("Exception:", ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ACacheUtil.get(mContext).remove("refreshSelect");
    }
}
