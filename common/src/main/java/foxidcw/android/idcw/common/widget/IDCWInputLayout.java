package foxidcw.android.idcw.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.HashMap;
import java.util.LinkedHashMap;

import foxidcw.android.idcw.common.R;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by hpz on 2018/7/3.
 */

public class IDCWInputLayout extends LinearLayout implements View.OnFocusChangeListener {

    private FrameLayout mInputFrame;
    private boolean isEditInit;
    private boolean textClearable;
    private boolean passwordToggleEnabled;
    private Context mContext;
    private EditText mEditText;
    private ImageView mIvClear;
    private ImageView mIvTogglePassword;
    private Drawable mEyeCloseDrawable;
    private Drawable mEyeOpenDrawable;
    private boolean isEyeOpen;
    private PorterDuffColorFilter filter;
    private LinearLayout mLayoutErr;

    public IDCWInputLayout(Context context) {
        this(context, null);
    }

    public IDCWInputLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IDCWInputLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setOrientation(HORIZONTAL);
        setWillNotDraw(false);
        setAddStatesFromChildren(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable
                .IDCMInputLayout, defStyleAttr, 0);
        textClearable = a.getBoolean(R.styleable.IDCMInputLayout_textClearable, true);
        passwordToggleEnabled = a.getBoolean(R.styleable.IDCMInputLayout_passwordToggleEnable, false);
        errorEnable = a.getBoolean(R.styleable.IDCMInputLayout_errorEnable, false);
        errorColor = a.getColor(R.styleable.IDCMInputLayout_errorColor, ContextCompat.getColor(mContext, R.color.common_blue));
        a.recycle();
        if(!passwordToggleEnabled){
            isEyeOpen = true;
        }
    }

    @Override
    public void addView(View child, int index, final ViewGroup.LayoutParams params) {
        if(child instanceof EditText){
            if(isEditInit)throw new IllegalArgumentException("只能有一个EditText");
            setEditText((EditText) child);
            isEditInit = true;
            initChild();
            FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(params);
            flp.gravity = Gravity.CENTER_VERTICAL | (flp.gravity & ~Gravity.VERTICAL_GRAVITY_MASK);
            mInputFrame.addView(child, flp);

        }else if(!isEditInit){
            super.addView(child, index,  params);
        }else {
            super.addView(child, -1, params);
        }
    }


    private void setEditText(EditText editText) {
        mEditText = editText;
    }

    private void initChild() {
        int width = mContext.getResources().getDimensionPixelSize(R.dimen.dp24);
        int height = mContext.getResources().getDimensionPixelSize(R.dimen.dp24);
        int paddingHorizental = mContext.getResources().getDimensionPixelSize(R.dimen.dp10);
        int paddingVertical = mContext.getResources().getDimensionPixelSize(R.dimen.dp5);

        mInputFrame = new FrameLayout(mContext);
        mInputFrame.setAddStatesFromChildren(true);
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        mInputFrame.setLayoutParams(layoutParams);
        addView(mInputFrame);

        //这里设置水波纹不知道怎么不生效
//        int resourceId=0;
//        if(textClearable || passwordToggleEnabled) {
//            int[] attrs = {android.R.attr.selectableItemBackgroundBorderless};
//            TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(attrs);
//            resourceId = typedArray.getResourceId(0, 0);
//        }
        if(textClearable){
//            Space space = new Space(mContext);
//            LayoutParams spaceLp = new LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen
//                    .dp10), 0);
//            addView(space, spaceLp);
            mIvClear = new ImageView(mContext);
            mIvClear.setPadding(paddingHorizental,paddingVertical,0,paddingVertical);
            //mIvClear.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.icon_input_delete));
//            mIvClear.setBackgroundResource(resourceId);
            LayoutParams layoutParams1 = new LayoutParams(width, height);
            layoutParams1.gravity = Gravity.CENTER_VERTICAL;
            addView(mIvClear,layoutParams1);
            mIvClear.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditText.setText("");
                    updateClearState();
                }
            });
            updateClearState();
        }
        if(passwordToggleEnabled){
            if(textClearable){
                Space space = new Space(mContext);
                LayoutParams spaceLp = new LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen
                        .dp10), 0);
                addView(space, spaceLp);
            }
            //mIvTogglePassword = new ImageView(mContext);
            mIvTogglePassword.setPadding(paddingHorizental,paddingVertical,0,paddingVertical);
//            mIvTogglePassword.setBackgroundResource(resourceId);
//            mEyeCloseDrawable = ContextCompat.getDrawable(mContext, R
//                    .mipmap.icon_input_close);
//            mEyeOpenDrawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_input_open);
//            StateListDrawable eyeDrawable = new StateListDrawable();
//            eyeDrawable.addState(new int[]{android.R.attr.state_selected}, mEyeCloseDrawable);
//            eyeDrawable.addState(new int[0], mEyeOpenDrawable);
//            mIvTogglePassword.setImageDrawable(eyeDrawable);
            LayoutParams layoutParams1 = new LayoutParams(width, height);
            layoutParams1.gravity = Gravity.CENTER_VERTICAL;
            addView(mIvTogglePassword,layoutParams1);
            mIvTogglePassword.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isEyeOpen = !isEyeOpen;
                    updatePasswordToggleState();
                }
            });
            updatePasswordToggleState();
        }
        mEditText.setOnFocusChangeListener(this);
        RxTextView.textChanges(mEditText)
                .skipInitialValue()
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        updateClearState();
                    }
                });
    }

    private void updateClearState() {
        if (!isEditInit || mIvClear==null)return;
        if(!mEditText.isFocused() || mEditText.getText().length()<1 || !textClearable){
            mIvClear.setVisibility(GONE);
        }else {
            mIvClear.setVisibility(GONE);
        }
    }

    private void updatePasswordToggleState() {
        if (!isEditInit || mIvTogglePassword==null)return;
        if(!mEditText.isFocused() || !passwordToggleEnabled){
            mIvTogglePassword.setVisibility(GONE);
        }else {
            mIvTogglePassword.setVisibility(GONE);
        }
        if (isEyeOpen) {
            mEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mIvTogglePassword.setImageDrawable(mEyeOpenDrawable);
        } else {
            mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mIvTogglePassword.setImageDrawable(mEyeCloseDrawable);
        }
        mEditText.setSelection(mEditText.length());
    }

    private OnFocusChangeListener mOnFocusChangeListener;
    public void setOnFocusChangeListener(OnFocusChangeListener listener){
        mOnFocusChangeListener = listener;
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        updateClearState();
        updatePasswordToggleState();
        if(mOnFocusChangeListener !=null)
            mOnFocusChangeListener.onFocusChange(this, hasFocus);
    }

    private int errorColor;
    private boolean errorEnable;
    private TextView mTvErr;
    private Disposable mErrDisposable;
    private HashMap<String, String> map = new LinkedHashMap<>();
    private static final PublishSubject<Pair<View, String>> sErrSubject = PublishSubject.create();

    /**
     * 在布局的下方显示该错误文本
     * @param text 输入null则不显示
     */
    public void setError(@Nullable String text){
        sErrSubject.onNext(new Pair(this, text));
        updateErrorState(!TextUtils.isEmpty(text));
    }

    /**
     * 传入true则显示格式错误，并将布局select设置true，传入false则不显示
     * @param isError
     */
    public void setError(boolean isError){
//        sErrSubject.onNext(new Pair(this, isError?mContext.getString(R.string.format_error):null));
//        updateErrorState(isError);
        //setError(isError?mContext.getString(R.string.format_error):null);
    }

    private void updateErrorState(boolean isError) {
        Drawable editTextBackground = getBackground();
        if (editTextBackground == null) {
            return;
        }
        setSelected(isError);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getParent() instanceof LinearLayout) {
            if (errorEnable) {
                LinearLayout parent = (LinearLayout) getParent();
                if(parent.getOrientation()==HORIZONTAL){
                    throw new IllegalStateException("需要父布局Orientation为垂直");
                }
                int indexOfChild = parent.indexOfChild(this);
                Space space = new Space(mContext);
                LayoutParams spaceLp = new LayoutParams(0, mContext.getResources().getDimensionPixelSize(R.dimen
                        .dp10));
                //插入到view的下面，避免出现嵌套问题
                parent.addView(space, indexOfChild + 1, spaceLp);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                mLayoutErr = new LinearLayout(mContext);
                mLayoutErr.setOrientation(HORIZONTAL);
                mLayoutErr.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                parent.addView(mLayoutErr, indexOfChild + 2, params);

                ImageView imageView = new ImageView(mContext);
                MarginLayoutParams params1 = new MarginLayoutParams(getResources().getDimensionPixelSize(R.dimen
                        .dp16), getResources().getDimensionPixelSize(R.dimen.dp16));
                params1.rightMargin = getResources().getDimensionPixelOffset(R.dimen.dp6);
                imageView.setLayoutParams(params1);
                //imageView.setImageResource(R.mipmap.icon_red_error);
                mLayoutErr.addView(imageView);

                mTvErr = new TextView(mContext);
                mTvErr.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.dp14));
                mTvErr.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                mTvErr.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.dp2));
//                Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.icon_red_error);
//                drawable.setBounds(0,0, getResources().getDimensionPixelSize(R.dimen.dp16), getResources().getDimensionPixelSize(R.dimen.dp16));
//                mTvErr.setCompoundDrawablesWithIntrinsicBounds(drawable, null,null,null);
//                mTvErr.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.dp5));
                mTvErr.setTextColor(errorColor);
//                ((LinearLayout) getParent()).addView(mTvErr, params);
                mLayoutErr.addView(mTvErr);
                mLayoutErr.setVisibility(INVISIBLE);
            }
        }else if(errorEnable){
            throw new IllegalStateException("使用errorEnable属性,其父布局必须为LinearLayout,并且Orientation为垂直");
        }
        if(errorEnable)
            mErrDisposable = sErrSubject.subscribe(new Consumer<Pair<View, String>>() {
                @Override
                public void accept(Pair<View, String> pair) throws Exception {
                    //判断是不是一个布局内的
                    if(pair.first.getParent() == getParent()) {
                        String text = pair.second;
                        if(!TextUtils.isEmpty(text)){
                            mTvErr.setText(text);
                            mLayoutErr.setVisibility(VISIBLE);
                            map.put(String.valueOf(pair.first.hashCode()), text);
                        }else{
                            map.remove(String.valueOf(pair.first.hashCode()));
                            if(!map.isEmpty()) {
                                mTvErr.setText(map.values().iterator().next());
                                mLayoutErr.setVisibility(VISIBLE);
                            }else {
                                mLayoutErr.setVisibility(INVISIBLE);
                            }
                        }
                    }
                }
            });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        map.clear();
        if(null != mErrDisposable)
            mErrDisposable.dispose();
    }
}
