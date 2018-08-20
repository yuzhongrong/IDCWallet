package com.cjwsc.idcm.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.cjwsc.idcm.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;


/**
 * Created by zw on 2017/11/29.
 */
public class XyMarkView extends MarkerView {
    public static final int ARROW_SIZE = 0; // 箭头的大小
    private static final float CIRCLE_OFFSET = 0;//因为我这里的折点是圆圈，所以要偏移，防止直接指向了圆心
    private static final float STOKE_WIDTH = 5;//这里对于stroke_width的宽度也要做一定偏移
    private static final float X_SETOFF= 35;//X轴的位移
    private  View root;
    private  TextView tvContent;

   // private final TextView tvContentY;
   // private final TextView tvContentX;
    //private LineChart chart;
    private CallBack mCallBack;
    public XyMarkView(Context context, int layoutResource) {
        super(context, layoutResource);
        root = findViewById(R.id.markcontirner);
        tvContent=(TextView) findViewById(R.id.tvContent);
     //   tvContentY = (TextView) findViewById(R.id.tvContentY);
      //  tvContentX = (TextView) findViewById(R.id.tvContentX);
       // this.chart=chart;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);

        String values;
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            values = "" + Utils.formatNumber(ce.getHigh(), 0, true);
       //     UtilLog.e("测试y的值01："+values);
        } else {

           values = com.cjwsc.idcm.Utils.Utils.toSubStringDegistForChart(Double.parseDouble(String.valueOf(e.getY())), 2,true);
          //  values=e.getY()+"";//对数据加工放在外面

         //   values=Utils.formatNumber(e.getY(), 2, true,'');

          //  values=""+"$ "+Utils.formatNumber(e.getY(), 0, true,',');

//            if (e.getY()>10){
//                values = "" + Utils.formatNumber(e.getY(), 0, true);
//            }else {
//                values = "" + Utils.formatNumber(e.getY(), 2, true);
//
//
//            }

       //     UtilLog.e("测试y的值02："+values);
        }
        if (mCallBack != null) {
            mCallBack.onCallBack(e.getX(), values,e.getData());
        }
        super.refreshContent(e, highlight);



    }
    public void setCallBack (CallBack callBack) {
        this.mCallBack = callBack;
    }
    public interface CallBack {
        void onCallBack(float x, String value, Object data);
    }
    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        MPPointF offset = getOffset();
      //  Chart chart = getChartView();
        float width = getWidth();
        float height = getHeight();
// posY \posX 指的是markerView左上角点在图表上面的位置
//处理Y方向
//        if (posY <= height + ARROW_SIZE) {// 如果点y坐标小于markerView的高度，如果不处理会超出上边界，处理了之后这时候箭头是向上的，我们需要把图标下移一个箭头的大小
//            offset.y = ARROW_SIZE;
//            UtilLog.e("01");
//        //    tvContent.setBackground(getResources().getDrawable(R.drawable.icon_zhongshang));
//        } else {//否则属于正常情况，因为我们默认是箭头朝下，然后正常偏移就是，需要向上偏移markerView高度和arrow size，再加一个stroke的宽度，因为你需要看到对话框的上面的边框
//            offset.y = -height - ARROW_SIZE - STOKE_WIDTH; // 40 arrow height   5 stroke width
//       //     tvContent.setBackground(getResources().getDrawable(R.drawable.icon_zhongxia));
//            UtilLog.e("02");
//        }
//处理X方向，分为3种情况，1、在图表左边 2、在图表中间 3、在图表右边
//
        if (posX > getChartView().getWidth() - width) {//如果超过右边界，则向左偏移markerView的宽度
            offset.x = -width+X_SETOFF;
          //  UtilLog.e("03");
            if (posY <= height + ARROW_SIZE) {// 如果点y坐标小于markerView的高度，如果不处理会超出上边界，处理了之后这时候箭头是向上的，我们需要把图标下移一个箭头的大小
                offset.y = ARROW_SIZE;
                root.setBackground(getResources().getDrawable(R.drawable.youshang));
            } else {//否则属于正常情况，因为我们默认是箭头朝下，然后正常偏移就是，需要向上偏移markerView高度和arrow size，再加一个stroke的宽度，因为你需要看到对话框的上面的边框
                offset.y = -height - ARROW_SIZE - STOKE_WIDTH; // 40 arrow height   5 stroke width
                root.setBackground(getResources().getDrawable(R.drawable.youxia));

            }

        } else {//默认情况，不偏移（因为是点是在左上角）
            offset.x = 0;
//            if (posY <= height + ARROW_SIZE) {// 如果点y坐标小于markerView的高度，如果不处理会超出上边界，处理了之后这时候箭头是向上的，我们需要把图标下移一个箭头的大小
//                offset.y = ARROW_SIZE;
//                root.setBackground(getResources().getDrawable(R.drawable.icon_zhongshang));
//            } else {//否则属于正常情况，因为我们默认是箭头朝下，然后正常偏移就是，需要向上偏移markerView高度和arrow size，再加一个stroke的宽度，因为你需要看到对话框的上面的边框
//                offset.y = -height - ARROW_SIZE - STOKE_WIDTH; // 40 arrow height   5 stroke width
//                root.setBackground(getResources().getDrawable(R.drawable.icon_zhongxia));
//            }
        //    tvContent.setBackground(getResources().getDrawable(R.drawable.icon_zhongxia));
            if (posX > width / 2) {//如果大于markerView的一半，说明箭头在中间，所以向右偏移一半宽度
                offset.x = -(width / 2);
                if (posY <= height + ARROW_SIZE) {// 如果点y坐标小于markerView的高度，如果不处理会超出上边界，处理了之后这时候箭头是向上的，我们需要把图标下移一个箭头的大小
                    offset.y = ARROW_SIZE;
                    root.setBackground(getResources().getDrawable(R.drawable.zhongshang));
                } else {//否则属于正常情况，因为我们默认是箭头朝下，然后正常偏移就是，需要向上偏移markerView高度和arrow size，再加一个stroke的宽度，因为你需要看到对话框的上面的边框
                    offset.y = -height - ARROW_SIZE - STOKE_WIDTH; // 40 arrow height   5 stroke width
                    root.setBackground(getResources().getDrawable(R.drawable.zhongxia));
                }
            }else {
                offset.x = -(-width / 2)-width / 2-X_SETOFF-STOKE_WIDTH;
                if (posY <= height + ARROW_SIZE) {// 如果点y坐标小于markerView的高度，如果不处理会超出上边界，处理了之后这时候箭头是向上的，我们需要把图标下移一个箭头的大小
                    offset.y = ARROW_SIZE;
                    root.setBackground(getResources().getDrawable(R.drawable.zuoshang));
                } else {//否则属于正常情况，因为我们默认是箭头朝下，然后正常偏移就是，需要向上偏移markerView高度和arrow size，再加一个stroke的宽度，因为你需要看到对话框的上面的边框
                    offset.y = -height - ARROW_SIZE - STOKE_WIDTH; // 40 arrow height   5 stroke width

                    root.setBackground(getResources().getDrawable(R.drawable.zuoxia));

                }
            }
        }
        return offset;
    }

//    public TextView getTvContentY() {
//        return tvContentY;
//    }
//    public TextView getTvContentX() {
//        return tvContentX;
//    }


    public View getRootView() {
        return root;
    }

    public TextView getTvContent() {
        return tvContent;
    }


    public View getViewById(int id){
      return   findViewById(id);

    }
}
