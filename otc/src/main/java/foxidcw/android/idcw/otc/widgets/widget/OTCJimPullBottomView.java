package foxidcw.android.idcw.otc.widgets.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by hpz on 2018/5/2.
 */

public class OTCJimPullBottomView extends ScrollView {

    private int downX;
    private int downY;
    private int mTouchSlop;
    //private ScrollViewListener mScrollViewListener=null;

//    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
//        mScrollViewListener = scrollViewListener;
//    }

    public OTCJimPullBottomView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public OTCJimPullBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public OTCJimPullBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
//        if (mScrollViewListener != null) {
//            mScrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
//        }
    }
}

