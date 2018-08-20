package foxidcw.android.idcw.otc.widgets.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import foxidcw.android.idcw.otc.R;

/**
 * Created by hpz on 2018/6/13.
 */

public class AddOrReduceView extends LinearLayout implements View.OnClickListener{
    private double value = 0.5;
    private double minValue = -10.0;
    private double maxValue = 10.0;
    private final TextView tvCount;

    public AddOrReduceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.number_adder, this);
        ImageView btn_reduce = (ImageView) view.findViewById(R.id.btn_reduce);
        tvCount = (TextView) view.findViewById(R.id.tv_count);
        ImageView btn_add = (ImageView) view.findViewById(R.id.btn_add);
        btn_reduce.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        //设置默认值
        double value = getValue();
        setValue(value);
    }

//    @OnClick({R.id.btn_reduce, R.id.btn_add})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_reduce://减
//
//                break;
//            case R.id.btn_add://加
//
//                break;
//        }
//    }

    public TextView setButtonColor() {
        return tvCount;
    }

    /**
     * 如果当前值大于最小值   减
     */
    private void reduce() {
        if (value > minValue) {
            value-=0.5;
        }
        setValue(value);
        if (onValueChangeListene != null) {
            onValueChangeListene.onValueChange(value);
        }
    }

    /**
     * 如果当前值小于最小值  加
     */
    private void add() {
        if (value < maxValue) {
            value+=0.5;
        }
        setValue(value);
        if (onValueChangeListene != null) {
            onValueChangeListene.onValueChange(value);
        }
    }

    //获取具体值
    public double getValue() {
        String countStr = tvCount.getText().toString().trim();
        if (countStr != null) {
            value = Integer.valueOf(countStr);
        }
        return value;
    }

    public void setValue(double value) {
        this.value = value;
       if (value>0){
            tvCount.setText("+"+value +"%"+ "");
        }else {
           tvCount.setText( value + "%"+"");
       }

    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_reduce) {
            tvCount.setTextColor(getResources().getColor(R.color.tip_black));
            reduce();

        } else if (i == R.id.btn_add) {
            tvCount.setTextColor(getResources().getColor(R.color.tip_black));
            add();

        }
    }


    //监听回调
    public interface OnValueChangeListener {
        public void onValueChange(double value);
    }

    private OnValueChangeListener onValueChangeListene;

    public void setOnValueChangeListene(OnValueChangeListener onValueChangeListene) {
        this.onValueChangeListene = onValueChangeListene;
    }
}
