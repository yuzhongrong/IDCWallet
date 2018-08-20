package com.idcg.idcw.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.idcg.idcw.R;
import com.idcg.idcw.model.bean.ShareBean;
import com.idcg.idcw.utils.MobShareUtil2;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.simple.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpz on 2018/6/27.
 */

public class CommonShareDialog extends Dialog implements View.OnClickListener {

    private WeakReference<Context> mWeakReferenceCtx;
    private View mRootView;
    private RecyclerView rvShare;
    private int[] SHARE_LIST_NAME = null;
    private int[] SHARE_ICONS = {
            R.mipmap.app_img_wechat,
            R.mipmap.app_img_wechat_quan,
            R.mipmap.app_img_weibo,
            R.mipmap.app_img_twitter,
            R.mipmap.app_img_facebook,
            R.mipmap.app_img_telegram};
    private BaseQuickAdapter<ShareBean, BaseViewHolder> shareCommonAdapter;
    private List<ShareBean> list = new ArrayList<>();

    private String shareUrl;
    private String shareTitle;
    private String shareSummary;
    private String imgUrl;

    public CommonShareDialog(@NonNull Context context) {
        this(context, 0);
    }

    public CommonShareDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BottomDialog);
        mWeakReferenceCtx = new WeakReference<>(context);
        EventBus.getDefault().register(this);
        initView();
    }

    public CommonShareDialog(@NonNull Context context,String shareTitle,String shareSummary,String shareUrl,String imgUrl) {
        super(context, R.style.BottomDialog);
        this.shareUrl=shareUrl;
        this.imgUrl=imgUrl;
        this.shareTitle=shareTitle;
        this.shareSummary=shareSummary;
        mWeakReferenceCtx = new WeakReference<>(context);
        initView();
    }

    private void initView() {
        SHARE_LIST_NAME = new int[]{
                R.string.str_app_wechat_fri,
                R.string.str_app_wechat_moment,
                R.string.str_app_sina,
                R.string.twitter,
                R.string.face_book,
                R.string.dian_bao};
        mRootView = LayoutInflater.from(mWeakReferenceCtx.get()).inflate(R.layout.item_share_dialog, null);
        setCancelable(true);
        setCanceledOnTouchOutside(true);// 外部点击取消
        //初始化控件
        rvShare = mRootView.findViewById(R.id.rv_share);
        mRootView.findViewById(R.id.btn_share_cancel).setOnClickListener(this);
        setContentView(mRootView);
        Window dialogWindow = getWindow();
        //设置Dialog从窗体底部弹出
        if (dialogWindow != null) {
            dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setWindowAnimations(R.style.AnimBottom);
            final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.gravity = Gravity.BOTTOM; // 紧贴底部
            lp.width = dialogWindow.getWindowManager().getDefaultDisplay().getWidth(); // 宽度持平
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialogWindow.setAttributes(lp);
        }
        initShareRecycler();
    }

    private void initShareRecycler() {
        rvShare.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvShare.setAdapter(shareCommonAdapter = new BaseQuickAdapter<ShareBean, BaseViewHolder>(R.layout.item_share, getShareInfo()) {
            @Override
            protected void convert(BaseViewHolder helper, ShareBean item) {
                if (item == null) return;
                helper.setImageResource(R.id.iv_type, item.getImg());
                helper.setText(R.id.tv_type, item.getName());
            }
        });

        rvShare.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {//ZXingUtils.createImage("IDCW", 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo))
                switch (position) {
                    case 0://微信好友
                        MobShareUtil2.getInstance().shareToWechatFriendsByMobProvider(mWeakReferenceCtx.get(),shareTitle, shareSummary, shareUrl, imgUrl);
                        break;
                    case 1://微信朋友圈
                        MobShareUtil2.getInstance().shareToWechatByMobProvider(mWeakReferenceCtx.get(),shareTitle, shareSummary, shareUrl,imgUrl);
                        break;
                    case 2://新浪微博
                        MobShareUtil2.getInstance().shareToWeiboByMobProvider(shareTitle, shareSummary, shareUrl, imgUrl,null);
                        break;
                    case 3://推特
                        MobShareUtil2.getInstance().shareToTwitterByMobProvider(shareTitle, shareSummary, shareUrl, null,null);
                        break;
                    case 4://FaceBook
                        MobShareUtil2.getInstance().shareToFacebookByMobProvider(shareTitle, shareSummary, shareUrl, imgUrl,null);
                        break;
                    case 5://电报
                        MobShareUtil2.getInstance().shareToTelegramByMobProvider(mWeakReferenceCtx.get(),shareTitle, shareSummary, shareUrl, imgUrl,null);
                        break;
                    default:
                        break;
                }
                dismiss();
            }
        });
    }

    private List<ShareBean> getShareInfo() {
        for (int i = 0; i < SHARE_LIST_NAME.length; i++) {
            ShareBean shareBean = new ShareBean();
            shareBean.setName(SHARE_LIST_NAME[i]);
            shareBean.setImg(SHARE_ICONS[i]);
            list.add(shareBean);
        }
        return list;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_share_cancel) {
            dismiss();
        }
    }
}
