package cn.com.epsoft.keyboard.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.epsoft.keyboard.R;
import cn.com.epsoft.keyboard.widget.adapter.KeyboardAdapter;
import cn.com.epsoft.keyboard.widget.adapter.KeyboardAdapter.OnItemClickListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 启研
 * @created at 2017/9/28 16:40
 */

public class PayKeyboardView extends LinearLayout implements OnItemClickListener {

  private TextView titleTv;
  private ImageView backIv;
  private TextView[] passwordTvs = new TextView[6];
  private List<String> passwords = new ArrayList<>();
  private RecyclerView keyboardRv;
  private TextView pass_view_forget_pass;
  KeyboardAdapter adapter = new KeyboardAdapter();

  public PayKeyboardView(Context context) {
    this(context, null);
  }

  public PayKeyboardView(Context context,
      @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PayKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PayKeyboardView);
    boolean showTitle = ta.getBoolean(R.styleable.PayKeyboardView_showTitle, true);
    ta.recycle();
    setOrientation(LinearLayout.VERTICAL);
    setBackgroundColor(Color.parseColor("#f7f7f7"));
    View view = LayoutInflater.from(context).inflate(R.layout.keyboard_view_keyboard, this);
    if (!showTitle) {
      view.findViewById(R.id.titleRl).setVisibility(GONE);
    }
    titleTv = view.findViewById(R.id.titleTv);
    pass_view_forget_pass = view.findViewById(R.id.pass_view_forget_pass);
    backIv = view.findViewById(R.id.backIv);
    keyboardRv = view.findViewById(R.id.keyboardRv);
    passwordTvs[0] = view.findViewById(R.id.pay1Tv);
    passwordTvs[1] = view.findViewById(R.id.pay2Tv);
    passwordTvs[2] = view.findViewById(R.id.pay3Tv);
    passwordTvs[3] = view.findViewById(R.id.pay4Tv);
    passwordTvs[4] = view.findViewById(R.id.pay5Tv);
    passwordTvs[5] = view.findViewById(R.id.pay6Tv);
    keyboardRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
    adapter.setItems(new Object[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0",
        R.drawable.keyboard_vec_delete});
    adapter.setOnItemClickListener(this);
    keyboardRv.setAdapter(adapter);
    keyboardRv.addItemDecoration(new ItemDecoration() {
      @Override
      public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        int position = parent.getChildLayoutPosition(view);
        int space = 2;
        outRect.top = space;
        if (position % 3 != 0) {
          outRect.left = space;
        }
      }
    });
    backIv.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (listener != null) {
          listener.onBack();
        }
      }
    });
    
    pass_view_forget_pass.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (listener != null) {
          listener.onSetPay();
        }
      }
    });
  }
  

  @Override
  public void onAddText(String text) {
    int size = passwords.size();
    if (size < 6) {
      passwords.add(text);
      passwordTvs[size].setText(text);
      if (size + 1 == 6) {
        if (listener != null) {
          listener
              .onComplete(passwords.get(0), passwords.get(1), passwords.get(2), passwords.get(3),
                  passwords.get(4), passwords.get(5));
        }
      }
    }
  }

  @Override
  public void onDeleteOne() {
    if (!passwords.isEmpty()) {
      int p = passwords.size() - 1;
      passwords.remove(p);
      passwordTvs[p].setText("");
    }
  }

  /**
   * 清理密码
   */
  public void clear() {
    passwords.clear();
    for (TextView tv : passwordTvs) {
      tv.setText("");
    }
  }

  public void setTitle(String title) {
    titleTv.setText(title);
  }

  public void setTitleForget(String titleForget) {
    pass_view_forget_pass.setText(titleForget);
  }

  OnKeyboardListener listener;

  public void setOnKeyboardListener(OnKeyboardListener l) {
    this.listener = l;
  }

  public interface OnKeyboardListener {

    void onComplete(String one, String two, String three, String four, String five, String six);

    void onBack();

    void onSetPay();
  }
}
