package foxidcw.android.idcw.common.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;

import com.cjwsc.idcm.Utils.ScreenUtil;
import com.cjwsc.idcm.base.application.BaseApplication;

/**
 * Created by yuzhongrong on 2018/2/1.
 */

public class CommonAnimUtils {


//    /**
//     * 播放RecyclerView动画
//     *
//     * @param animation
//     * @param isReverse
//     */
//    public static void playCommonAllViewAnimation(RecyclerView view, Animation animation, boolean isReverse) {
//        LayoutAnimationController controller = new LayoutAnimationController(animation);
//        controller.setDelay(0.1f);
//        controller.setOrder(isReverse ? LayoutAnimationController.ORDER_REVERSE : LayoutAnimationController.ORDER_NORMAL);
//
//        view.setLayoutAnimation(controller);
//        view.getAdapter().notifyDataSetChanged();
//        view.scheduleLayoutAnimation();
//    }

    /**
     * 左边出 右边进动画
     *
     * @param exitView
     * @param enterView
     */
    public static void leftExitRightEnterAnimation(View exitView, View enterView) {
        int mScreenWidth = ScreenUtil.getScreenWidth(BaseApplication.getContext());
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator exitTransX = ObjectAnimator.ofFloat(exitView, "translationX", 0f, -mScreenWidth);
        ObjectAnimator exitAlpha = ObjectAnimator.ofFloat(exitView, "alpha", 1f, 0.5f);

        ObjectAnimator enterTransX = ObjectAnimator.ofFloat(enterView, "translationX", mScreenWidth, 0f);
        ObjectAnimator enterAlpha = ObjectAnimator.ofFloat(enterView, "alpha", 0.5f, 1f);

        set.setInterpolator(new LinearInterpolator());
        set.setDuration(300);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                enterView.setVisibility(View.VISIBLE);
                exitView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.playTogether(exitTransX, exitAlpha, enterTransX, enterAlpha);
        set.start();
    }

    /**
     * 右边出左边进
     *
     * @param exitView
     * @param enterView
     */
    public static void LeftEnterRightExitAnimation(final View exitView, final View enterView) {
        int mScreenWidth = ScreenUtil.getScreenWidth(BaseApplication.getContext());
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oneExit = ObjectAnimator.ofFloat(exitView, "translationX", -mScreenWidth, 0f);
        ObjectAnimator oneAlpha = ObjectAnimator.ofFloat(exitView, "alpha", 0f, 1f);
        ObjectAnimator twoEnter = ObjectAnimator.ofFloat(enterView, "translationX", 0f, mScreenWidth);
        ObjectAnimator twoAlpha = ObjectAnimator.ofFloat(enterView, "alpha", 1f, 0f);
        set.setInterpolator(new LinearInterpolator());
        set.setDuration(300);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                exitView.setVisibility(View.VISIBLE);
                enterView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.playTogether(oneExit, twoEnter, oneAlpha, twoAlpha);
        set.start();
    }

    /**
     * 播放任何控件动画
     *
     * @param animation
     * @param isReverse
     */
    public static void playCommonViewAnimation(View view, Animation animation, boolean isReverse) {
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setDelay(0.1f);
        controller.setOrder(isReverse ? LayoutAnimationController.ORDER_REVERSE : LayoutAnimationController.ORDER_NORMAL);
        view.setAnimation(animation);
    }

    public static void playCommonAllViewAnimation(View view, Animation animation, boolean isReverse) {
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setDelay(0.1f);
        controller.setOrder(isReverse ? LayoutAnimationController.ORDER_REVERSE : LayoutAnimationController.ORDER_NORMAL);
        if (view instanceof RecyclerView) {
            RecyclerView viewList = (RecyclerView) view;
            viewList.setLayoutAnimation(controller);
            viewList.getAdapter().notifyDataSetChanged();
            viewList.scheduleLayoutAnimation();
        } else {
            view.setAnimation(animation);
        }
    }

}
