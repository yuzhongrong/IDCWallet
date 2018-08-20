package foxidcw.android.idcw.otc.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.base.BaseView;

import java.util.ArrayList;
import java.util.List;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.otc.R;
import foxidcw.android.idcw.otc.widgets.dialog.OTCPromptConfirmCancelPopup;

public class OTCPreviewImgListActivity extends BaseWalletActivity implements View.OnClickListener {

    private LinearLayout mMrBackLayout;
    private TextView mTvSetName;
    private ViewPager mViewPager;
    private boolean mCanDelete;
    private int mCurPos;
    private boolean mDataChanged;
    private static List<String> sPaths;
    private static CallBack sCallBack;
    private ImageView mIvRight;
    private View mLayoutRight;
    private PreviewAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_otcpreview_img_list;
    }

    public static final String CAN_DELETE = "can_delete";
    public static final String POS = "pos";

    /**
     *
     * @param canDelete     是否能删除
     * @param curPos        当前显示第几页
     * @param pathOrUrls    传入本地地址或者网络地址
     * @param callBack      结束该页面时会将改变后的剩余地址回调
     */
    public static void preview(Activity activity, boolean canDelete, int curPos, List<String> pathOrUrls, CallBack callBack) {
        Intent intent = new Intent(activity, OTCPreviewImgListActivity.class);
        sPaths = new ArrayList<>(pathOrUrls);
        sCallBack = callBack;
        intent.putExtra(CAN_DELETE, canDelete);
        intent.putExtra(POS, curPos);
        activity.startActivity(intent);
    }

    @Override
    protected void onInitView(Bundle bundle) {
        mCanDelete = getIntent().getBooleanExtra(CAN_DELETE, false);
        mCurPos = getIntent().getIntExtra(POS, 0);
        initView();
    }

    @Override
    protected void onEvent() {

    }

    public void initView() {
        ActivityCompat.postponeEnterTransition(this);
        mMrBackLayout = (LinearLayout) findViewById(R.id.mr_back_layout);
        mLayoutRight = findViewById(R.id.title_bar_right_layout);
        mLayoutRight.setOnClickListener(this);
        mIvRight = (ImageView) findViewById(R.id.title_bar_img_right);
        mMrBackLayout.setOnClickListener(this);
        mTvSetName = (TextView) findViewById(R.id.tv_set_Name);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        if(!mCanDelete){
            mLayoutRight.setVisibility(View.GONE);
        }
        refreshPageSelectState(mCurPos);

        mAdapter = new PreviewAdapter(this, sPaths);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                refreshPageSelectState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(mCurPos, false);
    }

    private void refreshPageSelectState(int curPos){
        mCurPos = curPos;
        mTvSetName.setText(String.format("%s/%s", mCurPos+1, sPaths.size()));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.mr_back_layout) {
            onBackPressed();
        }else if(id == R.id.title_bar_right_layout){
            if(mCanDelete){
                new OTCPromptConfirmCancelPopup(this,getString(R.string.otc_confirm_to_delete), getString(R.string.confirm), getString(R.string.cancel), () -> {
                    mAdapter.removeItem(mCurPos);
                    refreshPageSelectState(mViewPager.getCurrentItem());
                    mDataChanged = true;
                    if(sPaths.size()==0){
                        mViewPager.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //不知道为什么。。。不延时会提示没有notifyDataSetChanged
                                onBackPressed();
                            }
                        },50);
                    }
                }).show();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        if(mDataChanged)
            sCallBack.onListChanged(sPaths);
        sCallBack=null;
    }

    /**
     * 简单的适配器
     */
    class PreviewAdapter extends PagerAdapter {
        private final Activity mContext;
        private List<String> photos;

        public PreviewAdapter(Activity context, List<String> photoList) {
            super();
            mContext = context;
            this.photos = photoList;
        }

        @Override
        public int getCount() {
            return photos.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return mCanDelete?POSITION_NONE:POSITION_UNCHANGED;
        }

        public void removeItem(int position){
            if(getCount()<=position)return;
            photos.remove(position);
            notifyDataSetChanged();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_preview_image, container, false);
            view.setTag(position);
            final ImageView bigPhotoIv = (ImageView) view.findViewById(R.id.iv_image_item);
            View progressBar = view.findViewById(R.id.progress_bar);
            ViewCompat.setTransitionName(bigPhotoIv, position+"");
            if(getIntent().getIntExtra(POS,0) == position)
                bigPhotoIv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        bigPhotoIv.getViewTreeObserver().removeOnPreDrawListener(this);
                        ActivityCompat.startPostponedEnterTransition(mContext);
                        return true;
                    }
                });
            bigPhotoIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            String path = photos.get(position);
//            GlideUtil.loadImageView(mContext, path, bigPhotoIv);
            progressBar.setVisibility(View.VISIBLE);
            view.setTag(""+position);
            GlideUtil.loadImageViewContent(mContext, path, new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    if(!view.getTag().equals(""+position))return;
                    progressBar.setVisibility(View.GONE);
                    GlideUtil.loadImageView(mContext, path, bigPhotoIv);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    if(!view.getTag().equals(""+position))return;
                    progressBar.setVisibility(View.GONE);
                }

            });
            container.addView(view);
            return view;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
//            mCurIv = ((View) object).findViewById(R.id.iv_image_item);
        }
    }

    public interface CallBack {
        void onListChanged(List<String> paths);
    }

    @Override
    protected BaseView getView() {
        return null;
    }

}
