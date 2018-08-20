package foxidcw.android.idcw.otc.widgets.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cjwsc.idcm.Utils.GlideApp;
import com.cjwsc.idcm.Utils.GlideUtil;
import com.cjwsc.idcm.Utils.ToastUtil;
import com.cjwsc.idcm.Utils.image.SaveImgUtil;
import com.cjwsc.idcm.base.application.BaseApplication;
import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import foxidcw.android.idcw.common.base.BaseWalletActivity;
import foxidcw.android.idcw.otc.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import razerdp.basepopup.BasePopupWindow;

//一个按钮
public class OTCQrCodePopWindow
        extends BasePopupWindow implements View.OnClickListener {

    private ImageView mIvQrCode;
    private View mBtnConfirm;
    private String mQrCode;
    private View mIvClose;

    public OTCQrCodePopWindow(Context context) {
        super(context);

    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
//        return getPopupWindowView();
        return mIvClose;
    }

    @Override
    public View onCreatePopupView() {
        View root = createPopupById(R.layout.pop_otc_qr_code);
        mIvClose = root.findViewById(R.id.iv_close);
        mIvQrCode = root.findViewById(R.id.imageView);
        mBtnConfirm = root.findViewById(R.id.btn_confirm);
        RxPermissions rxPermissions = new RxPermissions((Activity) getContext());
        RxView.clicks(mBtnConfirm)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        if(!aBoolean)
                            Toast.makeText(BaseApplication.getContext(), R.string.permission_deny, Toast.LENGTH_SHORT).show();
                        return aBoolean&& !TextUtils.isEmpty(mQrCode);
                    }
                })
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        ((BaseWalletActivity)getContext()).showDialog();
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<Object, String>() {
                    @Override
                    public String apply(Object o) throws Exception {
                        return mQrCode;
                    }
                })
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String path) throws Exception {
                        return GlideApp.with(BaseApplication.getContext())
                                .asBitmap() //必须
                                .load(path)
                                .into(500, 500)
                                .get();
                    }
                })
                .map(new Function<Bitmap, Boolean>() {
                    @Override
                    public Boolean apply(Bitmap bitmap) throws Exception {
                        return SaveImgUtil.saveImageToGallery(BaseApplication.getContext(), bitmap);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        ((BaseWalletActivity)getContext()).dismissDialog();
                    }
                })
                .compose(((BaseWalletActivity)getContext()).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean o) throws Exception {
                        if(o)
                            ToastUtil.show(getContext().getString(R.string.otc_save_album_success));
                        else
                            ToastUtil.show(getContext().getString(R.string.otc_save_album_failed));
                    }
                });
        return root;
    }

    public void showPop(String qrCode){
        mQrCode = qrCode;
        int width = getContext().getResources().getDimensionPixelSize(R.dimen.dp239);
        int height = getContext().getResources().getDimensionPixelSize(R.dimen.dp292);
//        GlideUtil.loadImageViewSize(getContext(), qrCode,
//                width,
//                height, mIvQrCode);
//        GlideUtil.loadImageViewContentWithSizeFixRatio(getContext(), mQrCode, 1.2f, width, height, mIvQrCode);
        GlideApp.with(getContext()).load(mQrCode).diskCacheStrategy(DiskCacheStrategy.ALL).override(width, height).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean
                    isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                mIvQrCode.post(new Runnable() {
                    @Override
                    public void run() {
                        float intrinsicWidth = resource.getIntrinsicWidth();
                        float intrinsicHeight = resource.getIntrinsicHeight();
                        //高于这个比例显示最高
                        if(intrinsicHeight/intrinsicWidth>1.22f){
                            GlideApp.with(getContext()).load(mQrCode).override(width, height).centerCrop().into(mIvQrCode);
                        }else {
                            GlideUtil.loadImageView(getContext(), mQrCode, mIvQrCode);
                        }
                    }
                });
                return true;
            }

        }).into(mIvQrCode);
        showPopupWindow();
    }

    @Override
    public View initAnimaView() {
        return null;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm) {

        }
    }
}
