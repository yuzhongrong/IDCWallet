package foxidcw.android.idcw.otc.utils;

import android.annotation.SuppressLint;
import android.support.annotation.AnimRes;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.cjwsc.idcm.base.application.BaseApplication;

import foxidcw.android.idcw.otc.R;

public class OTCGridBottomInAnimUtils {


    public static LayoutAnimationController initAnimationController(@AnimRes int animResId) {
        @SuppressLint("ResourceType") Animation animation = AnimationUtils.loadAnimation(BaseApplication.getContext(),
                animResId <= 0 ? R.anim.otc_item_bottom_in_anim : animResId);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        controller.setDelay(0.05f);
        //controller.setInterpolator(new AnticipateOvershootInterpolator());
        return controller;
    }

    public static LayoutAnimationController initAnimationController() {
        return initAnimationController(0);
    }
}
